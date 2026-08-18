package net.ron.zam.api.projector;

import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public final class WebMediaSession implements AutoCloseable {
    private final FFmpegVideoSession video;

    @Nullable
    private FFmpegAudioSession audio;

    private boolean paused, started, closed;
    private float broadcasterVolume = 1.0F, distanceVolume = 1.0F;

    public WebMediaSession(Identifier textureId, String url, int width, int height) {
        video = new FFmpegVideoSession(textureId, url, width, height, 0);
        audio = new FFmpegAudioSession(url, 0);
    }

    public void update() {
        if (closed || paused || started || video.failed() || video.ended() || !video.prepared())
            return;

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

    public boolean ready() {
        return !closed && started && !failed() && !ended();
    }

    public boolean hasFrame() {
        return !closed && !ended() && video.hasFrame();
    }

    public boolean failed() {
        return !closed && (video.failed() || (audio != null && audio.failed()));
    }

    public boolean ended() {
        return closed || video.ended();
    }

    public Identifier texture() {
        return video.texture();
    }

    public void pause() {
        if (closed || paused) return;

        paused = true;
        video.pausePlayback();

        if (audio != null)
            audio.pausePlayback();
    }

    public void resume() {
        if (closed || !paused) return;

        paused = false;
        video.resumePlayback();

        if (audio != null)
            audio.resumePlayback();
    }

    public void setBroadcasterVolume(float value) {
        broadcasterVolume = Math.clamp(value, 0.0F, 1.0F);

        if (audio != null)
            audio.setBroadcasterVolume(broadcasterVolume);
    }

    public void setDistanceVolume(float value) {
        distanceVolume = Math.clamp(value, 0.0F, 1.0F);

        if (audio != null)
            audio.setDistanceVolume(distanceVolume);
    }

    @Override
    public void close() {
        if (closed) return;

        closed = true;

        video.close();

        FFmpegAudioSession currentAudio = audio;
        audio = null;

        if (currentAudio != null)
            currentAudio.close();
    }
}