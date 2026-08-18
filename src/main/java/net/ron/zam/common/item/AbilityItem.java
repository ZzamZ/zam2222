package net.ron.zam.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class AbilityItem extends Item {

    public AbilityItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);

        if (Minecraft.getInstance().hasShiftDown()) {
            tooltip.accept(Component.translatable(getDescriptionId() + ".ability")
                    .withStyle(ChatFormatting.AQUA));

            tooltip.accept(Component.translatable(getDescriptionId() + ".ability.description.1")
                    .withStyle(ChatFormatting.GRAY));

            tooltip.accept(Component.translatable(getDescriptionId() + ".ability.description.2")
                    .withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.accept(Component.translatable("tooltip.zam.hold_shift")
                    .withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}