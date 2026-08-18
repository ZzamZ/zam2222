package net.ron.zam.util.tooltips;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.ron.zam.ZAMMod;

import java.util.List;

public final class RowItemsClientTooltip implements ClientTooltipComponent {
    private static final int PAD  = 2;
    private static final int SLOT = 18;
    private static final Identifier CHECKMARK_TEX = ZAMMod.id("textures/gui/checkmark.png");

    private final List<ItemStack> items;
    private final List<Boolean> flags; // per-item collected flags

    public RowItemsClientTooltip(RowItemsTooltip data) {
        List<ItemStack> inItems = data.items();
        List<Boolean> inFlags   = data.collectedFlags();
        // hard-cap to 3 for safety
        this.items = inItems.size() > 5 ? inItems.subList(0, 5) : inItems;
        this.flags = inFlags.size() > 5 ? inFlags.subList(0, 5) : inFlags;
    }

    @Override public int getHeight(Font font) { return items.isEmpty() ? 0 : (SLOT + PAD * 2); }
    @Override public int getWidth (Font font) { return items.isEmpty() ? 0 : (items.size() * SLOT + PAD * 2); }

    @Override
    public void extractImage(Font font, int x, int y, int tooltipZ, int packedLight, GuiGraphicsExtractor g) {
        for (int i = 0; i < items.size(); i++) {
            int ix = x + PAD + i * SLOT;
            int iy = y + PAD;
            ItemStack s = items.get(i);

            g.item(s, ix, iy);
            g.itemDecorations(Minecraft.getInstance().font, s, ix, iy);

            boolean isCollected = (i < flags.size()) && Boolean.TRUE.equals(flags.get(i));
            if (isCollected) {
                g.blit(RenderPipelines.GUI_TEXTURED, CHECKMARK_TEX, ix + 10, iy - 4, 0, 0, 8, 8, 8, 8);
            }
        }
    }
}
