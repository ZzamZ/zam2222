package net.ron.zam.api.casesystem;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.ron.zam.api.casesystem.cases.CaseEntry;
import net.ron.zam.api.casesystem.cases.CaseReward;
import net.ron.zam.api.casesystem.cases.CaseStacks;
import net.ron.zam.api.rarity.Rarity;
import net.ron.zam.api.rarity.RarityItem;
import net.ron.zam.common.data.ZAMSavedData;
import net.ron.zam.registry.ZAMComponents;
import net.ron.zam.registry.ZAMItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class BaseLootBoxMenu<T extends BaseLootBoxMenu<T>> extends AbstractContainerMenu {
    private final ReadOnlyInventory lootInventory;
    private final List<RarityItem> lootItems;
    private final CaseEntry entry;
    private final ItemStack requiredKeyItem;
    private final int rows;

    private boolean rewardAuthorized;

    protected BaseLootBoxMenu(MenuType<T> menuType, int id, Inventory inventory, CaseEntry entry, Player player) {
        super(menuType, id);

        this.entry = entry;
        this.rows = entry.rows();
        this.requiredKeyItem = entry.keyItem().createStack(player.registryAccess());
        this.lootItems = materializeRewards(entry, player);
        this.lootInventory = new ReadOnlyInventory(this.lootItems.size());

        for (int i = 0; i < this.lootItems.size(); i++) {
            RarityItem reward = this.lootItems.get(i);
            ItemStack display = reward.getItemStack().copy();

            int total = reward.getRewardPoolSize();
            int collected = 0;

            if (player.level().getServer() != null) {
                if (total == 1) {
                    if (ZAMSavedData.isCollected(player.level().getServer(), player, reward.getSingleReward().getItem())) {
                        collected = 1;
                    }
                } else {
                    for (ItemStack stack : reward.viewRewardPool()) {
                        if (ZAMSavedData.isCollected(player.level().getServer(), player, stack.getItem())) {
                            collected++;
                        }
                    }
                }
            }

            boolean gold = display.is(ZAMItems.GOLD_ICON);
            boolean red = display.is(ZAMItems.RED_ICON);
            boolean choice = reward.requiresChoice();
            boolean mystery = total > 1 || gold || red || choice;

            List<Component> lines = new ArrayList<>();
            ItemLore lore = display.get(DataComponents.LORE);

            if (lore != null && !lore.lines().isEmpty()) {
                lines.addAll(lore.lines());
            }

            if (mystery) {
                if (red) {
                    display.set(
                            DataComponents.CUSTOM_NAME,
                            Component.literal("Cosmetic Apparel")
                                    .withStyle(ChatFormatting.RED, ChatFormatting.BOLD)
                    );
                } else if (gold) {
                    display.set(
                            DataComponents.CUSTOM_NAME,
                            Component.literal("Cosmetic Equipment")
                                    .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)
                    );
                }

                if (total > 1 && collected > 0) {
                    removeCollected(lines);
                    lines.add(
                            Component.literal("Collected: " + collected + "/" + total)
                                    .withStyle(ChatFormatting.GRAY)
                    );
                    checkmark(lines);
                } else if (total == 1 && collected > 0) {
                    checkmark(lines);
                }
            }

            display.set(DataComponents.LORE, new ItemLore(lines));
            this.lootInventory.setItem(i, display);
        }

        int cap = Math.min(this.lootInventory.getContainerSize(), this.rows * 9);

        for (int i = 0; i < cap; i++) {
            this.addSlot(new ReadOnlySlot(
                    this.lootInventory,
                    i,
                    8 + (i % 9) * 18,
                    70 + (i / 9) * 18
            ));
        }
    }

    private static List<RarityItem> materializeRewards(CaseEntry entry, Player player) {
        List<RarityItem> rewards = new ArrayList<>();

        for (CaseReward reward : entry.possibleRewards()) {
            ItemStack display = reward.display().createStack(player.registryAccess());

            if (reward.choice()) {
                List<ItemStack> pool = reward.rewards().stream()
                        .map(item -> item.createStack(player.registryAccess()))
                        .toList();

                rewards.add(RarityItem.mystery(
                        display,
                        pool,
                        reward.rarity()
                ));
            } else {
                rewards.add(new RarityItem(
                        reward.rewards().getFirst().createStack(player.registryAccess()),
                        reward.rarity()
                ));
            }
        }

        return List.copyOf(rewards);
    }

    private static void checkmark(List<Component> lines) {
        if (lines.stream().noneMatch(c -> c.getString().contains("✔") || c.getString().contains("✓"))) {
            lines.add(Component.literal("✔ Collected").withStyle(ChatFormatting.GREEN));
        }
    }

    private static void removeCollected(List<Component> lines) {
        lines.removeIf(c -> c.getString()
                .toLowerCase(Locale.ROOT)
                .replace("✔", "")
                .replace("✓", "")
                .trim()
                .startsWith("collected"));
    }

    private static void ensure(List<Component> lines, String prefix, Component line) {
        boolean exists = lines.stream()
                .anyMatch(c -> c.getString().toLowerCase(Locale.ROOT).startsWith(prefix));

        if (!exists) {
            lines.add(line);
        }
    }

    public boolean hasRequiredItems(Player player) {
        return countItem(player.getInventory(), this.requiredKeyItem) >= this.requiredKeyItem.getCount()
                && countCase(player.getInventory()) > 0;
    }

    public boolean consumeRequiredItems(Player player) {
        if (this.rewardAuthorized || !hasRequiredItems(player)) {
            return false;
        }

        if (!removeItem(player.getInventory(), this.requiredKeyItem)) {
            return false;
        }

        if (!removeCase(player.getInventory())) {
            ItemStack restored = this.requiredKeyItem.copy();

            if (!player.getInventory().add(restored)) {
                player.drop(restored, false);
            }

            return false;
        }

        this.rewardAuthorized = true;
        return true;
    }

    private int countCase(Inventory inventory) {
        int count = 0;

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);

            if (isMatchingCase(stack)) {
                count += stack.getCount();
            }
        }

        return count;
    }

    private boolean removeCase(Inventory inventory) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);

            if (isMatchingCase(stack)) {
                stack.shrink(1);
                return true;
            }
        }

        return false;
    }

    private boolean isMatchingCase(ItemStack stack) {
        Identifier caseId = stack.get(ZAMComponents.CASE_ID);

        return stack.is(ZAMItems.CASE)
                && caseId != null
                && caseId.equals(this.entry.id());
    }

    private static int countItem(Inventory inventory, ItemStack required) {
        int count = 0;

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);

            if (ItemStack.isSameItemSameComponents(stack, required)) {
                count += stack.getCount();
            }
        }

        return count;
    }

    private static boolean removeItem(Inventory inventory, ItemStack required) {
        int remaining = required.getCount();

        for (int i = 0; i < inventory.getContainerSize() && remaining > 0; i++) {
            ItemStack stack = inventory.getItem(i);

            if (!ItemStack.isSameItemSameComponents(stack, required)) {
                continue;
            }

            int remove = Math.min(stack.getCount(), remaining);
            stack.shrink(remove);
            remaining -= remove;
        }

        return remaining == 0;
    }

    public boolean isValidReward(ItemStack reward, Rarity rarity, Identifier caseId) {
        if (!this.rewardAuthorized || reward.isEmpty() || !this.entry.id().equals(caseId)) {
            return false;
        }

        for (RarityItem item : this.lootItems) {
            if (item.getRarity() != rarity) {
                continue;
            }

            for (ItemStack valid : item.viewRewardPool()) {
                if (ItemStack.isSameItemSameComponents(valid, reward)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean consumeRewardAuthorization() {
        if (!this.rewardAuthorized) {
            return false;
        }

        this.rewardAuthorized = false;
        return true;
    }

    public boolean isRewardAuthorized() {
        return this.rewardAuthorized;
    }

    public CaseEntry getCaseEntry() {
        return this.entry;
    }

    public List<RarityItem> getLootItems() {
        return this.lootItems;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    private static class ReadOnlySlot extends Slot {
        public ReadOnlySlot(Container inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public boolean mayPickup(Player player) {
            return false;
        }
    }
}