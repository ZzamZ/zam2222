package net.ron.zam.common.item.caserewards;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class CaseRewardAxeItem extends AxeItem {
    private final String collection;

    public CaseRewardAxeItem(String collection, ToolMaterial toolMaterial, float attackDamage, float attackSpeed, Properties properties) {
        super(toolMaterial, attackDamage, attackSpeed, properties);
        this.collection = collection;
    }

    public CaseRewardAxeItem(ToolMaterial toolMaterial, float attackDamage, float attackSpeed, Properties properties) {
        this(null, toolMaterial, attackDamage, attackSpeed, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, consumer, flag);

        if (collection != null && !collection.isEmpty()) {
            consumer.accept(Component.literal(collection + " Collection").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
        }
    }

    public String getCollection() {
        return collection;
    }
}
