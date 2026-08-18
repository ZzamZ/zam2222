package net.ron.zam.api.projector;

import net.ron.zam.ZAMMod;

import javax.sound.sampled.*;
import java.io.*;
import java.util.*;

public final class FFmpegAudioSession implements AutoCloseable {
    private static final float RATE = 48_000F;
    private static final int BUFFER = 8192;

    private final ResolvedMedia.Stream source;
    private final double startSeconds;

    private volatile boolean closed, prepared, startRequested, ready, paused, failed, ended;
    private volatile float broadcasterVolume = 1F, distanceVolume = 1F;
    private volatile long lastPlaybackNanos = -1L;
    private volatile Process process;
    private volatile SourceDataLine line;

    public FFmpegAudioSession(ResolvedMedia.Stream source, double startSeconds) {
        this.source = source;
        this.startSeconds = startSeconds;

        Thread thread = new Thread(this::run, "zam-web-audio");
        thread.setDaemon(true);
        thread.start();
    }

    private void run() {
        boolean eof = false;

        try {
            AudioFormat format = new AudioFormat(RATE, 16, 2, true, false);
            SourceDataLine created = (SourceDataLine) AudioSystem.getLine(
                    new DataLine.Info(SourceDataLine.class, format));

            created.open(format, BUFFER);

            if (closed) {
                safeClose(created);
                return;
            }

            line = created;

            List<String> command = new ArrayList<>();
            command.add(ProjectorTools.ffmpeg().toAbsolutePath().toString());
            command.add("-hide_banner");
            command.add("-loglevel");
            command.add("error");

            inputOptions(command, source);

            if (startSeconds > 0) {
                command.add("-ss");
                command.add(Double.toString(startSeconds));
            }

            command.add("-i");
            command.add(source.url());
            command.add("-vn");
            command.add("-sn");
            command.add("-dn");
            command.add("-ac");
            command.add("2");
            command.add("-ar");
            command.add("48000");
            command.add("-f");
            command.add("s16le");
            command.add("-acodec");
            command.add("pcm_s16le");
            command.add("pipe:1");

            Process createdProcess = new ProcessBuilder(command).start();

            if (closed) {
                createdProcess.destroyForcibly();
                return;
            }

            process = createdProcess;
            startLogger(createdProcess);

            byte[] input = new byte[BUFFER], output = new byte[BUFFER];

            try (BufferedInputStream stream =
                         new BufferedInputStream(createdProcess.getInputStream(), BUFFER)) {
                int first = stream.read(input);

                if (first < 0) {
                    failed = true;
                    return;
                }

                prepared = true;

                while (!closed && !startRequested)
                    Thread.sleep(1L);

                if (closed) return;

                created.start();
                write(input, output, first);
                ready = true;

                while (!closed) {
                    while (!closed && paused)
                        Thread.sleep(2L);

                    if (closed) break;

                    int read = stream.read(input);

                    if (read < 0) {
                        eof = true;
                        break;
                    }

                    write(input, output, read);
                }
            }

            if (!closed && eof) {
                capturePosition();
                ended = true;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            if (!closed) {
                failed = true;
                ZAMMod.LOGGER.error("Web audio decoder failed", e);
            }
        } finally {
            if (!closed) {
                capturePosition();

                if (!ready && !ended)
                    failed = true;

                cleanup(detach());
            }
        }
    }

    private void write(byte[] input, byte[] output, int length) {
        float volume = Math.clamp(broadcasterVolume * distanceVolume, 0F, 1F);

        for (int i = 0; i + 1 < length; i += 2) {
            short sample = (short) ((input[i] & 255) | input[i + 1] << 8);
            int scaled = Math.clamp(Math.round(sample * volume), Short.MIN_VALUE, Short.MAX_VALUE);

            output[i] = (byte) scaled;
            output[i + 1] = (byte) (scaled >> 8);
        }

        SourceDataLine current = line;

        if (!closed && current != null && !paused)
            current.write(output, 0, length);
    }

    private void capturePosition() {
        SourceDataLine current = line;

        if (current != null)
            lastPlaybackNanos = framesToNanos(current.getLongFramePosition());
    }

    public boolean prepared() { return prepared; }
    public boolean failed() { return failed; }
    public boolean ended() { return ended; }

    public void beginPlayback() {
        startRequested = true;
    }

    public void pausePlayback() {
        if (paused || closed) return;

        paused = true;
        SourceDataLine current = line;

        if (current != null && current.isOpen())
            current.stop();
    }

    public void resumePlayback() {
        if (!paused || closed) return;

        paused = false;
        SourceDataLine current = line;

        if (current != null && current.isOpen() && startRequested)
            current.start();
    }

    public long playbackNanos() {
        SourceDataLine current = line;

        if (current != null && ready && !closed)
            lastPlaybackNanos = framesToNanos(current.getLongFramePosition());

        return lastPlaybackNanos;
    }

    public void setBroadcasterVolume(float value) {
        broadcasterVolume = Math.clamp(value, 0F, 1F);
    }

    public void setDistanceVolume(float value) {
        distanceVolume = Math.clamp(value, 0F, 1F);
    }

    private static long framesToNanos(long frames) {
        return (long) (frames / RATE * 1_000_000_000D);
    }

    private static void inputOptions(List<String> command, ResolvedMedia.Stream source) {
        if (!remote(source.url())) return;

        command.add("-reconnect");
        command.add("1");
        command.add("-reconnect_streamed");
        command.add("1");
        command.add("-reconnect_on_network_error");
        command.add("1");
        command.add("-reconnect_on_http_error");
        command.add("4xx,5xx");
        command.add("-reconnect_delay_max");
        command.add("5");

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
                    ZAMMod.LOGGER.warn("[FFmpeg Audio] {}", line);
                }
            } catch (Exception ignored) {}
        }, "zam-ffmpeg-audio-log");

        thread.setDaemon(true);
        thread.start();
    }

    private synchronized Resources detach() {
        Resources resources = new Resources(process, line);
        process = null;
        line = null;
        return resources;
    }

    private static void cleanup(Resources resources) {
        if (resources.process() != null)
            resources.process().destroyForcibly();

        if (resources.line() != null)
            safeClose(resources.line());
    }

    private static void safeClose(SourceDataLine line) {
        try { line.stop(); } catch (Exception ignored) {}
        try { line.flush(); } catch (Exception ignored) {}
        try { line.close(); } catch (Exception ignored) {}
    }

    @Override
    public void close() {
        if (closed) return;

        closed = true;
        paused = false;
        startRequested = true;

        Resources resources = detach();

        if (resources.process() != null)
            resources.process().destroyForcibly();

        if (resources.line() != null) {
            Thread thread = new Thread(() -> safeClose(resources.line()), "zam-audio-close");
            thread.setDaemon(true);
            thread.start();
        }
    }

    private record Resources(Process process, SourceDataLine line) {}
}