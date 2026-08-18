package net.ron.zam.api.projector;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.ron.zam.ZAMMod;
import net.ron.zam.common.block.television.*;
import net.ron.zam.common.component.VideoMediaComponent;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.*;

public final class WebVideoManager {
    private static final Map<UUID, Entry> SESSIONS = new HashMap<>();

    private static final int VIDEO_WIDTH = 1920, VIDEO_HEIGHT = 1080;
    private static final float MAX_MEDIA_VOLUME = 0.35F;
    private static final double MAX_AUDIO_DISTANCE = 64D;
    private static final long RETRY_DELAY = 30_000L;

    private WebVideoManager() {}

    public enum MediaState {
        LOADING, PLAYING, PAUSED, ENDED, FAILED
    }

    @Nullable
    public static Identifier texture(TelevisionBlockEntity tv, VideoMediaComponent media) {
        Entry entry = getOrCreate(tv, media);

        if (entry == null || entry.session == null || entry.session.ended())
            return null;

        if (entry.session.failed()) {
            retry(entry);
            return null;
        }

        return entry.session.hasFrame() ? entry.session.texture() : null;
    }

    public static MediaState state(TelevisionBlockEntity tv, VideoMediaComponent media) {
        Entry entry = getOrCreate(tv, media);

        if (entry == null || entry.session == null)
            return MediaState.LOADING;

        if (entry.session.ended())
            return MediaState.ENDED;

        if (entry.session.failed()) {
            retry(entry);
            return MediaState.LOADING;
        }

        if (!powered(tv) || !tv.isPlaying())
            return MediaState.PAUSED;

        return entry.session.ready() ? MediaState.PLAYING : MediaState.LOADING;
    }

    public static void tick() {
        Iterator<Entry> iterator = SESSIONS.values().iterator();

        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            TelevisionBlockEntity tv = entry.tv;

            if (tv == null || tv.isRemoved() || tv.getLevel() == null) {
                close(entry);
                iterator.remove();
                continue;
            }

            if (entry.session == null)
                continue;

            if (entry.session.failed()) {
                retry(entry);
                continue;
            }

            settings(entry.session, tv, entry.volume);

            if (!powered(tv) || !tv.isPlaying())
                entry.session.pause();
            else
                entry.session.resume();

            entry.session.update();
        }
    }

    public static void renderFrame() {
        for (Entry entry : SESSIONS.values())
            if (entry.session != null)
                entry.session.present();
    }

    @Nullable
    private static Entry getOrCreate(TelevisionBlockEntity tv, VideoMediaComponent media) {
        if (tv.getLevel() == null || media.url().isBlank()) return null;

        UUID id = playbackId(tv);
        Entry entry = SESSIONS.get(id);

        if (entry != null && !entry.url.equals(media.url())) {
            remove(id);
            entry = null;
        }

        if (entry == null) {
            entry = new Entry(id, media.url(), tv, media.volume());
            entry.session = createSession(id, media.url());
            SESSIONS.put(id, entry);
        } else {
            entry.tv = tv;
            entry.volume = media.volume();
        }

        return entry;
    }

    private static WebMediaSession createSession(UUID id, String url) {
        return new WebMediaSession(
                ZAMMod.id("video/" + id.toString().replace("-", "")),
                url,
                VIDEO_WIDTH,
                VIDEO_HEIGHT
        );
    }

    private static void retry(Entry entry) {
        long now = System.currentTimeMillis();
        if (now - entry.lastRetry < RETRY_DELAY) return;

        entry.lastRetry = now;
        MediaResolver.invalidate(entry.url);
        close(entry);

        ZAMMod.LOGGER.info("Media stream failed, resolving again");
        entry.session = createSession(entry.id, entry.url);
    }

    private static UUID playbackId(TelevisionBlockEntity tv) {
        String value = tv.getLevel().dimension().identifier()
                + "@" + tv.getBlockPos().asLong();

        return UUID.nameUUIDFromBytes(value.getBytes(StandardCharsets.UTF_8));
    }

    private static void settings(WebMediaSession session, TelevisionBlockEntity tv, float volume) {
        session.setBroadcasterVolume(volume * MAX_MEDIA_VOLUME);

        var player = Minecraft.getInstance().player;

        if (player == null) {
            session.setDistanceVolume(0F);
            return;
        }

        double distance = player.position().distanceTo(Vec3.atCenterOf(tv.getBlockPos()));
        session.setDistanceVolume(
                (float) Math.clamp(1D - distance / MAX_AUDIO_DISTANCE, 0D, 1D)
        );
    }

    private static boolean powered(TelevisionBlockEntity tv) {
        if (tv.getLevel() == null) return false;

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

    public static void clear() {
        Entry[] entries = SESSIONS.values().toArray(Entry[]::new);
        SESSIONS.clear();

        for (Entry entry : entries)
            close(entry);
    }

    private static void remove(UUID id) {
        Entry entry = SESSIONS.remove(id);
        if (entry != null) close(entry);
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
        private TelevisionBlockEntity tv;
        private float volume;

        @Nullable private WebMediaSession session;
        private long lastRetry;

        private Entry(UUID id, String url, TelevisionBlockEntity tv, float volume) {
            this.id = id;
            this.url = url;
            this.tv = tv;
            this.volume = volume;
        }
    }
}