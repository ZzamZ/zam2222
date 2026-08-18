package net.ron.zam.common.gui.record_rack;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.ron.zam.ZAMMod;

public class RecordRackScreen extends AbstractContainerScreen<RecordRackMenu> {

    private static final Identifier TEX = ZAMMod.id("textures/gui/record_rack.png");

    public RecordRackScreen(RecordRackMenu menu, Inventory inv, Component title) {
        super(menu, inv, title, 176, 132);
        this.inventoryLabelY = 40;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor gui, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(gui, mouseX, mouseY, partialTick);
        this.extractTooltip(gui, mouseX, mouseY);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor gui, int mouseX, int mouseY, float partialTick) {
        this.extractTransparentBackground(gui);
        gui.blit(RenderPipelines.GUI_TEXTURED, TEX, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}