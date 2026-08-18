package net.ron.zam.api.musicbox;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.ron.zam.common.item.MusicBoxItem;
import net.ron.zam.registry.ZAMComponents;

import java.util.Optional;

/**
 * Handles Music Box playback, playlists, stopping, and looping.
 */
public final class SoundTracker {

    private static final Int2ObjectArrayMap<SoundInstance> ENTITY_SOUNDS =
            new Int2ObjectArrayMap<>();

    /**
     * Stores the original record or Record Sleeve.
     */
    private static final Int2ObjectArrayMap<ItemStack> ENTITY_RECORDS =
            new Int2ObjectArrayMap<>();

    /**
     * Stores the active playlist track for each entity.
     */
    private static final Int2IntArrayMap ENTITY_TRACKS =
            new Int2IntArrayMap();

    private SoundTracker() {
    }

    /**
     * Begins playing a normal record or starts a Record Sleeve
     * playlist at its first track.
     */
    public static void playMusicBox(
            int entityId,
            ItemStack record
    ) {
        stopMusicBox(entityId);

        if (record.isEmpty()
                || PlayableRecord.getTrackCount(record) == 0) {
            return;
        }

        playTrack(entityId, record, 0);
    }

    /**
     * Starts one track without discarding the playlist.
     */
    private static void playTrack(
            int entityId,
            ItemStack record,
            int track
    ) {
        stopCurrentSound(entityId);

        ClientLevel level = Minecraft.getInstance().level;

        if (level == null) {
            return;
        }

        Entity entity = level.getEntity(entityId);

        if (entity == null) {
            return;
        }

        Optional<SoundInstance> soundOptional =
                PlayableRecord.createEntitySound(
                        record,
                        entity,
                        track,
                        8
                );

        if (soundOptional.isEmpty()) {
            return;
        }

        SoundInstance newSound = soundOptional.get();

        ENTITY_SOUNDS.put(entityId, newSound);
        ENTITY_RECORDS.put(entityId, record.copy());
        ENTITY_TRACKS.put(entityId, track);

        Minecraft.getInstance()
                .getSoundManager()
                .play(newSound);
    }

    /**
     * Stops only the current SoundInstance while retaining
     * playlist information.
     */
    private static void stopCurrentSound(int entityId) {
        SoundInstance oldSound =
                ENTITY_SOUNDS.remove(entityId);

        if (oldSound != null) {
            Minecraft.getInstance()
                    .getSoundManager()
                    .stop(oldSound);
        }
    }

    /**
     * Completely stops playback and clears playlist state.
     */
    public static void stopMusicBox(int entityId) {
        stopCurrentSound(entityId);

        ENTITY_RECORDS.remove(entityId);
        ENTITY_TRACKS.remove(entityId);
    }

    /**
     * Returns the currently playing sound for an entity.
     */
    public static SoundInstance getEntitySound(int entityId) {
        return ENTITY_SOUNDS.get(entityId);
    }

    /**
     * Returns the active track when this exact record or sleeve
     * is currently playing.
     *
     * @return the track index, or -1 if it is not playing
     */
    public static int getCurrentTrack(
            int entityId,
            ItemStack record
    ) {
        ItemStack trackedRecord =
                ENTITY_RECORDS.get(entityId);

        if (trackedRecord == null
                || !ItemStack.matches(
                trackedRecord,
                record
        )) {
            return -1;
        }

        SoundInstance sound =
                ENTITY_SOUNDS.get(entityId);

        if (sound == null
                || !Minecraft.getInstance()
                .getSoundManager()
                .isActive(sound)) {
            return -1;
        }

        return ENTITY_TRACKS.getOrDefault(entityId, -1);
    }

    /**
     * Advances playlists after each song and handles looping.
     *
     * Record Sleeve:
     * - Looping off: plays every track once, then stops.
     * - Looping on: restarts the playlist after its final track.
     *
     * Normal record:
     * - Looping off: plays once, then stops.
     * - Looping on: repeats the song.
     */
    public static void tick() {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level == null) {
            return;
        }

        for (int entityId
                : ENTITY_SOUNDS.keySet().toIntArray()) {

            SoundInstance sound =
                    ENTITY_SOUNDS.get(entityId);

            if (sound == null) {
                continue;
            }

            Entity entity =
                    minecraft.level.getEntity(entityId);

            if (entity == null) {
                stopMusicBox(entityId);
                continue;
            }

            ItemStack record =
                    ENTITY_RECORDS.get(entityId);

            if (record == null || record.isEmpty()) {
                stopMusicBox(entityId);
                continue;
            }

            ItemStack musicBox =
                    findMatchingMusicBox(entity, record);

            if (musicBox.isEmpty()
                    || musicBox.has(ZAMComponents.PAUSED)) {
                stopMusicBox(entityId);
                continue;
            }

            if (minecraft.getSoundManager()
                    .isActive(sound)) {
                continue;
            }

            int currentTrack =
                    ENTITY_TRACKS.getOrDefault(
                            entityId,
                            0
                    );

            int nextTrack = currentTrack + 1;
            int trackCount =
                    PlayableRecord.getTrackCount(record);

            if (nextTrack < trackCount) {
                playTrack(
                        entityId,
                        record,
                        nextTrack
                );

                continue;
            }

            if (musicBox.has(ZAMComponents.LOOPING)) {
                /*
                 * Restart the entire playlist. For a normal disc,
                 * track zero is also the only track, so this repeats
                 * the individual song.
                 */
                playTrack(entityId, record, 0);
            } else {
                stopMusicBox(entityId);
            }
        }
    }

    /**
     * Finds the Music Box currently holding this record or sleeve.
     */
    private static ItemStack findMatchingMusicBox(
            Entity entity,
            ItemStack record
    ) {
        if (!(entity instanceof Player player)) {
            return ItemStack.EMPTY;
        }

        ItemStack carried =
                player.inventoryMenu.getCarried();

        if (isMatchingMusicBox(carried, record)) {
            return carried;
        }

        for (InteractionHand hand
                : InteractionHand.values()) {

            ItemStack stack =
                    player.getItemInHand(hand);

            if (isMatchingMusicBox(stack, record)) {
                return stack;
            }
        }

        for (ItemStack stack
                : player.getInventory()
                .getNonEquipmentItems()) {

            if (isMatchingMusicBox(stack, record)) {
                return stack;
            }
        }

        return ItemStack.EMPTY;
    }

    private static boolean isMatchingMusicBox(
            ItemStack stack,
            ItemStack record
    ) {
        return stack.getItem() instanceof MusicBoxItem
                && MusicBoxItem.hasRecord(stack)
                && ItemStack.matches(
                MusicBoxItem.getRecord(stack),
                record
        );
    }
}