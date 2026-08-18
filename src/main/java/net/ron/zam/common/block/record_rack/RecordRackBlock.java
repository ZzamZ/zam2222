package net.ron.zam.common.block.record_rack;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Util;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;

public class RecordRackBlock extends HorizontalDirectionalBlock implements EntityBlock {

    public static final MapCodec<RecordRackBlock> CODEC = simpleCodec(RecordRackBlock::new);

    public RecordRackBlock(Properties props) {
        super(props.noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction dir = ctx.getHorizontalDirection();
        return this.defaultBlockState().setValue(FACING, dir);
    }


    // === BASE SHAPE (NORTH) — EXACTLY MATCHES YOUR MODEL ===
    private static final VoxelShape SHAPE_NORTH = Shapes.or(
            Block.box(4, 0, 0.5, 12, 1, 15.5)
    ).optimize();

    // === ROTATED SHAPES (TROPHY SYSTEM) ===
    private static final Map<Direction, VoxelShape> SHAPES = Util.make(new EnumMap<>(Direction.class), m -> {
        m.put(Direction.NORTH, SHAPE_NORTH);
        VoxelShape east = rotateY(SHAPE_NORTH);
        m.put(Direction.EAST, east);
        m.put(Direction.SOUTH, rotateY(east));
        m.put(Direction.WEST, rotateY(m.get(Direction.SOUTH)));
    });

    private static VoxelShape rotateY(VoxelShape shape) {
        VoxelShape out = Shapes.empty();
        for (AABB a : shape.toAabbs()) {
            AABB r = new AABB(
                    1.0 - a.maxZ, a.minY, a.minX,
                    1.0 - a.minZ, a.maxY, a.maxX
            );
            out = Shapes.or(out, Shapes.create(r));
        }
        return out.optimize();
    }

    // === SHAPE API ===
    @Override
    public VoxelShape getShape(BlockState s, BlockGetter g, BlockPos p, CollisionContext c) {
        return SHAPES.get(s.getValue(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState s, BlockGetter g, BlockPos p, CollisionContext c) {
        return SHAPES.get(s.getValue(FACING));
    }

    @Override
    public VoxelShape getVisualShape(BlockState s, BlockGetter g, BlockPos p, CollisionContext c) {
        return SHAPES.get(s.getValue(FACING));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof RecordRackBlockEntity rack) {
            player.openMenu(rack);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return this.useWithoutItem(state, level, pos, player, hit);
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        if (level instanceof Level lvl && lvl.getBlockEntity(pos) instanceof RecordRackBlockEntity rack) {
            Containers.dropContents(lvl, pos, rack);
        }
        super.destroy(level, pos, state);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction dir) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof RecordRackBlockEntity rack)) return 0;

        int filled = 0;
        for (int i = 0; i < RecordRackBlockEntity.SIZE; i++) {
            if (!rack.getItem(i).isEmpty()) {
                filled++;
            }
        }

        return switch (filled) {
            case 0 -> 0;
            case 1 -> 2;
            case 2 -> 4;
            case 3 -> 6;
            case 4 -> 8;
            case 5 -> 10;
            case 6 -> 12;
            case 7 -> 15;
            default -> 0;
        };
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RecordRackBlockEntity(pos, state);
    }
}
