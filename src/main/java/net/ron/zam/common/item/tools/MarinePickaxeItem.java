package net.ron.zam.common.item.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.ron.zam.common.item.AbilityItem;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class MarinePickaxeItem extends AbilityItem {

    private static final int MAX_VEIN = 32;

    public MarinePickaxeItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {

        if (!(entity instanceof Player player)) {
            return super.mineBlock(stack, level, state, pos, entity);
        }

        if (level.isClientSide()) {
            return super.mineBlock(stack, level, state, pos, entity);
        }

        if (!player.isShiftKeyDown()) {
            return super.mineBlock(stack, level, state, pos, entity);
        }

        if (!(state.getBlock() instanceof DropExperienceBlock)) {
            return super.mineBlock(stack, level, state, pos, entity);
        }

        // Hunger check
        if (player.getFoodData().getFoodLevel() < 3) {
            return super.mineBlock(stack, level, state, pos, entity);
        }

        ServerLevel serverLevel = (ServerLevel) level;

        Set<BlockPos> vein = findVein(serverLevel, pos);

        int mined = 0;

        for (BlockPos target : vein) {

            if (target.equals(pos)) continue;

            BlockState targetState = serverLevel.getBlockState(target);

            if (!(targetState.getBlock() instanceof DropExperienceBlock)) continue;

            Block.dropResources(targetState, serverLevel, target, serverLevel.getBlockEntity(target), player, stack);

            serverLevel.setBlock(target, Blocks.AIR.defaultBlockState(), 3);

            mined++;
        }

        if (mined > 0) {

            stack.hurtAndBreak(mined, entity, EquipmentSlot.MAINHAND);

            FoodData food = player.getFoodData();
            food.setFoodLevel(Math.max(food.getFoodLevel() - 3, 0));
        }

        return super.mineBlock(stack, level, state, pos, entity);
    }

    private Set<BlockPos> findVein(ServerLevel level, BlockPos start) {

        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();

        queue.add(start);

        while (!queue.isEmpty() && visited.size() < MAX_VEIN) {

            BlockPos current = queue.poll();

            if (!visited.add(current)) continue;

            for (BlockPos neighbor : BlockPos.betweenClosed(
                    current.offset(-1,-1,-1),
                    current.offset(1,1,1))) {

                if (visited.contains(neighbor)) continue;

                BlockState neighborState = level.getBlockState(neighbor);

                if (neighborState.getBlock() instanceof DropExperienceBlock) {
                    queue.add(neighbor.immutable());
                }
            }
        }

        return visited;
    }
}