package net.ron.zam.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.ron.zam.api.casesystem.cases.CaseMenuProvider;
import net.ron.zam.api.casesystem.cases.CaseRewards;
import net.ron.zam.registry.ZAMComponents;
import net.ron.zam.registry.ZAMItems;

import java.util.function.Consumer;

public class CaseItem extends Item {

    public CaseItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!stack.is(ZAMItems.CASE)) {
            return InteractionResult.PASS;
        }

        Identifier caseId = stack.get(ZAMComponents.CASE_ID);

        if (caseId == null) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            var entry = CaseRewards.getById(caseId);

            if (entry.isEmpty()) {
                return InteractionResult.FAIL;
            }

            serverPlayer.openMenu(
                    new CaseMenuProvider(entry.get())
            );
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, consumer, flag);

        Component name = stack.get(DataComponents.CUSTOM_NAME);

        if (name != null) {
            String collection = name.getString();

            if (collection.endsWith(" Case")) {
                collection = collection.substring(0, collection.length() - " Case".length());
            }

            consumer.accept(
                    Component.literal(collection + " Collection")
                            .withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC)
            );
        }

        consumer.accept(Component.literal("Drop Rates:").withStyle(ChatFormatting.YELLOW));
        consumer.accept(Component.literal(" - Common: 78%").withStyle(ChatFormatting.GRAY));
        consumer.accept(Component.literal(" - Uncommon: 12%").withStyle(ChatFormatting.GREEN));
        consumer.accept(Component.literal(" - Rare: 8%").withStyle(ChatFormatting.BLUE));
        consumer.accept(Component.literal(" - Very Rare: 1.75%").withStyle(ChatFormatting.LIGHT_PURPLE));
        consumer.accept(Component.literal(" - Ultra Rare: 0.25%").withStyle(ChatFormatting.GOLD));
    }
}