package net.ron.zam.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.ron.zam.common.item.RecordSleeveItem;
import net.ron.zam.util.RecordSleeveJukeboxExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(JukeboxBlockEntity.class)
public abstract class JukeboxBlockEntityMixin
        implements RecordSleeveJukeboxExtension {

    @Unique
    private static final String ZAM_WAS_PLAYING = "zam_was_playing";

    @Unique
    private static final String ZAM_SLEEVE_TRACK = "zam_sleeve_track";

    @Shadow
    public abstract ItemStack getTheItem();

    @Unique
    private int zam$recordSleeveTrack = -1;

    @Unique
    private boolean zam$recordSleevePlaying;

    /**
     * True after loading a jukebox that was playing when the
     * world was saved. The sound restarts during the next tick.
     */
    @Unique
    private boolean zam$restartPending;

    /**
     * Resets playlist state whenever the jukebox item changes.
     */
    @Inject(method = "setTheItem", at = @At("TAIL"))
    private void zam$prepareInsertedItem(ItemStack stack, CallbackInfo ci) {
        zam$recordSleeveTrack = -1;
        zam$restartPending = false;

        zam$recordSleevePlaying = stack.getItem() instanceof RecordSleeveItem && !zam$getSleeveDiscs(stack).isEmpty();
    }

    /**
     * Saves whether any record was playing, including normal
     * discs, modded discs, and Record Sleeves.
     */
    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void zam$savePlaybackState(ValueOutput output, CallbackInfo ci) {
        JukeboxBlockEntity jukebox = (JukeboxBlockEntity) (Object) this;

        ItemStack insertedItem = jukebox.getTheItem();

        boolean hasPlayableItem = insertedItem.has(DataComponents.JUKEBOX_PLAYABLE) || insertedItem.getItem() instanceof RecordSleeveItem;
        boolean wasPlaying = hasPlayableItem && (jukebox.getSongPlayer().isPlaying() || zam$recordSleevePlaying || zam$restartPending);
        output.putBoolean(ZAM_WAS_PLAYING, wasPlaying);
        output.putInt(ZAM_SLEEVE_TRACK, zam$recordSleeveTrack);
    }

    /**
     * Restores the saved state. Actual sound playback begins on
     * the first tick after loading, when the level is available.
     */
    @Inject(method = "loadAdditional", at = @At("TAIL"))
    private void zam$loadPlaybackState(ValueInput input, CallbackInfo ci) {
        zam$recordSleeveTrack = input.getIntOr(ZAM_SLEEVE_TRACK, -1);

        zam$restartPending = input.getBooleanOr(ZAM_WAS_PLAYING, false);

        ItemStack insertedItem = getTheItem();

        zam$recordSleevePlaying = insertedItem.getItem() instanceof RecordSleeveItem && zam$restartPending;
    }

    /**
     * Restarts saved songs and advances Record Sleeve playlists.
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private static void zam$tickPlayback(Level level, BlockPos pos, BlockState state, JukeboxBlockEntity jukebox, CallbackInfo ci) {
        if (!(jukebox
                instanceof RecordSleeveJukeboxExtension extension)) {
            return;
        }

        if (extension.zam$isRestartPending()) {
            extension.zam$setRestartPending(false);

            ItemStack insertedItem = jukebox.getTheItem();

            ItemStack trackToRestart = zam$getSavedTrack(insertedItem, extension);

            JukeboxPlayable playable = trackToRestart.get(DataComponents.JUKEBOX_PLAYABLE);

            if (playable != null) {jukebox.getSongPlayer().play(level, playable.song());

                jukebox.setChanged();
                return;
            }

            extension.zam$setRecordSleevePlaying(false);
            extension.zam$setRecordSleeveTrack(-1);
            jukebox.setChanged();

            return;
        }

        if (jukebox.getSongPlayer().isPlaying()) {
            return;
        }

        if (!extension.zam$isRecordSleevePlaying()) {
            return;
        }

        List<ItemStack> discs =
                zam$getSleeveDiscs(
                        jukebox.getTheItem()
                );

        int nextTrack =
                extension.zam$getRecordSleeveTrack() + 1;

        if (nextTrack >= discs.size()) {
            extension.zam$setRecordSleevePlaying(false);
            extension.zam$setRecordSleeveTrack(-1);

            jukebox.onSongChanged();
            jukebox.setChanged();

            return;
        }

        ItemStack nextDisc = discs.get(nextTrack);

        JukeboxPlayable playable =
                nextDisc.get(
                        DataComponents.JUKEBOX_PLAYABLE
                );

        if (playable == null) {
            extension.zam$setRecordSleeveTrack(
                    nextTrack
            );

            jukebox.setChanged();
            return;
        }

        extension.zam$setRecordSleeveTrack(nextTrack);

        jukebox.getSongPlayer().play(
                level,
                playable.song()
        );

        jukebox.setChanged();
    }

    /**
     * Determines which exact item should restart after loading.
     *
     * Normal and modded records use the jukebox item directly.
     * Record Sleeves use the saved playlist index.
     */
    @Unique
    private static ItemStack zam$getSavedTrack(ItemStack insertedItem, RecordSleeveJukeboxExtension extension) {
        if (!(insertedItem.getItem() instanceof RecordSleeveItem)) {

            return insertedItem.has(DataComponents.JUKEBOX_PLAYABLE) ? insertedItem : ItemStack.EMPTY;
        }

        List<ItemStack> discs = zam$getSleeveDiscs(insertedItem);

        if (discs.isEmpty()) {
            return ItemStack.EMPTY;
        }

        int savedTrack = extension.zam$getRecordSleeveTrack();

        /*
         * If the sleeve was saved immediately after insertion,
         * its index may still be -1. In that case, begin with its
         * first track.
         */
        if (savedTrack < 0 || savedTrack >= discs.size()) {
            savedTrack = 0;
        }

        extension.zam$setRecordSleeveTrack(savedTrack);
        extension.zam$setRecordSleevePlaying(true);

        return discs.get(savedTrack);
    }

    /**
     * Makes comparator output follow the active sleeve track.
     */
    @Inject(
            method = "getComparatorOutput",
            at = @At("RETURN"),
            cancellable = true
    )
    private void zam$recordSleeveComparator(
            CallbackInfoReturnable<Integer> cir
    ) {
        if (cir.getReturnValue() != 0
                || !zam$recordSleevePlaying) {
            return;
        }

        List<ItemStack> discs =
                zam$getSleeveDiscs(getTheItem());

        if (zam$recordSleeveTrack < 0
                || zam$recordSleeveTrack
                >= discs.size()) {
            return;
        }

        JukeboxPlayable playable = discs
                .get(zam$recordSleeveTrack)
                .get(DataComponents.JUKEBOX_PLAYABLE);

        if (playable != null) {
            cir.setReturnValue(
                    playable.song()
                            .value()
                            .comparatorOutput()
            );
        }
    }

    @Unique
    private static List<ItemStack> zam$getSleeveDiscs(ItemStack sleeve) {
        if (!(sleeve.getItem() instanceof RecordSleeveItem)) {
            return List.of();
        }

        BundleContents contents = sleeve.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);

        return contents.itemCopyStream().filter(stack -> stack.has(
                DataComponents.JUKEBOX_PLAYABLE)).limit(RecordSleeveItem.MAX_DISCS).toList();
    }

    @Override
    public int zam$getRecordSleeveTrack() {
        return zam$recordSleeveTrack;
    }

    @Override
    public void zam$setRecordSleeveTrack(int track) {
        zam$recordSleeveTrack = track;
    }

    @Override
    public boolean zam$isRecordSleevePlaying() {
        return zam$recordSleevePlaying;
    }

    @Override
    public void zam$setRecordSleevePlaying(boolean playing) {
        zam$recordSleevePlaying = playing;
    }

    @Override
    public boolean zam$isRestartPending() {
        return zam$restartPending;
    }

    @Override
    public void zam$setRestartPending(boolean pending) {
        zam$restartPending = pending;
    }
}