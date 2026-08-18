package net.ron.zam.api.projector;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.ron.zam.ZAMMod;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.LongSupplier;

public final class FFmpegVideoSession implements AutoCloseable {
    private static final int FPS = 60;
    private static final long FRAME_NS = 1_000_000_000L / FPS;
    private static final int QUEUE_SIZE = 3, BUFFER_COUNT = 4;

    private final Identifier textureId;
    private final ResolvedMedia.Stream source;
    private final int width, height, frameSize;

    private final ArrayBlockingQueue<VideoFrame> frames = new ArrayBlockingQueue<>(QUEUE_SIZE);
    private final ArrayBlockingQueue<ByteBuffer> buffers = new ArrayBlockingQueue<>(BUFFER_COUNT);
    private final AtomicBoolean uploadQueued = new AtomicBoolean();

    private volatile LongSupplier playbackClock = () -> -1L;
    private volatile boolean closed, running, prepared, playbackRequested, paused, failed, decodeEnded, ended;

    private long decodedIndex, presentedIndex = -1, lastDecodedIndex = -1;

    @Nullable private volatile Process process;
    @Nullable private volatile BlazeVideoTexture texture;

    public FFmpegVideoSession(Identifier textureId, ResolvedMedia.Stream source,
                              int width, int height, double startSeconds) {
        this.textureId = textureId;
        this.source = source;
        this.width = width;
        this.height = height;
        this.frameSize = width * height * 4;

        texture = new BlazeVideoTexture("ZAM Video " + textureId, width, height);
        Minecraft.getInstance().getTextureManager().register(textureId, texture);
        start(startSeconds);
    }

    private void start(double startSeconds) {
        if (closed || running) return;

        running = true;
        prepared = playbackRequested = paused = failed = decodeEnded = ended = false;
        decodedIndex = 0;
        presentedIndex = lastDecodedIndex = -1;

        Thread thread = new Thread(() -> decode(startSeconds),
                "zam-video-decoder-" + textureId.getPath());

        thread.setDaemon(true);
        thread.start();
    }

    public void beginPlayback(LongSupplier clock) {
        if (closed) return;
        playbackClock = clock;
        playbackRequested = true;
    }

    public void pausePlayback() {
        if (!closed) paused = true;
    }

    public void resumePlayback() {
        if (!closed) paused = false;
    }

    private void decode(double startSeconds) {
        ByteBuffer buffer = null;

        try {
            for (int i = 0; i < BUFFER_COUNT; i++)
                buffers.put(ByteBuffer.allocateDirect(frameSize));

            if (closed) return;

            List<String> command = new ArrayList<>();
            command.add(ProjectorTools.ffmpeg().toAbsolutePath().toString());
            command.add("-hide_banner");
            command.add("-loglevel");
            command.add("error");

            inputOptions(command, source);

            command.add("-hwaccel");
            command.add("auto");

            if (startSeconds > 0) {
                command.add("-ss");
                command.add(Double.toString(startSeconds));
            }

            command.add("-i");
            command.add(source.url());
            command.add("-an");
            command.add("-sn");
            command.add("-dn");
            command.add("-vf");
            command.add("fps=" + FPS
                    + ",scale=" + width + ":" + height
                    + ":force_original_aspect_ratio=decrease:flags=fast_bilinear"
                    + ",pad=" + width + ":" + height
                    + ":(ow-iw)/2:(oh-ih)/2:black");
            command.add("-pix_fmt");
            command.add("rgba");
            command.add("-f");
            command.add("rawvideo");
            command.add("pipe:1");

            Process createdProcess = new ProcessBuilder(command).start();

            if (closed) {
                createdProcess.destroyForcibly();
                return;
            }

            process = createdProcess;
            startLogger(createdProcess);

            try (ReadableByteChannel input = Channels.newChannel(createdProcess.getInputStream())) {
                buffer = takeBuffer();
                if (buffer == null) return;

                if (!readFrame(input, buffer)) {
                    recycle(buffer);
                    buffer = null;

                    if (!closed) failed = true;
                    return;
                }

                frames.put(new VideoFrame(buffer, 0));
                buffer = null;

                decodedIndex = 1;
                lastDecodedIndex = 0;
                prepared = true;

                while (!closed && running && !playbackRequested)
                    Thread.sleep(1L);

                if (closed || !running) return;

                while (!closed && running) {
                    while (!closed && running && paused)
                        Thread.sleep(2L);

                    if (closed || !running) break;

                    buffer = takeBuffer();
                    if (buffer == null) break;

                    if (!readFrame(input, buffer)) {
                        recycle(buffer);
                        buffer = null;

                        if (!closed) decodeEnded = true;
                        break;
                    }

                    long index = decodedIndex++;
                    lastDecodedIndex = index;

                    VideoFrame frame = new VideoFrame(buffer, index);
                    buffer = null;

                    while (!closed && running
                            && !frames.offer(frame, 10, TimeUnit.MILLISECONDS)) {}

                    if (closed || !running)
                        recycle(frame.data());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            if (!closed) {
                failed = true;
                ZAMMod.LOGGER.error("Video decoder failed for {}", textureId, e);
            }
        } finally {
            recycle(buffer);

            if (!closed && !prepared)
                failed = true;

            running = false;
        }
    }

    public void present() {
        if (closed || paused || failed || ended || !playbackRequested || !prepared)
            return;

        long clock = playbackClock.getAsLong();
        if (clock < 0) return;

        long targetIndex = Math.max(0, clock) / FRAME_NS;
        VideoFrame chosen = null;

        while (true) {
            VideoFrame next = frames.peek();

            if (next == null || next.index() > targetIndex)
                break;

            next = frames.poll();

            if (chosen != null)
                recycle(chosen.data());

            chosen = next;
        }

        if (chosen != null) {
            if (chosen.index() > presentedIndex) {
                presentedIndex = chosen.index();
                queueUpload(chosen.data());
            } else {
                recycle(chosen.data());
            }
        }

        if (decodeEnded && frames.isEmpty() && presentedIndex >= lastDecodedIndex)
            finishPlayback();
    }

    private void queueUpload(ByteBuffer frame) {
        if (!uploadQueued.compareAndSet(false, true)) {
            recycle(frame);
            return;
        }

        Minecraft.getInstance().execute(() -> {
            try {
                BlazeVideoTexture current = texture;

                if (!closed && !ended && current != null)
                    current.upload(frame);
            } finally {
                recycle(frame);
                uploadQueued.set(false);
            }
        });
    }

    private void finishPlayback() {
        if (ended) return;
        ended = true;

        VideoFrame frame;
        while ((frame = frames.poll()) != null)
            recycle(frame.data());
    }

    @Nullable
    private ByteBuffer takeBuffer() throws InterruptedException {
        while (!closed && running) {
            ByteBuffer buffer = buffers.poll(10, TimeUnit.MILLISECONDS);
            if (buffer != null) return buffer;
        }

        return null;
    }

    private void recycle(@Nullable ByteBuffer buffer) {
        if (buffer == null || closed) return;

        buffer.clear();
        buffers.offer(buffer);
    }

    public boolean prepared() { return prepared; }
    public boolean hasFrame() { return prepared && !ended; }
    public boolean failed() { return failed; }
    public boolean ended() { return ended; }

    public Identifier texture() {
        return textureId;
    }

    private static boolean readFrame(ReadableByteChannel input, ByteBuffer destination)
            throws IOException {
        destination.clear();

        while (destination.hasRemaining()) {
            int read = input.read(destination);

            if (read < 0) return false;
            if (read == 0) Thread.onSpinWait();
        }

        destination.flip();
        return true;
    }

    private static void inputOptions(List<String> command, ResolvedMedia.Stream source) {
        if (!remote(source.url())) return;

        Collections.addAll(command,
                "-reconnect", "1",
                "-reconnect_streamed", "1",
                "-reconnect_on_network_error", "1",
                "-reconnect_on_http_error", "4xx,5xx",
                "-reconnect_delay_max", "5");

        String headers = headers(source.headers());

        if (!headers.isBlank()) {
            command.add("-headers");
            command.add(headers);
        }
    }

    private static String headers(Map<String, String> headers) {
        StringBuilder result = new StringBuilder();

        headers.forEach((key, value) -> {
            String k = key.replace("\r", "").replace("\n", "");
            String v = value.replace("\r", "").replace("\n", "");

            if (!k.isBlank() && !v.isBlank())
                result.append(k).append(": ").append(v).append("\r\n");
        });

        return result.toString();
    }

    private static boolean remote(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

    private void startLogger(Process process) {
        Thread thread = new Thread(() -> {
            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    if (closed) break;
                    ZAMMod.LOGGER.warn("[FFmpeg Video] {}", line);
                }
            } catch (Exception ignored) {}
        }, "zam-ffmpeg-video-log");

        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void close() {
        if (closed) return;

        closed = true;
        running = false;
        playbackRequested = true;
        paused = false;

        Process current = process;
        process = null;

        if (current != null)
            current.destroyForcibly();

        texture = null;

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.isSameThread())
            minecraft.getTextureManager().release(textureId);
        else
            minecraft.execute(() -> minecraft.getTextureManager().release(textureId));
    }

    private record VideoFrame(ByteBuffer data, long index) {}
}