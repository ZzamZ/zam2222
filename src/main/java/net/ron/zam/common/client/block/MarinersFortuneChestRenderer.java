package net.ron.zam.common.client.block;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.Vec3;
import net.ron.zam.ZAMMod;
import net.ron.zam.common.block.fortune.MarinersFortuneChestBlockEntity;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class MarinersFortuneChestRenderer implements BlockEntityRenderer<@NotNull MarinersFortuneChestBlockEntity, @NotNull ChestRenderState> {

    private final SpriteGetter materials;
    private final ChestModel singleModel;
    private final ChestModel doubleLeftModel;
    private final ChestModel doubleRightModel;

    private static final SpriteId SINGLE = Sheets.CHEST_MAPPER.apply(Identifier.fromNamespaceAndPath(ZAMMod.MOD_ID, "mariners_fortune"));
    private static final SpriteId LEFT = Sheets.CHEST_MAPPER.apply(Identifier.fromNamespaceAndPath(ZAMMod.MOD_ID, "mariners_fortune_left"));
    private static final SpriteId RIGHT = Sheets.CHEST_MAPPER.apply(Identifier.fromNamespaceAndPath(ZAMMod.MOD_ID, "mariners_fortune_right"));

    public MarinersFortuneChestRenderer(BlockEntityRendererProvider.Context context) {
        this.materials = context.sprites();
        this.singleModel = new ChestModel(context.bakeLayer(ModelLayers.CHEST));
        this.doubleLeftModel = new ChestModel(context.bakeLayer(ModelLayers.DOUBLE_CHEST_LEFT));
        this.doubleRightModel = new ChestModel(context.bakeLayer(ModelLayers.DOUBLE_CHEST_RIGHT));
    }

    @Override
    public ChestRenderState createRenderState() {
        return new ChestRenderState();
    }

    @Override
    public void extractRenderState(MarinersFortuneChestBlockEntity blockEntity, ChestRenderState state, float tickDelta, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay overlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickDelta, cameraPos, overlay);

        BlockState blockState = blockEntity.getBlockState();
        state.type = blockState.getValue(ChestBlock.TYPE);
        state.facing = blockState.getValue(ChestBlock.FACING);
        state.material = ChestRenderState.ChestMaterialType.REGULAR;

        DoubleBlockCombiner.NeighborCombineResult<? extends @NotNull ChestBlockEntity> result;
        if (blockState.getBlock() instanceof ChestBlock chest) {
            assert blockEntity.getLevel() != null;
            result = chest.combine(blockState, blockEntity.getLevel(), blockEntity.getBlockPos(), true);
        } else {
            result = DoubleBlockCombiner.Combiner::acceptNone;
        }

        state.open = result.apply(ChestBlock.opennessCombiner(blockEntity)).get(tickDelta);

        if (state.type != ChestType.SINGLE) {
            state.lightCoords = ((Int2IntFunction) result.apply(new BrightnessCombiner())).applyAsInt(state.lightCoords);
        }
    }

    @Override
    public void submit(ChestRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(ChestRenderer.modelTransformation(state.facing));

        float open = state.open;
        open = 1.0F - open;
        open = 1.0F - open * open * open;

        SpriteId sprite;
        if (state.type == ChestType.LEFT) {
            sprite = LEFT;
        } else if (state.type == ChestType.RIGHT) {
            sprite = RIGHT;
        } else {
            sprite = SINGLE;
        }

        ChestModel model;
        if (state.type == ChestType.LEFT) {
            model = doubleLeftModel;
        } else if (state.type == ChestType.RIGHT) {
            model = doubleRightModel;
        } else {
            model = singleModel;
        }

        collector.submitModel(model, open, poseStack, state.lightCoords, OverlayTexture.NO_OVERLAY,
                -1, sprite, this.materials, 0, state.breakProgress);

        poseStack.popPose();
    }
}
