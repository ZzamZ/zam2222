package net.ron.zam.api.casesystem;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxSong;
import net.ron.zam.ZAMMod;
import net.ron.zam.api.casesystem.cases.CaseEntry;
import net.ron.zam.api.rarity.Rarity;
import net.ron.zam.api.rarity.RarityItem;
import net.ron.zam.util.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class BaseSpinScreen extends Screen {
    private final Identifier texture;
    private final List<RarityItem> items;
    private final List<RarityItem> displayedItems = new ArrayList<>();
    private final Random random = new Random();
    private final Component caseTitle;
    private final CaseEntry entry;

    private int tickCounter = 0;
    private float itemScrollPosition = 0;
    private float speed = 15.0f;
    private boolean isSlowingDown = false;
    private long stopTime;
    private boolean rewardSelected = false;
    private Player player;
    private RarityItem selectedItem;

    public BaseSpinScreen(List<RarityItem> lootItems, Component caseTitle, CaseEntry entry) {
        super(Component.literal("Spinning..."));
        this.texture = ZAMMod.id("textures/gui/spin_gui.png");
        this.caseTitle = caseTitle;
        this.entry = entry;
        this.items = new ArrayList<>(lootItems);
        while (displayedItems.size() < 30) addRandomItemToDisplayedItems();
    }

    @Override
    protected void init() {
        super.init();
        this.player = this.minecraft.player;
    }


    @Override
    public void tick() {
        tickCounter++;
        if (tickCounter >= 30 && !isSlowingDown) isSlowingDown = true;
        if (isSlowingDown) {
            speed = Math.max(0.1f, speed * 0.95f);
            if (speed <= 0.1f && !rewardSelected) {
                speed = 0;
                rewardSelected = true;
                stopTime = System.currentTimeMillis();
                selectedItem = getSelectedReward();
            }
        }
        itemScrollPosition += speed;
        while (displayedItems.size() < itemScrollPosition / 18 + 50) addRandomItemToDisplayedItems();
        if (rewardSelected && System.currentTimeMillis() - stopTime >= 500) {
            if (selectedItem.requiresChoice() && selectedItem.getRewardPoolSize() > 1) {
                this.minecraft.gui.setScreen(new BaseLootBoxSelectionScreen(selectedItem, this.minecraft.player, this.entry));
            } else {

                var resolved = selectedItem.resolveReward(RandomSource.create());
                var rarity   = selectedItem.getRarity();

                this.minecraft.gui.setScreen(new BaseLootBoxRewardScreen(resolved, rarity, this.minecraft.player, this.entry));
            }
        }
    }

    private RarityItem getSelectedReward() {
        int centerOffset = 88; // visually center-aligned at x + 88
        int index = (int) ((itemScrollPosition + centerOffset) / 16) % displayedItems.size();
        if (index < 0) index += displayedItems.size();
        return displayedItems.get(index);
    }

    private void addRandomItemToDisplayedItems() {
        int roll = random.nextInt(10_000);
        Rarity rarity = (roll < 7800) ? Rarity.COMMON :
                (roll < 9000) ? Rarity.UNCOMMON :
                        (roll < 9800) ? Rarity.RARE :
                                (roll < 9975) ? Rarity.VERY_RARE :
                                        Rarity.ULTRA_RARE;

        RarityItem item = getRandomItemByRarity(rarity);
        if (item != null) {
            displayedItems.add(item);
        }
    }


    private RarityItem getRandomItemByRarity(Rarity rarity) {
        List<RarityItem> filtered = items.stream().filter(i -> i.getRarity() == rarity).toList();
        if (!filtered.isEmpty()) {
            return filtered.get(random.nextInt(filtered.size()));
        }

        for (Rarity fallback : Rarity.values()) {
            filtered = items.stream().filter(i -> i.getRarity() == fallback).toList();
            if (!filtered.isEmpty()) {
                return filtered.get(random.nextInt(filtered.size()));
            }
        }

        return items.get(random.nextInt(items.size()));
    }

    private int getRarityColor(Rarity rarity) {
        return rarity.getColor();
    }

    private void drawCenteredString(GuiGraphicsExtractor guiGraphics, Font font, String text, int centerX, int y, int color) {
        int width = font.width(text);
        guiGraphics.text(font, text, centerX - width / 2, y, color, false);
    }

    private void renderScrollingItems(GuiGraphicsExtractor gui, int x, int y) {
        int itemSize = 16;
        int leftBound = x + 3;
        int rightBound = x + 173;
        float startX = leftBound - (itemScrollPosition % itemSize);
        int startIndex = (int)(itemScrollPosition / itemSize);

        for (int i = 0; i < 20; i++) {
            float itemX = startX + (i * itemSize);
            if (itemX + itemSize <= leftBound || itemX >= rightBound) continue;
            RarityItem rarityItem = displayedItems.get((startIndex + i) % displayedItems.size());
            gui.fill(RenderPipelines.GUI, (int) itemX, y - 1, (int) (itemX + 16), y + 17, getRarityColor(rarityItem.getRarity()));
            gui.item(rarityItem.getItemStack(), (int) itemX, y);
            gui.itemDecorations(this.font, rarityItem.getItemStack(), (int) itemX, y);
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor gui, int mouseX, int mouseY, float partialTicks) {
        super.extractRenderState(gui, mouseX, mouseY, partialTicks);

        int screenWidth = this.width;
        int screenHeight = this.height;

        int x = (screenWidth - 176) / 2;
        int y = (screenHeight - 70) / 2;

        drawCenteredString(gui, this.font, this.caseTitle.getString(), screenWidth / 2, y + 6, 0xFFFFFF);

        int scissorX = x + 3;
        int scissorY = y + 23;
        int scissorWidth = 170;
        int scissorHeight = 18;
        gui.enableScissor(scissorX, scissorY, scissorX + scissorWidth, scissorY + scissorHeight);
        renderScrollingItems(gui, x, y + 23);
        gui.disableScissor();

        gui.fill(RenderPipelines.GUI, x + 88, y + 22, x + 89, y + 41, 0xFF555555);

        RarityItem selected = getSelectedReward();
        ItemStack stack = selected.getItemStack();
        Component displayName = Optional.ofNullable(stack.get(DataComponents.JUKEBOX_PLAYABLE))
                .map(playable -> playable.song().unwrap())
                .map(either -> either.map(
                        key -> minecraft.level.registryAccess()
                                .lookupOrThrow(net.minecraft.core.registries.Registries.JUKEBOX_SONG)
                                .getOrThrow(key).value().description(), JukeboxSong::description)).orElse(stack.getHoverName());

        TextUtils.drawCenteredWrappedString(gui, this.font, displayName.getString(), screenWidth / 2, y + 45, 170, getRarityColor(selected.getRarity()));
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor gui, int mouseX, int mouseY, float partialTicks) {
        this.extractTransparentBackground(gui);

        int x = (this.width - 176) / 2;
        int y = (this.height - 70) / 2;

        gui.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 0, 0, 176, 79, 256, 256);
    }

    @Override
    public void onClose() {
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}