package net.ron.zam.util.tooltips;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;

public class ClientRecordSleeveTooltip implements ClientTooltipComponent {
    private final List<ItemStack> items = new ArrayList<>();
    private final int selected;

    private static final int COLS = 4;
    private static final float SCALE = 1.0f;
    private static final int SLOT = 22;
    private static final int MAX = 8;

    private static final int TEXT_DX = 2;
    private static final int TEXT_X_NUDGE = -10;
    private static final int LINE_GAP = 0;
    private static final int GRID_X_SHIFT = 0;

    private static final int COLOR_GRAY = 0xFFAAAAAA;
    private static final int COLOR_WHITE = 0xFFFFFFFF;
    private static final int COLOR_SELECTED_FILL = 0x30FFFFFF;
    private static final int COLOR_SELECTED_BORDER = 0xCCFFFFFF;

    private static final Identifier SLOT_SPRITE =
            Identifier.withDefaultNamespace("container/bundle/slot_background");

    public ClientRecordSleeveTooltip(RecordSleeveTooltip data) {
        data.contents().items().forEach(template -> this.items.add(template.create()));
        this.selected = data.contents().getSelectedItemIndex();
    }

    @Override
    public int getWidth(Font font) {
        return COLS * SLOT;
    }

    @Override
    public int getHeight(Font font) {
        int itemCount = items.size();
        if (itemCount == 0) {
            int textBlockH = font.lineHeight * 3 + LINE_GAP * 2;
            return Math.max(SLOT, textBlockH + 2);
        }
        int rows = Math.max(1, (itemCount + COLS - 1) / COLS);
        return rows * SLOT;
    }

    @Override
    public boolean showTooltipWithItemInHand() {
        return true;
    }

    @Override
    public void extractImage(Font font, int x, int y, int width, int height, GuiGraphicsExtractor g) {
        int itemCount = items.size();

        if (itemCount == 0) {
            // same empty state text & layout you had
            String l1 = "Can hold a mix";
            String l2 = "of music discs";
            String l3 = "0 / " + MAX;

            int w = getWidth(font);
            int h = getHeight(font);
            int textBlockH = font.lineHeight * 3 + LINE_GAP * 2;

            int y0 = y + (h - textBlockH) / 2 - 1;
            int xLeft = x + (w - font.width(l1)) / 2 + TEXT_DX + TEXT_X_NUDGE;

            g.text(font, l1, xLeft, y0, COLOR_GRAY, false);
            g.text(font, l2, xLeft, y0 + font.lineHeight + LINE_GAP, COLOR_GRAY, false);
            g.text(font, l3, xLeft, y0 + (font.lineHeight + LINE_GAP) * 2 + 2, COLOR_WHITE, false);
            return;
        }

        int rows = Math.max(1, (itemCount + COLS - 1) / COLS);
        int topCols = itemCount % COLS;
        if (topCols == 0) topCols = COLS;

        for (int displayRow = 0; displayRow < rows; displayRow++) {
            int colsThisRow = (displayRow == 0) ? topCols : COLS;
            int startCol = COLS - colsThisRow;
            for (int c = 0; c < colsThisRow; c++) {
                int cellX = x + GRID_X_SHIFT + (startCol + c) * SLOT;
                int cellY = y + displayRow * SLOT;
                g.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_SPRITE, cellX, cellY, SLOT, SLOT);
            }
        }

        float iconSize = 16f * SCALE;
        float iconOffset = (SLOT - iconSize) / 2f;

        for (int i = 0; i < itemCount; i++) {
            int logicalRow = i / COLS;
            int displayRow = rows - 1 - logicalRow;
            int colInRow = i % COLS;

            int colsThisRow = (displayRow == 0) ? topCols : COLS;
            int startCol = COLS - colsThisRow;
            int displayCol = startCol + (colsThisRow - 1 - colInRow);

            int cellX = x + GRID_X_SHIFT + displayCol * SLOT;
            int cellY = y + displayRow * SLOT;

            if (selected >= 0 && i == selected) {
                g.fill(cellX, cellY, cellX + SLOT, cellY + SLOT, COLOR_SELECTED_FILL);
                int border = COLOR_SELECTED_BORDER;
                g.fill(cellX, cellY, cellX + SLOT, cellY + 1, border);
                g.fill(cellX, cellY + SLOT - 1, cellX + SLOT, cellY + SLOT, border);
                g.fill(cellX, cellY, cellX + 1, cellY + SLOT, border);
                g.fill(cellX + SLOT - 1, cellY, cellX + SLOT, cellY + SLOT, border);
            }

            int sx = Math.round(cellX + iconOffset);
            int sy = Math.round(cellY + iconOffset);

            var m = g.pose();
            m.pushMatrix();
            m.translate((float) sx, (float) sy);
            m.scale(SCALE, SCALE);
            g.item(items.get(i), 0, 0);
            g.itemDecorations(font, items.get(i), 0, 0);
            m.popMatrix();
        }

        if (this.selected >= 0 && this.selected < items.size()) {
            ItemStack sel = items.get(this.selected);
            Component line = getDiscDescriptionOrName(sel);

            int textW = font.width(line.getVisualOrderText());
            int centerX = x + getWidth(font) / 2;
            ClientTooltipComponent ctc = ClientTooltipComponent.create(line.getVisualOrderText());

            g.tooltip(
                    font,
                    List.of(ctc),
                    centerX - textW / 2,
                    y - 15,
                    DefaultTooltipPositioner.INSTANCE,
                    sel.get(DataComponents.TOOLTIP_STYLE)
            );
        }
    }

    private static Component getDiscDescriptionOrName(ItemStack stack) {
        if (stack.has(DataComponents.JUKEBOX_PLAYABLE)) {
            Minecraft mc = Minecraft.getInstance();

            Item.TooltipContext ctx =
                    mc.level != null
                            ? Item.TooltipContext.of(mc.level)
                            : Item.TooltipContext.EMPTY;

            TooltipFlag flag =
                    mc.options.advancedItemTooltips
                            ? TooltipFlag.ADVANCED
                            : TooltipFlag.NORMAL;

            List<Component> lines =
                    stack.getTooltipLines(ctx, mc.player, flag);

            Component discLine = null;

            if (lines.size() >= 3) {
                discLine = lines.get(2);
            } else if (lines.size() >= 2) {
                discLine = lines.get(1);
            }

            if (discLine != null && !discLine.getString().isEmpty()) {
                return discLine.copy().withStyle(stack.getRarity().color());
            }
        }

        return stack.getHoverName()
                .copy()
                .withStyle(stack.getRarity().color());
    }
}
