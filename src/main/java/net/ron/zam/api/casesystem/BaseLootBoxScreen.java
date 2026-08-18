package net.ron.zam.api.casesystem;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.ron.zam.ZAMMod;
import net.ron.zam.api.casesystem.cases.CaseEntry;
import net.ron.zam.api.rarity.Rarity;
import net.ron.zam.api.rarity.RarityItem;
import net.ron.zam.common.packet.ConsumeLootBoxItemsPacket;
import net.ron.zam.registry.ZAMItems;
import net.ron.zam.registry.ZAMSounds;
import net.ron.zam.util.tooltips.RowItemsClientTooltip;
import net.ron.zam.util.tooltips.RowItemsTooltip;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class BaseLootBoxScreen<T extends BaseLootBoxMenu<T>> extends AbstractContainerScreen<T> {

    private static final Identifier CHECKMARK_TEXTURE = ZAMMod.id("textures/gui/checkmark.png");
    private static final Pattern COLLECTED_COUNTS = Pattern.compile("(?i)^\\s*(?:✔\\s*)?collected:?\\s*(\\d+)\\s*/\\s*(\\d+)\\s*$");

    private final Identifier texture;
    private Button openButton;
    private boolean showMessage;
    private Component message;
    private final CaseEntry entry;
    private int messageTicks = 0;

    public BaseLootBoxScreen(T menu, Inventory playerInventory) {
        super(menu, playerInventory, menu.getCaseEntry().title(), 176, 184);

        this.entry = menu.getCaseEntry();
        this.texture = this.entry.texture();
        this.showMessage = false;
        this.message = Component.empty();
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
        this.inventoryLabelY = 10000;
        int buttonX = this.leftPos + (this.imageWidth / 2) - 50;
        int buttonY = this.topPos + 25;

        this.openButton = Button.builder(Component.literal("Open Case"), button -> {
            if (this.menu.hasRequiredItems(this.minecraft.player)) {
                ClientPlayNetworking.send(new ConsumeLootBoxItemsPacket());

                this.minecraft.level.playLocalSound(
                        this.minecraft.player.getX(),
                        this.minecraft.player.getY(),
                        this.minecraft.player.getZ(),
                        ZAMSounds.CASE_SPIN,
                        SoundSource.UI,
                        3.0f,
                        1.0f,
                        false
                );

                this.minecraft.gui.setScreen(
                        new BaseSpinScreen(
                                this.menu.getLootItems(),
                                this.entry.title(),
                                this.entry
                        )
                );
            } else {
                this.showMessage = true;
                this.message = Component.literal("You need a Case Key to open!");
                this.messageTicks = 80;
            }
        }).bounds(buttonX, buttonY, 100, 20).build();

        this.addRenderableWidget(this.openButton);
        populateSlotsWithItems();
    }

    private void populateSlotsWithItems() {
        List<ItemStack> items = this.menu.getItems();
        for (int i = 0; i < items.size() && i < this.menu.slots.size(); i++) {
            this.menu.slots.get(i).set(items.get(i));
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);

        int titleX = this.leftPos + (this.imageWidth / 2) - (this.font.width(this.title) / 2);
        guiGraphics.text(this.font, this.title, titleX, this.topPos + 6, 0x404040, false);

        if (showMessage && !message.getString().isEmpty()) {
            drawCenteredMessage(guiGraphics, message);
        }

        // Render pooled preview tooltip when applicable; otherwise fallback.
        if (!renderGoldTooltipWithPreview(guiGraphics, mouseX, mouseY)) {
            this.extractTooltip(guiGraphics, mouseX, mouseY);
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor gfx, int mouseX, int mouseY, float partialTicks) {
        this.extractTransparentBackground(gfx);
        gfx.blit(RenderPipelines.GUI_TEXTURED, texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        renderRarityBorders(gfx);
    }

    @Override
    protected void extractSlot(GuiGraphicsExtractor guiGraphics, Slot slot, int i, int j) {
        super.extractSlot(guiGraphics, slot, i, j);
        ItemStack itemstack = slot.getItem();
        if (itemstack.has(DataComponents.LORE)) {
            List<Component> loreLines = itemstack.get(DataComponents.LORE).lines();
            for (Component line : loreLines) {
                if (line.getString().contains("✔")) {
                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, CHECKMARK_TEXTURE, slot.x + 10, slot.y - 4, 0, 0, 8, 8, 8, 8);
                    break;
                }
            }
        }
    }

    private void drawCenteredMessage(GuiGraphicsExtractor guiGraphics, Component message) {
        if (messageTicks <= 0) return;

        int centerX = this.leftPos + (this.imageWidth / 2);
        int y = this.topPos + 47;

        int alpha = 255;
        if (messageTicks <= 20) {
            alpha = (int) ((messageTicks / 20.0f) * 255);
        }

        int color = (alpha << 24) | 0xFF0000;
        guiGraphics.centeredText(this.font, message, centerX, y, color);
    }

    private void renderRarityBorders(GuiGraphicsExtractor guiGraphics) {
        List<RarityItem> items = this.menu.getLootItems();
        for (int i = 0; i < items.size() && i < this.menu.slots.size(); i++) {
            int slotX = this.leftPos + this.menu.slots.get(i).x;
            int slotY = this.topPos + this.menu.slots.get(i).y;
            guiGraphics.fill(slotX, slotY, slotX + 16, slotY + 16, getRarityColor(items.get(i).getRarity()));
        }
    }

    // === BEGIN: tooltip preview additions mirrored from HH ===

    private List<Component> robustTooltipLines(ItemStack stack) {
        List<Component> lines = this.getTooltipFromItem(this.minecraft, stack);
        if (lines != null && !lines.isEmpty()) return lines;

        TooltipFlag flag = (this.minecraft != null && this.minecraft.options != null && this.minecraft.options.advancedItemTooltips)
                ? TooltipFlag.Default.ADVANCED
                : TooltipFlag.Default.NORMAL;

        Item.TooltipContext ctx = (this.minecraft != null && this.minecraft.level != null)
                ? Item.TooltipContext.of(this.minecraft.level)
                : Item.TooltipContext.EMPTY;

        return stack.getTooltipLines(ctx, (this.minecraft != null ? this.minecraft.player : null), flag);
    }

    private boolean isCollectedByTooltip(ItemStack stack) {
        for (Component c : robustTooltipLines(stack)) {
            String s = c.getString().toLowerCase(Locale.ROOT);
            if (s.contains("collected") || s.contains("✔") || s.contains("✓")) return true;
        }
        return false;
    }

    private boolean renderGoldTooltipWithPreview(GuiGraphicsExtractor gfx, int mouseX, int mouseY) {
        Slot hovered = this.hoveredSlot;
        if (hovered == null || !hovered.hasItem()) return false;

        int idx = this.menu.slots.indexOf(hovered);
        if (idx < 0) return false;

        List<RarityItem> loot = this.menu.getLootItems();
        if (idx >= loot.size()) return false;

        RarityItem ri = loot.get(idx);
        List<ItemStack> pool = ri.viewRewardPool();

        boolean isGoldIcon = hovered.getItem().is(ZAMItems.GOLD_ICON);
        boolean isRedIcon  = hovered.getItem().is(ZAMItems.RED_ICON);
        boolean isMystery  = pool.size() > 1;
        boolean isUltra    = ri.getRarity() == Rarity.ULTRA_RARE;

        if (!isUltra && !isGoldIcon && !isRedIcon && !isMystery) return false;

        // Ensure single-reward entries still have something to preview
        if (pool.isEmpty()) {
            ItemStack single = ri.getSingleReward();
            if (single != null && !single.isEmpty()) {
                pool = List.of(single.copy());
            }
        }

        ItemStack stack = hovered.getItem();
        List<Component> base = robustTooltipLines(stack);
        int totalFromPool = Math.max(1, pool.size());

        List<ClientTooltipComponent> out = new ArrayList<>(base.size() + 3);
        int insertCollectedAt = -1;
        Integer have = null, total = null;
        boolean sawReceive = false, sawPool = false;

        for (Component c : base) {
            String raw = c.getString();
            String lower = raw.toLowerCase(Locale.ROOT).replace("✔", "").replace("✓", "").trim();

            if (lower.startsWith("receive ") || lower.startsWith("pull to receive")) {
                sawReceive = true;
            } else if (lower.startsWith("pool:")) {
                sawPool = true;
                out.add(ClientTooltipComponent.create(c.getVisualOrderText()));
                insertCollectedAt = out.size();
                continue;
            } else {
                var m = COLLECTED_COUNTS.matcher(raw);
                if (m.find()) {
                    try {
                        have = Integer.parseInt(m.group(1));
                        total = Integer.parseInt(m.group(2));
                    } catch (NumberFormatException ignored) {}
                    continue;
                }
                if (lower.startsWith("collected")) continue;
            }
            out.add(ClientTooltipComponent.create(c.getVisualOrderText()));
        }

        if (!sawReceive) {
            Component line = Component.literal("Pick ")
                    .append(Component.literal("One").withStyle(net.minecraft.ChatFormatting.YELLOW))
                    .append(Component.literal(" Cosmetic").withStyle(net.minecraft.ChatFormatting.GRAY));
            out.add(ClientTooltipComponent.create(line.getVisualOrderText()));
        }

        if (!sawPool) {
            Component line = Component.literal("Pool: ")
                    .append(Component.literal(String.valueOf(totalFromPool)).withStyle(net.minecraft.ChatFormatting.YELLOW))
                    .append(Component.literal(totalFromPool == 1 ? " item" : " items").withStyle(net.minecraft.ChatFormatting.GRAY));
            out.add(ClientTooltipComponent.create(line.getVisualOrderText()));
            insertCollectedAt = out.size();
        }

        int haveVal  = (have  != null) ? have  : 0;
        int totalVal = (total != null) ? total : totalFromPool;

        // Fallbacks so single-gold always shows a count
        if (have == null) {
            if (totalFromPool == 1) {
                boolean itemCollected = isCollectedByTooltip(pool.get(0));
                boolean iconCollected = isCollectedByTooltip(stack);
                haveVal = (itemCollected || iconCollected) ? 1 : 0;
            } else {
                int inferred = 0;
                for (ItemStack s : pool) if (isCollectedByTooltip(s)) inferred++;
                haveVal = inferred;
            }
        }

        if (haveVal > 0 && totalVal > 0) {
            Component green = Component.literal("✔ Collected: " + haveVal + "/" + totalVal)
                    .withStyle(net.minecraft.ChatFormatting.GREEN);
            var collectedComp = ClientTooltipComponent.create(green.getVisualOrderText());
            if (insertCollectedAt >= 0 && insertCollectedAt <= out.size()) out.add(insertCollectedAt, collectedComp);
            else out.add(collectedComp);
        }

        int cap = Math.min(5, pool.size());
        if (cap > 0) {
            List<ItemStack> row = new ArrayList<>(cap);
            List<Boolean> flags = new ArrayList<>(cap);
            for (int i = 0; i < cap; i++) {
                ItemStack s = pool.get(i).copy();
                row.add(s);
                flags.add(isCollectedByTooltip(s));
            }
            out.add(new RowItemsClientTooltip(new RowItemsTooltip(row, flags)));
        }

        gfx.tooltip(this.font, out, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);

        return true;
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (this.messageTicks > 0) {
            this.messageTicks--;
            if (this.messageTicks == 0) {
                this.showMessage = false;
            }
        }
    }

    private int getRarityColor(Rarity rarity) {
        return rarity.getColor();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}