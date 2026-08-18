package net.ron.zam.util.tooltips;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record MusicBoxTooltip(ItemStack disc, boolean paused, boolean looping) implements TooltipComponent {
}