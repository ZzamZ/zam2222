package net.ron.zam.common.block.television;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.ron.zam.ZAMMod;
import net.ron.zam.api.television.PowerState;
import net.ron.zam.common.item.CassetteItem;
import net.ron.zam.registry.ZAMBlockEntities;
import net.ron.zam.registry.ZAMItems;
import net.ron.zam.registry.ZAMSounds;
import org.jetbrains.annotations.Nullable;

public class TelevisionBlock extends BaseEntityBlock {
    public static final MapCodec<TelevisionBlock> CODEC = simpleCodec(TelevisionBlock::new);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<Connection> CONNECTION = EnumProperty.create("connection", Connection.class);
    public static final EnumProperty<PowerState> POWER_STATE = EnumProperty.create("power_state", PowerState.class);

    public TelevisionBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(CONNECTION, Connection.SINGLE)
                .setValue(POWER_STATE, PowerState.OFF));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, CONNECTION, POWER_STATE);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction facing = ctx.getHorizontalDirection().getOpposite();
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();

        return defaultBlockState()
                .setValue(FACING, facing)
                .setValue(POWER_STATE, computePower(level, pos));
    }

    private static PowerState computePower(Level level, BlockPos pos) {
        if (level.hasNeighborSignal(pos))
            return PowerState.DIRECT;

        return level.getBestNeighborSignal(pos) > 0
                ? PowerState.INDIRECT
                : PowerState.OFF;
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock,
                                   @Nullable net.minecraft.world.level.redstone.Orientation orientation,
                                   boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, orientation, movedByPiston);

        if (level.isClientSide()) return;

        PowerState power = computePower(level, pos);

        if (state.getValue(POWER_STATE) != power)
            level.setBlock(pos, state.setValue(POWER_STATE, power), 3);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        if (!(level.getBlockEntity(pos) instanceof TelevisionBlockEntity tv))
            return 0;

        ItemStack cassette = tv.cassette();

        if (cassette.isEmpty())
            return 0;

        net.minecraft.resources.Identifier id =
                CassetteItem.getCassetteId(cassette);

        if (id == null)
            return 1;

        var data = ZAMMod.CASSETTES.get(id);

        return data != null
                ? data.comparatorOutput()
                : 1;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TelevisionBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            BlockEntityType<T> type
    ) {
        return createTickerHelper(
                type,
                ZAMBlockEntities.TELEVISION,
                (l, p, s, be) ->
                        TelevisionBlockEntity.tick(l, p, s, be)
        );
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hit) {
        TelevisionMultiblock.MasterTV master =
                TelevisionMultiblock.findMaster(
                        level,
                        pos,
                        state.getValue(FACING)
                );

        if (master == null)
            return InteractionResult.PASS;

        if (player.isSecondaryUseActive()
                && master.tv().cassette().is(ZAMItems.VIDEO_TAPE)) {
            if (!level.isClientSide())
                master.tv().togglePlay();

            return InteractionResult.SUCCESS;
        }

        if (stack.is(ZAMItems.CASSETTE)
                || stack.is(ZAMItems.VIDEO_TAPE)) {
            if (!master.tv().cassette().isEmpty())
                return InteractionResult.FAIL;

            master.tv().setCassette(stack.copyWithCount(1));

            if (!player.getAbilities().instabuild)
                stack.shrink(1);

            level.playSound(
                    null,
                    pos,
                    ZAMSounds.CASSETTE_INSERT,
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F
            );

            return InteractionResult.SUCCESS;
        }

        if (stack.isEmpty()) {
            if (tryEject(state, level, pos, player))
                return InteractionResult.SUCCESS;

            return InteractionResult.PASS;
        }

        return InteractionResult.PASS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        TelevisionMultiblock.MasterTV master =
                TelevisionMultiblock.findMaster(
                        level,
                        pos,
                        state.getValue(FACING)
                );

        if (master == null)
            return InteractionResult.PASS;

        if (player.isSecondaryUseActive()) {
            if (!master.tv().cassette().is(ZAMItems.VIDEO_TAPE))
                return InteractionResult.PASS;

            if (!level.isClientSide())
                master.tv().togglePlay();

            return InteractionResult.SUCCESS;
        }

        if (tryEject(state, level, pos, player))
            return InteractionResult.SUCCESS;

        return InteractionResult.PASS;
    }

    private static boolean tryEject(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player
    ) {
        TelevisionMultiblock.MasterTV master =
                TelevisionMultiblock.findMaster(
                        level,
                        pos,
                        state.getValue(FACING)
                );

        if (master == null || master.tv().cassette().isEmpty())
            return false;

        ItemStack out = master.tv().cassette().copy();

        master.tv().setCassette(ItemStack.EMPTY);

        if (!level.isClientSide()) {
            Direction facing = state.getValue(FACING);

            double spawnX =
                    master.pos().getX() + 0.5
                            + facing.getStepX() * 0.6;

            double spawnY =
                    master.pos().getY() + 0.5;

            double spawnZ =
                    master.pos().getZ() + 0.5
                            + facing.getStepZ() * 0.6;

            var drop =
                    new net.minecraft.world.entity.item.ItemEntity(
                            level,
                            spawnX,
                            spawnY,
                            spawnZ,
                            out
                    );

            drop.setDeltaMovement(
                    facing.getStepX() * 0.15,
                    0.12,
                    facing.getStepZ() * 0.15
            );

            drop.setDefaultPickUpDelay();
            level.addFreshEntity(drop);
        }

        level.playSound(
                null,
                pos,
                ZAMSounds.CASSETTE_EJECT,
                SoundSource.BLOCKS,
                1.0F,
                1.0F
        );

        return true;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos,
                           BlockState oldState, boolean moving) {
        super.onPlace(state, level, pos, oldState, moving);

        if (level.isClientSide() || oldState.is(this))
            return;

        TelevisionMultiblock.refresh(
                level,
                pos,
                state.getValue(FACING)
        );
    }

    @Override
    protected void affectNeighborsAfterRemoval(
            BlockState state,
            net.minecraft.server.level.ServerLevel level,
            BlockPos pos,
            boolean moving
    ) {
        TelevisionMultiblock.refreshNeighbors(
                level,
                pos,
                state.getValue(FACING)
        );

        super.affectNeighborsAfterRemoval(
                state,
                level,
                pos,
                moving
        );
    }

    public enum Connection implements StringRepresentable {
        SINGLE("single"),
        TOP("top"),
        BOTTOM("bottom"),
        LEFT("left"),
        RIGHT("right"),
        TOP_LEFT("top_left"),
        TOP_RIGHT("top_right"),
        BOTTOM_LEFT("bottom_left"),
        BOTTOM_RIGHT("bottom_right"),
        CENTER("center"),
        HORIZONTAL_LEFT("horizontal_left"),
        HORIZONTAL_MIDDLE("horizontal_middle"),
        HORIZONTAL_RIGHT("horizontal_right"),
        VERTICAL_TOP("vertical_top"),
        VERTICAL_MIDDLE("vertical_middle"),
        VERTICAL_BOTTOM("vertical_bottom");

        private final String name;

        Connection(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}