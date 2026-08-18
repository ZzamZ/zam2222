package net.ron.zam.common.item.caserewards;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class RewardItem extends Item {
    private final String collection;

    public RewardItem(String collection, Properties properties) {
        super(properties);
        this.collection = collection;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);

        if (collection != null && !collection.isEmpty()) {
            builder.accept(
                    Component.literal(collection + " Collection")
                            .withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC)
            );
        }
    }

    public String getCollection() {
        return collection;
    }
}