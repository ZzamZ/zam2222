package net.ron.zam.common.item.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.golem.CopperGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.ron.zam.common.item.AbilityItem;

import java.util.Optional;

public class MarineShovelItem extends AbilityItem {

    public MarineShovelItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        if (!(state.getBlock() instanceof WeatheringCopper weatheringCopper)) {
            return InteractionResult.PASS;
        }

        Optional<BlockState> nextState = weatheringCopper.getNext(state);

        if (nextState.isEmpty()) {
            return InteractionResult.PASS;
        }

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.setBlock(pos, nextState.get(), 11);
            serverLevel.gameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, pos);

            serverLevel.sendParticles(ParticleTypes.BUBBLE_POP,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    16, 0.35, 0.35, 0.35, 0.03);

            serverLevel.sendParticles(ParticleTypes.SPLASH,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    10, 0.35, 0.25, 0.35, 0.06);

            serverLevel.playSound(null, pos, SoundEvents.WET_GRASS_PLACE,
                    SoundSource.BLOCKS, 1.0F, 1.0F);

            Player player = context.getPlayer();

            if (player != null) {
                player.swing(context.getHand());
                context.getItemInHand().hurtAndBreak(1, player, getSlot(context.getHand()));
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        if (!(entity instanceof CopperGolem copperGolem)) {
            return InteractionResult.PASS;
        }

        WeatheringCopper.WeatherState nextState = getNextWeatherState(copperGolem.getWeatherState());

        if (nextState == null) {
            return InteractionResult.PASS;
        }

        if (player.level() instanceof ServerLevel serverLevel) {
            copperGolem.setWeatherState(nextState);
            copperGolem.gameEvent(GameEvent.ENTITY_INTERACT, player);

            serverLevel.sendParticles(ParticleTypes.BUBBLE_POP,
                    copperGolem.getX(),
                    copperGolem.getY() + copperGolem.getBbHeight() * 0.5,
                    copperGolem.getZ(),
                    20,
                    copperGolem.getBbWidth() * 0.6,
                    copperGolem.getBbHeight() * 0.4,
                    copperGolem.getBbWidth() * 0.6,
                    0.03);

            serverLevel.sendParticles(ParticleTypes.SPLASH,
                    copperGolem.getX(),
                    copperGolem.getY() + copperGolem.getBbHeight() * 0.5,
                    copperGolem.getZ(),
                    12,
                    copperGolem.getBbWidth() * 0.5,
                    copperGolem.getBbHeight() * 0.35,
                    copperGolem.getBbWidth() * 0.5,
                    0.06);

            serverLevel.playSound(null, copperGolem.blockPosition(),
                    SoundEvents.WET_GRASS_PLACE, SoundSource.NEUTRAL, 1.0F, 1.0F);

            player.swing(hand);
            stack.hurtAndBreak(1, player, getSlot(hand));
        }

        return InteractionResult.SUCCESS;
    }

    private static EquipmentSlot getSlot(InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
    }

    private static WeatheringCopper.WeatherState getNextWeatherState(WeatheringCopper.WeatherState state) {
        return switch (state) {
            case UNAFFECTED -> WeatheringCopper.WeatherState.EXPOSED;
            case EXPOSED -> WeatheringCopper.WeatherState.WEATHERED;
            case WEATHERED -> WeatheringCopper.WeatherState.OXIDIZED;
            case OXIDIZED -> null;
        };
    }
}