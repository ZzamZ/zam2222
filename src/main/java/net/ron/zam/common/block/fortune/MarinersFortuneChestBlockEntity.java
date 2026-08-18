package net.ron.zam.common.block.fortune;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.ron.zam.registry.ZAMBlockEntities;

public class MarinersFortuneChestBlockEntity extends ChestBlockEntity {

    public MarinersFortuneChestBlockEntity(BlockPos pos, BlockState state) {
        super(ZAMBlockEntities.MARINERS_FORTUNE_CHEST, pos, state);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("menu." + "zam" + ".mariners_fortune");
    }
}
