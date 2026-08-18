package net.ron.zam.common.item.caserewards;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.function.Consumer;

public class CaseRewardFishingRodItem extends FishingRodItem {

    private final String collection;
    private final SoundEvent castSound;
    private final SoundEvent biteSound;
    private final SoundEvent pullSound;

    public CaseRewardFishingRodItem(String collection, SoundEvent castSound, SoundEvent biteSound, SoundEvent pullSound, Properties properties) {
        super(properties);
        this.collection = collection;
        this.castSound = castSound;
        this.biteSound = biteSound;
        this.pullSound = pullSound;
    }

    public CaseRewardFishingRodItem(String collection, Properties properties) {
        this(
                collection,
                SoundEvents.FISHING_BOBBER_THROW,
                SoundEvents.FISHING_BOBBER_SPLASH,
                SoundEvents.FISHING_BOBBER_RETRIEVE,
                properties
        );
    }

    public CaseRewardFishingRodItem(Properties properties) {
        this(null, properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (player.fishing != null) {
            if (!level.isClientSide()) {
                int damage = player.fishing.retrieve(itemStack);
                itemStack.hurtAndBreak(damage, player, hand.asEquipmentSlot());
            }

            level.playSound(null, player.getX(), player.getY(), player.getZ(), this.pullSound, SoundSource.NEUTRAL, 1.0F, 1.0F);
            itemStack.causeUseVibration(player, GameEvent.ITEM_INTERACT_FINISH);
        } else {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), this.castSound, SoundSource.NEUTRAL, 0.5F, 1.0F);

            if (level instanceof ServerLevel serverLevel) {
                int lureSpeed = (int)(EnchantmentHelper.getFishingTimeReduction(serverLevel, itemStack, player) * 20.0F);
                int luck = EnchantmentHelper.getFishingLuckBonus(serverLevel, itemStack, player);
                Projectile.spawnProjectile(new FishingHook(player, level, luck, lureSpeed), serverLevel, itemStack);
            }

            player.awardStat(Stats.ITEM_USED.get(this));
            itemStack.causeUseVibration(player, GameEvent.ITEM_INTERACT_START);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, consumer, flag);

        if (collection != null && !collection.isEmpty()) {
            consumer.accept(Component.literal(collection + " Collection")
                    .withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
        }
    }

    public SoundEvent getBiteSound() {
        return biteSound;
    }
}