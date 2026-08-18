package net.ron.zam.api.projector;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.ron.zam.ZAMMod;
import net.ron.zam.common.block.television.TelevisionBlock;
import net.ron.zam.common.block.television.TelevisionBlockEntity;
import net.ron.zam.common.block.television.TelevisionMultiblock;
import net.ron.zam.common.component.VideoMediaComponent;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class WebVideoManager {
    private static final Map<UUID, Entry> SESSIONS = new HashMap<>();

    private static final int VIDEO_WIDTH = 1920, VIDEO_HEIGHT = 1080;
    private static final float MAX_VIDEO_VOLUME = 0.35F;
    private static final long STREAM_RETRY_DELAY = 30_000L, UNUSED_TIMEOUT = 5 * 60_000L;
    private static final double MAX_AUDIO_DISTANCE = 32.0D, FULL_VOLUME_DISTANCE = 4.0D;

    private WebVideoManager() {}

    public enum MediaState {
        LOADING, PLAYING, PAUSED, ENDED, FAILED
    }

    @Nullable
    public static Identifier texture(TelevisionBlockEntity tv, VideoMediaComponent media) {
        Entry entry = getOrCreate(tv, media);

        if (entry == null || entry.failed || entry.session == null || entry.session.ended())
            return null;

        if (entry.session.failed()) {
            retrySession(entry);
            return null;
        }

        entry.lastUsed = System.currentTimeMillis();
        applySettings(entry.session, tv, media);

        if (!powered(tv) || !tv.isPlaying()) {
            entry.session.pause();
            return entry.session.hasFrame() ? entry.session.texture() : null;
        }

        entry.session.resume();
        entry.session.update();

        return entry.session.hasFrame()
                ? entry.session.texture()
                : null;
    }

    public static MediaState state(TelevisionBlockEntity tv, VideoMediaComponent media) {
        Entry entry = getOrCreate(tv, media);

        if (entry == null || entry.failed)
            return MediaState.FAILED;

        if (entry.session == null)
            return MediaState.LOADING;

        if (entry.session.ended())
            return MediaState.ENDED;

        if (entry.session.failed()) {
            retrySession(entry);
            return MediaState.LOADING;
        }

        entry.lastUsed = System.currentTimeMillis();
        applySettings(entry.session, tv, media);

        if (!powered(tv) || !tv.isPlaying()) {
            entry.session.pause();
            return MediaState.PAUSED;
        }

        entry.session.resume();
        entry.session.update();

        return entry.session.ready()
                ? MediaState.PLAYING
                : MediaState.LOADING;
    }

    @Nullable
    private static Entry getOrCreate(TelevisionBlockEntity tv, VideoMediaComponent media) {
        if (tv.getLevel() == null || media.url().isBlank())
            return null;

        UUID id = playbackId(tv);
        long now = System.currentTimeMillis();
        Entry entry = SESSIONS.get(id);

        if (entry != null && !entry.url.equals(media.url())) {
            remove(id);
            entry = null;
        }

        if (entry == null) {
            entry = new Entry(id, media.url(), now);
            entry.session = createSession(id, media.url());
            SESSIONS.put(id, entry);
        }

        return entry;
    }

    private static WebMediaSession createSession(UUID id, String url) {
        Identifier texture = ZAMMod.id("video/" + id.toString().replace("-", ""));
        return new WebMediaSession(texture, url, VIDEO_WIDTH, VIDEO_HEIGHT);
    }

    private static void retrySession(Entry entry) {
        long now = System.currentTimeMillis();

        if (now - entry.lastStreamRetry < STREAM_RETRY_DELAY)
            return;

        entry.lastStreamRetry = now;
        close(entry);

        ZAMMod.LOGGER.info("Projector media stream failed, retrying");
        entry.session = createSession(entry.id, entry.url);
    }

    private static UUID playbackId(TelevisionBlockEntity tv) {
        String value =
                tv.getLevel().dimension().identifier()
                        + "@"
                        + tv.getBlockPos().asLong();

        return UUID.nameUUIDFromBytes(
                value.getBytes(StandardCharsets.UTF_8)
        );
    }

    private static void applySettings(WebMediaSession session, TelevisionBlockEntity tv,
                                      VideoMediaComponent media) {
        session.setBroadcasterVolume(media.volume() * MAX_VIDEO_VOLUME);

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            session.setDistanceVolume(0.0F);
            return;
        }

        double distance = minecraft.player.position()
                .distanceTo(Vec3.atCenterOf(tv.getBlockPos()));

        session.setDistanceVolume(attenuation(distance));
    }

    private static float attenuation(double distance) {
        if (distance <= FULL_VOLUME_DISTANCE)
            return 1.0F;

        if (distance >= MAX_AUDIO_DISTANCE)
            return 0.0F;

        double value =
                1.0D
                        - (distance - FULL_VOLUME_DISTANCE)
                        / (MAX_AUDIO_DISTANCE - FULL_VOLUME_DISTANCE);

        return (float) (value * value);
    }

    private static boolean powered(TelevisionBlockEntity tv) {
        if (tv.getLevel() == null)
            return false;

        Direction facing = tv.getBlockState().getValue(TelevisionBlock.FACING);

        return TelevisionMultiblock.isAnyConnectedTvPowered(
                tv.getLevel(),
                tv.getBlockPos(),
                facing
        );
    }

    public static void stop(TelevisionBlockEntity tv) {
        if (tv.getLevel() != null)
            remove(playbackId(tv));
    }

    public static void cleanup() {
        long now = System.currentTimeMillis();

        SESSIONS.entrySet().removeIf(entry -> {
            if (now - entry.getValue().lastUsed <= UNUSED_TIMEOUT)
                return false;

            close(entry.getValue());
            return true;
        });
    }

    public static void clear() {
        Entry[] entries = SESSIONS.values().toArray(Entry[]::new);
        SESSIONS.clear();

        for (Entry entry : entries)
            close(entry);
    }

    private static void remove(UUID id) {
        Entry entry = SESSIONS.remove(id);

        if (entry != null)
            close(entry);
    }

    private static void close(Entry entry) {
        WebMediaSession session = entry.session;
        entry.session = null;

        if (session != null)
            session.close();
    }

    private static final class Entry {
        private final UUID id;
        private final String url;

        @Nullable
        private WebMediaSession session;

        private boolean failed;
        private long lastUsed, lastStreamRetry;

        private Entry(UUID id, String url, long now) {
            this.id = id;
            this.url = url;
            this.lastUsed = now;
        }
    }
}