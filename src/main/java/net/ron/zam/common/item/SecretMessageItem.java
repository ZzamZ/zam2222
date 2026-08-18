package net.ron.zam.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.ron.zam.common.gui.SecretMessageScreen;
import net.ron.zam.registry.ZAMComponents;

import java.util.function.Consumer;

public class SecretMessageItem extends Item {

    public SecretMessageItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            int message = player.getItemInHand(hand).getOrDefault(ZAMComponents.SECRET_MESSAGE, 0);

            player.playSound(SoundEvents.BOOK_PAGE_TURN, 1.0F, 1.0F);
            Minecraft.getInstance().gui.setScreen(new SecretMessageScreen(message));
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag) {
        int message = stack.getOrDefault(ZAMComponents.SECRET_MESSAGE, 0);

        if (message > 0) {
            consumer.accept(
                    Component.translatable("tooltip.zam.secret_message", message)
                            .withStyle(ChatFormatting.GRAY)
            );
        }
    }
}