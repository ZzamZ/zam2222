package net.ron.zam.common.block.projector;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.ron.zam.registry.ZAMBlockEntities;

public class ProjectorBlockEntity extends BlockEntity {
    public ProjectorBlockEntity(BlockPos pos, BlockState state) {
        super(ZAMBlockEntities.PROJECTOR, pos, state);
    }

    public boolean isPowered() {
        return getBlockState().getValue(ProjectorBlock.POWERED);
    }
}