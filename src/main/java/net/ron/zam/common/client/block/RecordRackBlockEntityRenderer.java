package net.ron.zam.common.client.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.Vec3;
import net.ron.zam.common.block.record_rack.RecordRackBlockEntity;
import org.jetbrains.annotations.Nullable;

public class RecordRackBlockEntityRenderer implements BlockEntityRenderer<RecordRackBlockEntity, RecordRackBlockEntityRenderer.RenderState> {

    private final ItemModelResolver itemModelResolver;

    /**
     * Peg positions in MODEL ORDER.
     * Slot index i renders at PEG_Z_OFFSETS[i].
     * One-to-one. No remapping.
     */
    private static final double[] PEG_Z_OFFSETS = {
            14.0 / 16.0,
            12.0 / 16.0,
            10.0 / 16.0,
            8.0  / 16.0,
            6.0  / 16.0,
            4.0  / 16.0,
            2.0  / 16.0
    };

    public RecordRackBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }


    public static class RenderState extends BlockEntityRenderState {
        public final ItemStack[] stacks = new ItemStack[RecordRackBlockEntity.SIZE];
        public final ItemStackRenderState[] itemStates = new ItemStackRenderState[RecordRackBlockEntity.SIZE];

        public Level level;
        public BlockPos pos;
        public Direction facing;

        public RenderState() {
            for (int i = 0; i < RecordRackBlockEntity.SIZE; i++) {
                stacks[i] = ItemStack.EMPTY;
                itemStates[i] = new ItemStackRenderState();
            }
        }
    }

    // ---------------------------------------------------------------------

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(
            RecordRackBlockEntity rack,
            RenderState state,
            float tick,
            Vec3 camPos,
            @Nullable ModelFeatureRenderer.CrumblingOverlay overlay
    ) {
        BlockEntityRenderer.super.extractRenderState(rack, state, tick, camPos, overlay);

        state.level = rack.getLevel();
        state.pos = rack.getBlockPos();
        state.facing = rack.getBlockState().getValue(HorizontalDirectionalBlock.FACING);

        for (int i = 0; i < RecordRackBlockEntity.SIZE; i++) {
            state.stacks[i] = ItemStack.EMPTY;
            state.itemStates[i].clear();

            ItemStack stack = rack.getItem(i);
            if (stack.isEmpty()) continue;

            ItemStack copy = stack.copy();
            state.stacks[i] = copy;

            itemModelResolver.updateForTopItem(
                    state.itemStates[i],
                    copy,
                    ItemDisplayContext.FIXED,
                    state.level,
                    null,
                    0
            );
        }
    }

    @Override
    public void submit(RenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.level == null || state.pos == null || state.facing == null) return;

        int light = getPackedLight(state.level, state.pos);

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.facing.toYRot()));
        poseStack.translate(-0.5, 0.0, -0.5);

        for (int slot = 0; slot < RecordRackBlockEntity.SIZE; slot++)
        {ItemStack stack = state.stacks[slot];
            if (stack.isEmpty()) continue;

            poseStack.pushPose();
            poseStack.translate(0.48, 0.2, PEG_Z_OFFSETS[slot]);
            poseStack.scale(0.6f, 0.6f, 1.0f);
            state.itemStates[slot].submit(poseStack, collector, light, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        poseStack.popPose();
    }


    private static int getPackedLight(Level level, BlockPos pos) {
        int block = level.getBrightness(LightLayer.BLOCK, pos);
        int sky = level.getBrightness(LightLayer.SKY, pos);
        return LightCoordsUtil.pack(block, sky);
    }
}
