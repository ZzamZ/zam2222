package net.ron.zam.common.block.projector;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.ron.zam.common.gui.ProjectorScreen;
import net.ron.zam.common.item.VideoTapeItem;
import net.ron.zam.registry.ZAMItems;
import org.jetbrains.annotations.Nullable;

public class ProjectorBlock extends BaseEntityBlock {
    public static final MapCodec<ProjectorBlock> CODEC = simpleCodec(ProjectorBlock::new);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    public ProjectorBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ProjectorBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hit) {
        if (!stack.is(ZAMItems.VIDEO_TAPE))
            return InteractionResult.PASS;

        if (level.isClientSide()) {
            var media = VideoTapeItem.getMedia(stack);

            ProjectorScreen.open(
                    hand,
                    media != null ? media.url() : "",
                    media != null ? media.volume() : 1.0F
            );
        }

        return InteractionResult.SUCCESS;
    }
}