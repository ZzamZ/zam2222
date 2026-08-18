package net.ron.zam.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.ron.zam.registry.ZAMBlocks;
import net.ron.zam.registry.ZAMItems;
import org.jetbrains.annotations.Nullable;

public class CornCropBlock extends CropBlock {
    public static final MapCodec<CornCropBlock> CODEC = simpleCodec(CornCropBlock::new);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    private static final VoxelShape[] SHAPES = {
            Block.box(2, 0, 2, 14, 5, 14),
            Block.box(2, 0, 2, 14, 11, 14),
            Block.box(2, 0, 2, 14, 16, 14),
            Block.box(2, 0, 2, 14, 16, 14)
    };

    public CornCropBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(AGE, 0)
                .setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public MapCodec<? extends CropBlock> codec() {
        return CODEC;
    }

    @Override
    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ZAMItems.CORN_KERNELS;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[getAge(state)];
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER && !isMaxAge(state);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER)
            return;

        float speed = CropBlock.getGrowthSpeed(this, level, pos);

        if (random.nextInt((int) (25.0F / speed) + 1) == 0)
            grow(level, state, pos, 1);
    }

    private void grow(ServerLevel level, BlockState lowerState, BlockPos lowerPos, int increase) {
        int age = Math.min(getAge(lowerState) + increase, getMaxAge());

        if (!canGrow(level, lowerPos, lowerState, age))
            return;

        BlockState newLower = lowerState
                .setValue(AGE, age)
                .setValue(HALF, DoubleBlockHalf.LOWER);

        level.setBlock(lowerPos, newLower, 2);

        if (isDouble(age))
            level.setBlock(lowerPos.above(),
                    newLower.setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }

    private boolean canGrow(LevelReader level, BlockPos lowerPos, BlockState lowerState, int newAge) {
        return !isMaxAge(lowerState)
                && hasSufficientLight(level, lowerPos)
                && level.isInsideBuildHeight(lowerPos.above())
                && (!isDouble(newAge) || canGrowInto(level, lowerPos.above()));
    }

    private static boolean canGrowInto(LevelReader level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.isAir() || state.is(ZAMBlocks.CORN);
    }

    private static boolean isDouble(int age) {
        return age >= 2;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            BlockState below = level.getBlockState(pos.below());

            return below.is(this)
                    && below.getValue(HALF) == DoubleBlockHalf.LOWER
                    && below.getValue(AGE) >= 2;
        }

        return super.canSurvive(state, level, pos);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks,
                                     BlockPos pos, Direction direction, BlockPos neighborPos,
                                     BlockState neighborState, RandomSource random) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER
                && direction == Direction.DOWN
                && (!neighborState.is(this)
                || neighborState.getValue(HALF) != DoubleBlockHalf.LOWER))
            return Blocks.AIR.defaultBlockState();

        if (state.getValue(HALF) == DoubleBlockHalf.LOWER
                && isDouble(state.getValue(AGE))
                && direction == Direction.UP
                && (!neighborState.is(this)
                || neighborState.getValue(HALF) != DoubleBlockHalf.UPPER))
            return Blocks.AIR.defaultBlockState();

        return super.updateShape(state, level, ticks, pos,
                direction, neighborPos, neighborState, random);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide() && isDouble(state.getValue(AGE))) {
            BlockPos otherPos = state.getValue(HALF) == DoubleBlockHalf.UPPER
                    ? pos.below()
                    : pos.above();

            BlockState other = level.getBlockState(otherPos);

            if (other.is(this))
                level.destroyBlock(otherPos, false, player);
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        PosAndState lower = getLowerHalf(level, pos, state);

        return lower != null
                && canGrow(level, lower.pos(), lower.state(), getAge(lower.state()) + 1);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        PosAndState lower = getLowerHalf(level, pos, state);

        if (lower != null)
            grow(level, lower.state(), lower.pos(), 1);
    }

    @Nullable
    private PosAndState getLowerHalf(LevelReader level, BlockPos pos, BlockState state) {
        if (state.is(this) && state.getValue(HALF) == DoubleBlockHalf.LOWER)
            return new PosAndState(pos, state);

        BlockPos lowerPos = pos.below();
        BlockState lowerState = level.getBlockState(lowerPos);

        return lowerState.is(this)
                && lowerState.getValue(HALF) == DoubleBlockHalf.LOWER
                ? new PosAndState(lowerPos, lowerState)
                : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, HALF);
    }

    private record PosAndState(BlockPos pos, BlockState state) {}
}