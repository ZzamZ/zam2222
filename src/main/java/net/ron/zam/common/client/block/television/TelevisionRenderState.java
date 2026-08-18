package net.ron.zam.common.client.block.television;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.ron.zam.common.block.television.TelevisionBlock;
import org.jetbrains.annotations.Nullable;

public class TelevisionRenderState extends BlockEntityRenderState {
    @Nullable
    public Identifier cassetteTexture;

    public Direction facing = Direction.NORTH;
    public TelevisionBlock.Connection connection = TelevisionBlock.Connection.SINGLE;

    public float crtPhase;
    public int masterPlaybackTicks;
    public float screenAlpha;

    public int gridCol = 0;
    public int gridRow = 0;
    public int gridCols = 1;
    public int gridRows = 1;

    public VideoOverlay videoOverlay = VideoOverlay.NONE;

    public enum VideoOverlay {
        NONE,
        LOADING,
        PAUSED,
        ERROR
    }
}