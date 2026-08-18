package net.ron.zam.common.block.television;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.ron.zam.api.projector.WebVideoManager;
import net.ron.zam.api.television.IntAnimationState;
import net.ron.zam.common.item.CassetteItem;
import net.ron.zam.registry.ZAMBlockEntities;
import net.ron.zam.registry.ZAMBlocks;
import org.jetbrains.annotations.Nullable;

public class TelevisionBlockEntity extends BlockEntity {
    private ItemStack cassette = ItemStack.EMPTY;
    private boolean playing = true;
    private int playbackTicks;

    public int currentFrame;
    public float crtPhase;
    private boolean needsConnectionRefresh = true;

    public final IntAnimationState fadeAnimation = new IntAnimationState(3, 9);

    public TelevisionBlockEntity(BlockPos pos, BlockState state) {
        super(ZAMBlockEntities.TELEVISION, pos, state);
    }

    public float screenAlpha(float partialTick) {
        return fadeAnimation.getValue(partialTick);
    }

    public ItemStack cassette() {
        return cassette;
    }

    public void setCassette(ItemStack stack) {
        cassette = stack;
        playbackTicks = 0;
        playing = !stack.isEmpty();
        changedAndSync();
    }

    void transferPlayback(ItemStack stack, boolean playing, int ticks) {
        cassette = stack.copy();
        this.playing = !stack.isEmpty() && playing;
        playbackTicks = Math.max(0, ticks);
        changedAndSync();
    }

    public boolean isPlaying() {
        return playing;
    }

    public void togglePlay() {
        if (cassette.isEmpty()) {
            playing = false;
            return;
        }

        playing = !playing;
        changedAndSync();
    }

    @Nullable
    public Identifier currentCassetteId() {
        return cassette.isEmpty() ? null : CassetteItem.getCassetteId(cassette);
    }

    public int playbackTicks() {
        return playbackTicks;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TelevisionBlockEntity be) {
        if (be.needsConnectionRefresh && !level.isClientSide()) {
            be.needsConnectionRefresh = false;
            TelevisionMultiblock.refresh(level, pos, state.getValue(TelevisionBlock.FACING));
        }

        if (be.playing && !be.cassette.isEmpty())
            be.playbackTicks++;

        if (level.isClientSide()) {
            be.crtPhase += 0.05F;
            if (be.crtPhase > Math.PI * 4.0F) be.crtPhase = 0.0F;

            boolean powered = TelevisionMultiblock.isAnyConnectedTvPowered(
                    level, pos, state.getValue(TelevisionBlock.FACING));

            if (powered)
                be.fadeAnimation.increment();
            else
                be.fadeAnimation.decrement();
        }
    }

    private void changedAndSync() {
        setChanged();

        if (level != null && !level.isClientSide())
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    @Override
    public void setRemoved() {
        if (level != null && level.isClientSide())
            WebVideoManager.stop(this);

        super.setRemoved();
    }

    @Override
    protected void loadAdditional(ValueInput in) {
        super.loadAdditional(in);
        cassette = in.read("cassette", ItemStack.CODEC).orElse(ItemStack.EMPTY);
        playing = in.getBooleanOr("playing", false);
        playbackTicks = in.getIntOr("ticks", 0);
    }

    @Override
    protected void saveAdditional(ValueOutput out) {
        super.saveAdditional(out);

        if (!cassette.isEmpty())
            out.store("cassette", ItemStack.CODEC, cassette);

        out.putBoolean("playing", playing);
        out.putInt("ticks", playbackTicks);
    }

    @Override
    public net.minecraft.nbt.CompoundTag getUpdateTag(HolderLookup.Provider lookup) {
        return saveCustomOnly(lookup);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}