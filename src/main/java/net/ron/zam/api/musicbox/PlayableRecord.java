package net.ron.zam.api.musicbox;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.component.BundleContents;
import net.ron.zam.common.item.RecordSleeveItem;
import net.ron.zam.registry.ZAMComponents;

import java.util.List;
import java.util.Optional;

public final class PlayableRecord {

    private PlayableRecord() {
    }

    public static boolean isPlayableRecord(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        if (stack.getItem() instanceof RecordSleeveItem) {
            return getTrackCount(stack) > 0;
        }

        return stack.has(ZAMComponents.MUSIC)
                || stack.has(DataComponents.JUKEBOX_PLAYABLE);
    }

    /**
     * Returns the number of playable tracks contained by an item.
     *
     * Normal records contain one track. Record sleeves contain every
     * jukebox-playable disc stored in their bundle contents.
     */
    public static int getTrackCount(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }

        if (stack.getItem() instanceof RecordSleeveItem) {
            return getSleeveDiscs(stack).size();
        }

        return JukeboxSong.fromStack(stack).isPresent() ? 1 : 0;
    }

    /**
     * Returns the disc corresponding to a playlist track.
     */
    public static ItemStack getTrack(ItemStack stack, int track) {
        if (stack.isEmpty() || track < 0) {
            return ItemStack.EMPTY;
        }

        if (stack.getItem() instanceof RecordSleeveItem) {
            List<ItemStack> discs = getSleeveDiscs(stack);

            if (track >= discs.size()) {
                return ItemStack.EMPTY;
            }

            return discs.get(track);
        }

        if (track == 0 && JukeboxSong.fromStack(stack).isPresent()) {
            return stack;
        }

        return ItemStack.EMPTY;
    }

    private static List<ItemStack> getSleeveDiscs(ItemStack sleeve) {
        BundleContents contents = sleeve.getOrDefault(
                DataComponents.BUNDLE_CONTENTS,
                BundleContents.EMPTY
        );

        return contents.itemCopyStream()
                .filter(item ->
                        item.has(DataComponents.JUKEBOX_PLAYABLE))
                .limit(RecordSleeveItem.MAX_DISCS)
                .toList();
    }

    /**
     * Checks whether the local player is close enough to receive
     * the "Now Playing" text.
     */
    @Environment(EnvType.CLIENT)
    public static boolean canShowMessage(
            double x,
            double y,
            double z
    ) {
        LocalPlayer player = Minecraft.getInstance().player;

        return player == null
                || player.distanceToSqr(x, y, z) <= 4096.0;
    }

    @Environment(EnvType.CLIENT)
    public static Optional<SoundInstance> createPositionalSound(
            ItemStack stack,
            double x,
            double y,
            double z,
            int attenuationDistance
    ) {
        ItemStack trackStack = getTrack(stack, 0);

        if (trackStack.isEmpty()) {
            return Optional.empty();
        }

        Optional<Holder<JukeboxSong>> maybeSong =
                JukeboxSong.fromStack(trackStack);

        if (maybeSong.isEmpty()) {
            return Optional.empty();
        }

        JukeboxSong song = maybeSong.get().value();

        if (canShowMessage(x, y, z)) {
            Minecraft.getInstance().gui.hud.setNowPlaying(
                    song.description()
            );
        }

        return Optional.of(
                new PositionalRecordSoundInstance(
                        song.soundEvent().value(),
                        x,
                        y,
                        z,
                        attenuationDistance
                )
        );
    }

    /**
     * Creates the sound for one track. For a normal record, only
     * track zero exists. For a record sleeve, each stored disc is
     * addressed by its playlist index.
     */
    @Environment(EnvType.CLIENT)
    public static Optional<SoundInstance> createEntitySound(
            ItemStack stack,
            Entity entity,
            int track,
            int attenuationDistance
    ) {
        ItemStack trackStack = getTrack(stack, track);

        if (trackStack.isEmpty()) {
            return Optional.empty();
        }

        Optional<Holder<JukeboxSong>> maybeSong =
                JukeboxSong.fromStack(trackStack);

        if (maybeSong.isEmpty()) {
            return Optional.empty();
        }

        JukeboxSong song = maybeSong.get().value();

        if (entity.level()
                .getBlockState(entity.blockPosition().above())
                .isAir()
                && canShowMessage(
                entity.getX(),
                entity.getY(),
                entity.getZ()
        )) {

            Minecraft.getInstance().gui.hud.setNowPlaying(
                    song.description()
            );
        }

        return Optional.of(
                new EntityRecordSoundInstance(
                        song.soundEvent().value(),
                        entity
                )
        );
    }
}