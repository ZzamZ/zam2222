package net.ron.zam.util.tooltips;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** items.size() == collectedFlags.size() (capped to <= 3 by the screen) */
public record RowItemsTooltip(List<ItemStack> items, List<Boolean> collectedFlags) implements TooltipComponent { }
