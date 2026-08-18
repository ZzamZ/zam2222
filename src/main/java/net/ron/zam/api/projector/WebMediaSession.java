package net.ron.zam.api.projector;

import net.minecraft.resources.Identifier;
import net.ron.zam.ZAMMod;
import org.jetbrains.annotations.Nullable;

public final class WebMediaSession implements AutoCloseable {
    private final Identifier textureId;
    private final int width, height;

    @Nullable private volatile ResolvedMedia resolved;
    private volatile boolean resolving = true, resolveFailed;

    @Nullable private FFmpegVideoSession video;
    @Nullable private FFmpegAudioSession audio;

    private boolean paused, started, closed;
    private float broadcasterVolume = 1F, distanceVolume = 1F;

    public WebMediaSession(Identifier textureId, String url, int width, int height) {
        this.textureId = textureId;
        this.width = width;
        this.height = height;

        Thread thread = new Thread(() -> resolve(url), "zam-media-resolver");
        thread.setDaemon(true);
        thread.start();
    }

    private void resolve(String url) {
        try {
            resolved = MediaResolver.resolve(url);
        } catch (Exception e) {
            if (!closed) {
                resolveFailed = true;
                ZAMMod.LOGGER.error("Failed to resolve media {}", url, e);
            }
        } finally {
            resolving = false;
        }
    }

    private void createSessions() {
        if (closed || video != null || resolved == null) return;

        video = new FFmpegVideoSession(textureId, resolved.video(), width, height, 0);

        if (resolved.audio() != null)
            audio = new FFmpegAudioSession(resolved.audio(), 0);
    }

    public void update() {
        if (closed || resolveFailed || resolving) return;

        createSessions();

        if (video == null || video.failed() || video.ended())
            return;

        if (!started && !paused && video.prepared()) {
            if (audio != null && audio.failed()) {
                audio.close();
                audio = null;
            }

            if (audio != null && audio.prepared()) {
                audio.setBroadcasterVolume(broadcasterVolume);
                audio.setDistanceVolume(distanceVolume);
                audio.beginPlayback();

                video.beginPlayback(audio::playbackNanos);
                started = true;
            } else if (audio == null) {
                long start = System.nanoTime();
                video.beginPlayback(() -> System.nanoTime() - start);
                started = true;
            }
        }

        if (started && !paused)
            video.present();
    }

    public boolean ready() {
        return !closed && started && !failed() && !ended();
    }

    public boolean hasFrame() {
        return !closed && video != null && video.hasFrame();
    }

    public boolean failed() {
        return !closed && (resolveFailed || video != null && video.failed());
    }

    public boolean ended() {
        return closed || video != null && video.ended();
    }

    public Identifier texture() {
        return video != null ? video.texture() : textureId;
    }

    public void pause() {
        if (closed || paused) return;

        paused = true;

        if (video != null) video.pausePlayback();
        if (audio != null) audio.pausePlayback();
    }

    public void resume() {
        if (closed || !paused) return;

        paused = false;

        if (video != null) video.resumePlayback();
        if (audio != null) audio.resumePlayback();
    }

    public void setBroadcasterVolume(float value) {
        broadcasterVolume = Math.clamp(value, 0F, 1F);

        if (audio != null)
            audio.setBroadcasterVolume(broadcasterVolume);
    }

    public void setDistanceVolume(float value) {
        distanceVolume = Math.clamp(value, 0F, 1F);

        if (audio != null)
            audio.setDistanceVolume(distanceVolume);
    }

    @Override
    public void close() {
        if (closed) return;

        closed = true;

        if (video != null) {
            video.close();
            video = null;
        }

        if (audio != null) {
            audio.close();
            audio = null;
        }
    }
}