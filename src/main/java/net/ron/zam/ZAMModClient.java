package net.ron.zam;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.ClientTooltipComponentCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.ron.zam.api.musicbox.SoundTracker;
import net.ron.zam.api.projector.WebVideoManager;
import net.ron.zam.common.client.block.MarinersFortuneChestRenderer;
import net.ron.zam.common.client.block.RecordRackBlockEntityRenderer;
import net.ron.zam.common.client.block.television.TelevisionRenderer;
import net.ron.zam.common.gui.cases.CaseScreen;
import net.ron.zam.common.gui.record_rack.RecordRackScreen;
import net.ron.zam.registry.ZAMBlockEntities;
import net.ron.zam.registry.ZAMMenuTypes;
import net.ron.zam.registry.ZAMRenderPipelines;
import net.ron.zam.util.tooltips.ClientMusicBoxTooltip;
import net.ron.zam.util.tooltips.ClientRecordSleeveTooltip;
import net.ron.zam.util.tooltips.MusicBoxTooltip;
import net.ron.zam.util.tooltips.RecordSleeveTooltip;

public class ZAMModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ZAMRenderPipelines.register();

        registerMenuScreens();
        registerBlockEntityRenderers();

        ClientTooltipComponentCallback.EVENT.register(data -> {
            if (data instanceof RecordSleeveTooltip sleeve)
                return new ClientRecordSleeveTooltip(sleeve);

            if (data instanceof MusicBoxTooltip musicBox)
                return new ClientMusicBoxTooltip(musicBox);

            return null;
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) ->
                WebVideoManager.clear());

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            SoundTracker.tick();
            WebVideoManager.cleanup();
        });
    }

    private void registerMenuScreens() {
        MenuScreens.register(ZAMMenuTypes.CASE, CaseScreen::new);
        MenuScreens.register(ZAMMenuTypes.RECORD_RACK, RecordRackScreen::new);
    }

    private void registerBlockEntityRenderers() {
        BlockEntityRenderers.register(ZAMBlockEntities.RECORD_RACK, RecordRackBlockEntityRenderer::new);
        BlockEntityRenderers.register(ZAMBlockEntities.MARINERS_FORTUNE_CHEST, MarinersFortuneChestRenderer::new);
        BlockEntityRenderers.register(ZAMBlockEntities.TELEVISION, TelevisionRenderer::new);
    }
}