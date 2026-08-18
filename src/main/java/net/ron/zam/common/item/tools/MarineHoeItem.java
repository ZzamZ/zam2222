package net.ron.zam.common.item.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.ron.zam.common.item.AbilityItem;
import net.ron.zam.registry.ZAMBlocks;

public class MarineHoeItem extends AbilityItem {

    private static final IntProvider XP_REWARD = UniformInt.of(1, 3);

    public MarineHoeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        BlockState state = level.getBlockState(pos);

        if (!level.getBlockState(pos.above()).isAir()) return InteractionResult.PASS;

        if (state.is(BlockTags.DIRT)) {
            level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);

            if (!level.isClientSide()) {
                level.setBlock(pos, ZAMBlocks.MARINE_FARMLAND.defaultBlockState(), 2);

                if (player != null) {
                    int experience = XP_REWARD.sample(player.getRandom());
                    if (experience > 0) ExperienceOrb.award((ServerLevel) level, Vec3.atCenterOf(pos), experience);

                    context.getItemInHand().hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                }
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}