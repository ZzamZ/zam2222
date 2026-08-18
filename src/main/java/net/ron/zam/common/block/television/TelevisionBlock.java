package net.ron.zam.common.block.television;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.ron.zam.ZAMMod;
import net.ron.zam.api.television.PowerState;
import net.ron.zam.common.item.CassetteItem;
import net.ron.zam.registry.*;
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

    @Override protected MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, CONNECTION, POWER_STATE);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection().getOpposite())
                .setValue(POWER_STATE, computePower(ctx.getLevel(), ctx.getClickedPos()));
    }

    private static PowerState computePower(Level level, BlockPos pos) {
        if (level.hasNeighborSignal(pos)) return PowerState.DIRECT;
        return level.getBestNeighborSignal(pos) > 0 ? PowerState.INDIRECT : PowerState.OFF;
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, @Nullable net.minecraft.world.level.redstone.Orientation orientation, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, orientation, movedByPiston);
        if (level.isClientSide()) return;

        PowerState power = computePower(level, pos);
        if (state.getValue(POWER_STATE) != power)
            level.setBlock(pos, state.setValue(POWER_STATE, power), 3);
    }

    @Override protected boolean hasAnalogOutputSignal(BlockState state) { return true; }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        if (!(level.getBlockEntity(pos) instanceof TelevisionBlockEntity tv)) return 0;

        ItemStack cassette = tv.cassette();
        if (cassette.isEmpty()) return 0;

        var id = CassetteItem.getCassetteId(cassette);
        if (id == null) return 1;

        var data = ZAMMod.CASSETTES.get(id);
        return data != null ? data.comparatorOutput() : 1;
    }

    @Override public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TelevisionBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ZAMBlockEntities.TELEVISION, TelevisionBlockEntity::tick);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        var master = TelevisionMultiblock.findMaster(level, pos, state.getValue(FACING));
        if (master == null) return InteractionResult.PASS;

        if (player.isSecondaryUseActive() && master.tv().cassette().is(ZAMItems.VIDEO_TAPE)) {
            if (!level.isClientSide()) master.tv().togglePlay();
            return InteractionResult.SUCCESS;
        }

        if (stack.is(ZAMItems.CASSETTE) || stack.is(ZAMItems.VIDEO_TAPE)) {
            if (!master.tv().cassette().isEmpty()) return InteractionResult.FAIL;

            if (!level.isClientSide()) {
                master.tv().setCassette(stack.copyWithCount(1));
                if (!player.getAbilities().instabuild) stack.shrink(1);
            }

            level.playSound(null, pos, ZAMSounds.CASSETTE_INSERT, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }

        return stack.isEmpty() && tryEject(state, level, pos, player)
                ? InteractionResult.SUCCESS
                : InteractionResult.PASS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        var master = TelevisionMultiblock.findMaster(level, pos, state.getValue(FACING));
        if (master == null) return InteractionResult.PASS;

        if (player.isSecondaryUseActive()) {
            if (!master.tv().cassette().is(ZAMItems.VIDEO_TAPE))
                return InteractionResult.PASS;

            if (!level.isClientSide()) master.tv().togglePlay();
            return InteractionResult.SUCCESS;
        }

        return tryEject(state, level, pos, player)
                ? InteractionResult.SUCCESS
                : InteractionResult.PASS;
    }

    private static boolean tryEject(BlockState state, Level level, BlockPos pos, Player player) {
        if (!player.getMainHandItem().isEmpty())
            return false;

        var master = TelevisionMultiblock.findMaster(level, pos, state.getValue(FACING));
        if (master == null || master.tv().cassette().isEmpty())
            return false;

        if (!level.isClientSide()) {
            ItemStack out = master.tv().cassette().copy();
            master.tv().setCassette(ItemStack.EMPTY);

            Direction facing = state.getValue(FACING);
            ItemEntity drop = new ItemEntity(
                    level,
                    master.pos().getX() + 0.5D + facing.getStepX() * 0.6D,
                    master.pos().getY() + 0.5D,
                    master.pos().getZ() + 0.5D + facing.getStepZ() * 0.6D,
                    out
            );

            drop.setDeltaMovement(facing.getStepX() * 0.15D, 0.12D, facing.getStepZ() * 0.15D);
            drop.setDefaultPickUpDelay();
            level.addFreshEntity(drop);
        }

        level.playSound(null, pos, ZAMSounds.CASSETTE_EJECT, SoundSource.BLOCKS, 1.0F, 1.0F);
        return true;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moving) {
        super.onPlace(state, level, pos, oldState, moving);

        if (!level.isClientSide() && !oldState.is(this))
            TelevisionMultiblock.refresh(level, pos, state.getValue(FACING));
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()
                && level.getBlockEntity(pos) instanceof TelevisionBlockEntity tv
                && !tv.cassette().isEmpty()) {
            ItemStack cassette = tv.cassette().copy();
            tv.setCassette(ItemStack.EMPTY);

            ItemEntity drop = new ItemEntity(
                    level,
                    pos.getX() + 0.5D,
                    pos.getY() + 0.5D,
                    pos.getZ() + 0.5D,
                    cassette
            );

            drop.setDefaultPickUpDelay();
            level.addFreshEntity(drop);
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean moving) {
        TelevisionMultiblock.refreshNeighbors(level, pos, state.getValue(FACING));
        super.affectNeighborsAfterRemoval(state, level, pos, moving);
    }

    public enum Connection implements StringRepresentable {
        SINGLE("single"), TOP("top"), BOTTOM("bottom"), LEFT("left"), RIGHT("right"),
        TOP_LEFT("top_left"), TOP_RIGHT("top_right"), BOTTOM_LEFT("bottom_left"), BOTTOM_RIGHT("bottom_right"),
        CENTER("center"), HORIZONTAL_LEFT("horizontal_left"), HORIZONTAL_MIDDLE("horizontal_middle"),
        HORIZONTAL_RIGHT("horizontal_right"), VERTICAL_TOP("vertical_top"),
        VERTICAL_MIDDLE("vertical_middle"), VERTICAL_BOTTOM("vertical_bottom");

        private final String name;

        Connection(String name) { this.name = name; }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}