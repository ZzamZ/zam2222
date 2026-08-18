package net.ron.zam.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.ron.zam.registry.ZAMComponents;
import net.ron.zam.registry.ZAMItems;
import net.ron.zam.util.BottleMessages;

import java.util.function.Consumer;

public class MessageInABottleItem extends Item {

    public MessageInABottleItem(Properties properties) {
        super(properties.stacksTo(16));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack bottle = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            int message = bottle.getOrDefault(ZAMComponents.SECRET_MESSAGE, 0);

            if (message <= 0 || message > BottleMessages.count()) {
                message = player.getRandom().nextInt(BottleMessages.count()) + 1;
            }

            ItemStack secretMessage = new ItemStack(ZAMItems.SECRET_MESSAGE);
            secretMessage.set(ZAMComponents.SECRET_MESSAGE, message);

            if (bottle.getCount() == 1) {
                player.setItemInHand(hand, secretMessage);
            } else {
                bottle.shrink(1);

                if (!player.addItem(secretMessage)) {
                    player.drop(secretMessage, false);
                }
            }

            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag) {
        consumer.accept(
                Component.translatable("tooltip.zam.message_in_a_bottle_hint")
                        .withStyle(ChatFormatting.DARK_GRAY)
        );
    }
}