package net.ron.zam.common.item.caserewards;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class CaseRewardMaceItem extends MaceItem {
    private final String collection;

    public CaseRewardMaceItem(String collection, Properties properties) {
        super(properties);
        this.collection = collection;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, consumer, flag);

        if (collection != null && !collection.isEmpty()) {
            consumer.accept(Component.literal(collection + " Collection")
                    .withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
        }
    }
}