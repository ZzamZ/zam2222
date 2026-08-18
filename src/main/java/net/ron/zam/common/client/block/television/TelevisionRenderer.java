package net.ron.zam.common.client.block.television;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.ron.zam.ZAMMod;
import net.ron.zam.api.cassette.AnimatedPngLoader;
import net.ron.zam.api.cassette.GifLoader;
import net.ron.zam.api.cassette.McmetaAnimationLoader;
import net.ron.zam.api.projector.WebVideoManager;
import net.ron.zam.common.block.television.TelevisionBlock;
import net.ron.zam.common.block.television.TelevisionBlockEntity;
import net.ron.zam.common.block.television.TelevisionMultiblock;
import net.ron.zam.common.component.VideoMediaComponent;
import net.ron.zam.common.data.CassetteData;
import net.ron.zam.common.item.VideoTapeItem;
import net.ron.zam.registry.ZAMItems;
import org.jetbrains.annotations.Nullable;

public class TelevisionRenderer implements BlockEntityRenderer<TelevisionBlockEntity, TelevisionRenderState> {
    private static final float HALF_W = 6.0F / 16.0F, HALF_H = 5.5F / 16.0F, Y_BIAS = 0.5F / 16.0F, FACE_OUT = 0.501F;
    private static final Identifier DEFAULT_SCREEN = ZAMMod.id("textures/cassette_tape/color_bars_static.png");
    private static final Identifier VIDEO_LOADING = ZAMMod.id("textures/gui/television/broadcast_loading.png");
    private static final Identifier VIDEO_PAUSED = ZAMMod.id("textures/gui/television/broadcast_paused.png");

    public TelevisionRenderer(BlockEntityRendererProvider.Context ctx) {}

    @Override
    public TelevisionRenderState createRenderState() {
        return new TelevisionRenderState();
    }

    @Override
    public void extractRenderState(TelevisionBlockEntity be, TelevisionRenderState state, float partialTick,
                                   Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay overlay) {
        BlockEntityRenderState.extractBase(be, state, overlay);

        state.facing = be.getBlockState().getValue(TelevisionBlock.FACING);
        state.connection = be.getBlockState().getValue(TelevisionBlock.CONNECTION);
        state.screenAlpha = be.screenAlpha(partialTick);
        state.masterPlaybackTicks = resolveMasterPlaybackTicks(be);
        state.crtPhase = be.crtPhase + partialTick * 0.05F;
        state.videoOverlay = TelevisionRenderState.VideoOverlay.NONE;

        updateVideoPlayback(be);
        state.cassetteTexture = resolveActiveFrame(be, state);

        if (be.getLevel() != null) {
            TelevisionMultiblock.GridSlot slot =
                    TelevisionMultiblock.getGridSlot(be.getLevel(), be.getBlockPos(), state.facing);

            state.gridCol = slot.col();
            state.gridRow = slot.row();
            state.gridCols = slot.totalCols();
            state.gridRows = slot.totalRows();
        } else {
            state.gridCol = state.gridRow = 0;
            state.gridCols = state.gridRows = 1;
        }
    }

    private static int resolveMasterPlaybackTicks(TelevisionBlockEntity be) {
        if (be.getLevel() == null) return 0;

        TelevisionMultiblock.MasterTV master = TelevisionMultiblock.findMaster(
                be.getLevel(), be.getBlockPos(),
                be.getBlockState().getValue(TelevisionBlock.FACING)
        );

        return master != null ? master.tv().playbackTicks() : 0;
    }

    private static void updateVideoPlayback(TelevisionBlockEntity be) {
        if (be.getLevel() == null) return;

        TelevisionMultiblock.MasterTV master = TelevisionMultiblock.findMaster(
                be.getLevel(), be.getBlockPos(),
                be.getBlockState().getValue(TelevisionBlock.FACING)
        );

        if (master != null && !master.tv().cassette().is(ZAMItems.VIDEO_TAPE))
            WebVideoManager.stop(master.tv());
    }

    @Override
    public void submit(TelevisionRenderState state, PoseStack pose,
                       SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.cassetteTexture == null || state.screenAlpha <= 0.0F) return;

        submitScreen(
                state,
                pose,
                collector,
                state.cassetteTexture,
                Math.clamp(state.screenAlpha, 0.0F, 1.0F),
                false
        );

        if (state.videoOverlay == TelevisionRenderState.VideoOverlay.PAUSED)
            submitScreen(state, pose, collector, VIDEO_PAUSED, 1.0F, true);
    }

    private static void submitScreen(TelevisionRenderState state, PoseStack pose,
                                     SubmitNodeCollector collector, Identifier texture,
                                     float alpha, boolean overlay) {
        Direction facing = state.facing;
        float fx = facing.getStepX(), fz = facing.getStepZ();
        float offset = overlay ? 0.002F : 0.0F;

        float cx = 0.5F + fx * (FACE_OUT + offset);
        float cy = 0.5F + Y_BIAS;
        float cz = 0.5F + fz * (FACE_OUT + offset);
        float xUnit = fz, zUnit = -fx, nx = -fx, nz = -fz;

        float left = state.gridCol > 0 ? 0.5F : HALF_W;
        float right = state.gridCol < state.gridCols - 1 ? 0.5F : HALF_W;
        float top = state.gridRow > 0 ? 0.5F - Y_BIAS : HALF_H;
        float bottom = state.gridRow < state.gridRows - 1 ? 0.5F + Y_BIAS : HALF_H;

        float cols = Math.max(1, state.gridCols), rows = Math.max(1, state.gridRows);
        float u0 = state.gridCol / cols, u1 = (state.gridCol + 1) / cols;
        float v0 = state.gridRow / rows, v1 = (state.gridRow + 1) / rows;

        if (!overlay) {
            McmetaAnimationLoader.Animation anim = McmetaAnimationLoader.getOrLoad(texture);

            if (anim.isAnimated()) {
                float[] range = anim.vRangeFor(state.masterPlaybackTicks);
                float a0 = range[0], a1 = range[1];

                v0 = a0 + v0 * (a1 - a0);
                v1 = a0 + v1 * (a1 - a0);
            }
        }

        float finalV0 = v0, finalV1 = v1;
        int color = (Math.round(alpha * 255.0F) << 24) | 0x00FFFFFF;
        int light = net.minecraft.util.LightCoordsUtil.FULL_BRIGHT;

        RenderType renderType = overlay
                ? RenderTypes.entityTranslucent(texture)
                : TelevisionRenderTypes.zamCrt(texture);

        collector.submitCustomGeometry(pose, renderType, (entry, buf) -> {
            buf.addVertex(entry, cx - xUnit * left, cy + top, cz - zUnit * left)
                    .setColor(color).setUv(u0, finalV0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(entry, nx, 0, nz);
            buf.addVertex(entry, cx - xUnit * left, cy - bottom, cz - zUnit * left)
                    .setColor(color).setUv(u0, finalV1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(entry, nx, 0, nz);
            buf.addVertex(entry, cx + xUnit * right, cy - bottom, cz + zUnit * right)
                    .setColor(color).setUv(u1, finalV1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(entry, nx, 0, nz);
            buf.addVertex(entry, cx + xUnit * right, cy + top, cz + zUnit * right)
                    .setColor(color).setUv(u1, finalV0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(entry, nx, 0, nz);
        });
    }

    @Nullable
    private static Identifier resolveActiveFrame(TelevisionBlockEntity be, TelevisionRenderState state) {
        if (be.getLevel() == null) return null;

        Direction facing = be.getBlockState().getValue(TelevisionBlock.FACING);
        TelevisionMultiblock.MasterTV master =
                TelevisionMultiblock.findMaster(be.getLevel(), be.getBlockPos(), facing);

        if (master == null || master.tv().cassette().isEmpty())
            return DEFAULT_SCREEN;

        ItemStack cassette = master.tv().cassette();

        if (cassette.is(ZAMItems.VIDEO_TAPE)) {
            VideoMediaComponent media = VideoTapeItem.getMedia(cassette);

            if (media == null || media.url().isBlank()) {
                state.videoOverlay = TelevisionRenderState.VideoOverlay.ERROR;
                return DEFAULT_SCREEN;
            }

            WebVideoManager.MediaState mediaState = WebVideoManager.state(master.tv(), media);

            boolean powered = TelevisionMultiblock.isAnyConnectedTvPowered(
                    be.getLevel(),
                    master.pos(),
                    facing
            );

            state.videoOverlay = switch (mediaState) {
                case LOADING -> TelevisionRenderState.VideoOverlay.LOADING;
                case PAUSED -> powered && !master.tv().isPlaying()
                        ? TelevisionRenderState.VideoOverlay.PAUSED
                        : TelevisionRenderState.VideoOverlay.NONE;
                case FAILED -> TelevisionRenderState.VideoOverlay.ERROR;
                case PLAYING, ENDED -> TelevisionRenderState.VideoOverlay.NONE;
            };

            Identifier texture = WebVideoManager.texture(master.tv(), media);

            if (texture != null)
                return texture;

            return mediaState == WebVideoManager.MediaState.LOADING
                    ? VIDEO_LOADING
                    : DEFAULT_SCREEN;
        }

        Identifier cassetteId = master.tv().currentCassetteId();
        if (cassetteId == null) return DEFAULT_SCREEN;

        CassetteData data = ZAMMod.CASSETTES.get(cassetteId);
        if (data == null) return DEFAULT_SCREEN;

        int playbackTicks = master.tv().playbackTicks();

        Identifier gifLookup = Identifier.fromNamespaceAndPath(
                data.assetId().getNamespace(),
                "cassette_tape/" + data.assetId().getPath()
        );

        GifLoader.Loaded gif = GifLoader.getOrLoad(gifLookup);
        if (gif != null && !gif.isEmpty())
            return GifLoader.frameAt(gif, playbackTicks);

        Identifier framePath = data.framePath();
        AnimatedPngLoader.Loaded animated = AnimatedPngLoader.getOrLoad(framePath);

        return animated != null && !animated.isEmpty()
                ? AnimatedPngLoader.frameAt(animated, playbackTicks)
                : framePath;
    }
}