package net.ron.zam.common.item.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.ron.zam.common.item.AbilityItem;

import java.util.*;

public class MarineAxeItem extends AbilityItem {

    private static final int MAX_TREE = 64;
    private static final int HUNGER_COST = 3;

    public MarineAxeItem(Properties properties) {
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

        if (!isChoppableStructureBlock(state)) {
            return super.mineBlock(stack, level, state, pos, entity);
        }

        if (player.getFoodData().getFoodLevel() < HUNGER_COST) {
            return super.mineBlock(stack, level, state, pos, entity);
        }

        if (!isBaseBlock(level, pos, state)) {
            return super.mineBlock(stack, level, state, pos, entity);
        }

        if (!hasNaturalCanopyAbove(level, pos)) {
            return super.mineBlock(stack, level, state, pos, entity);
        }

        ServerLevel serverLevel = (ServerLevel) level;
        Set<BlockPos> structure = findStructure(serverLevel, pos);

        if (structure.size() <= 1) {
            return super.mineBlock(stack, level, state, pos, entity);
        }

        List<BlockPos> ordered = new ArrayList<>(structure);
        ordered.remove(pos);
        ordered.sort((a, b) -> Integer.compare(b.getY(), a.getY()));
        int chopped = 0;

        for (BlockPos target : ordered) {
            BlockState targetState = serverLevel.getBlockState(target);

            if (!isChoppableStructureBlock(targetState)) {
                continue;
            }

            Block.dropResources(targetState, serverLevel, target, serverLevel.getBlockEntity(target), player, stack);
            serverLevel.setBlock(target, Blocks.AIR.defaultBlockState(), 3);
            chopped++;
        }

        if (chopped > 0) {
            stack.hurtAndBreak(chopped, entity, EquipmentSlot.MAINHAND);

            FoodData food = player.getFoodData();
            food.setFoodLevel(Math.max(food.getFoodLevel() - HUNGER_COST, 0));
        }

        return super.mineBlock(stack, level, state, pos, entity);
    }

    private Set<BlockPos> findStructure(ServerLevel level, BlockPos start) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();
        queue.add(start);

        while (!queue.isEmpty() && visited.size() < MAX_TREE) {
            BlockPos current = queue.poll();

            if (!visited.add(current)) {
                continue;
            }

            for (BlockPos neighbor : BlockPos.betweenClosed(
                    current.offset(-1, 0, -1),
                    current.offset(1, 1, 1))) {

                BlockPos next = neighbor.immutable();

                if (visited.contains(next)) {
                    continue;
                }

                BlockState nextState = level.getBlockState(next);
                if (!isChoppableStructureBlock(nextState)) {
                    continue;
                }

                int dy = next.getY() - current.getY();

                if (dy < 0) {
                    continue;
                }

                if (dy == 1) {
                    queue.add(next);
                    continue;
                }

                if (dy == 0
                        && Math.abs(next.getX() - start.getX()) <= 2
                        && Math.abs(next.getZ() - start.getZ()) <= 2) {
                    queue.add(next);
                }
            }
        }

        return visited;
    }

    private boolean isBaseBlock(Level level, BlockPos pos, BlockState state) {
        BlockState below = level.getBlockState(pos.below());

        if (isSameStructureType(below, state)) {
            return false;
        }

        return true;
    }

    private boolean hasNaturalCanopyAbove(Level level, BlockPos pos) {
        for (BlockPos check : BlockPos.betweenClosed(
                pos.offset(-3, 1, -3),
                pos.offset(3, 12, 3))) {

            BlockState state = level.getBlockState(check);

            if (isCanopyBlock(state)) {
                return true;
            }
        }

        return false;
    }

    private boolean isChoppableStructureBlock(BlockState state) {
        return state.is(BlockTags.LOGS)
                || state.is(Blocks.MUSHROOM_STEM)
                || state.is(Blocks.RED_MUSHROOM_BLOCK)
                || state.is(Blocks.BROWN_MUSHROOM_BLOCK);
    }

    private boolean isCanopyBlock(BlockState state) {
        return state.is(BlockTags.LEAVES)
                || state.is(Blocks.NETHER_WART_BLOCK)
                || state.is(Blocks.WARPED_WART_BLOCK)
                || state.is(Blocks.SHROOMLIGHT)
                || state.is(Blocks.RED_MUSHROOM_BLOCK)
                || state.is(Blocks.BROWN_MUSHROOM_BLOCK);
    }

    private boolean isSameStructureType(BlockState a, BlockState b) {
        if (a.is(BlockTags.LOGS) && b.is(BlockTags.LOGS)) {
            return true;
        }

        if ((a.is(Blocks.MUSHROOM_STEM) || a.is(Blocks.RED_MUSHROOM_BLOCK) || a.is(Blocks.BROWN_MUSHROOM_BLOCK))
                && (b.is(Blocks.MUSHROOM_STEM) || b.is(Blocks.RED_MUSHROOM_BLOCK) || b.is(Blocks.BROWN_MUSHROOM_BLOCK))) {
            return true;
        }

        return false;
    }
}