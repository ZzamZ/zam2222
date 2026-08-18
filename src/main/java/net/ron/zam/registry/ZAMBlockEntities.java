package net.ron.zam.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.ron.zam.ZAMMod;
import net.ron.zam.common.block.fortune.MarinersFortuneChestBlockEntity;
import net.ron.zam.common.block.projector.ProjectorBlockEntity;
import net.ron.zam.common.block.record_rack.RecordRackBlockEntity;
import net.ron.zam.common.block.television.TelevisionBlockEntity;

public class ZAMBlockEntities {

    public static final BlockEntityType<RecordRackBlockEntity> RECORD_RACK =
            register("record_rack", FabricBlockEntityTypeBuilder.create(RecordRackBlockEntity::new,
                    ZAMBlocks.OAK_RECORD_RACK, ZAMBlocks.SPRUCE_RECORD_RACK, ZAMBlocks.BIRCH_RECORD_RACK, ZAMBlocks.JUNGLE_RECORD_RACK,
                    ZAMBlocks.ACACIA_RECORD_RACK, ZAMBlocks.DARK_OAK_RECORD_RACK, ZAMBlocks.MANGROVE_RECORD_RACK, ZAMBlocks.CHERRY_RECORD_RACK,
                    ZAMBlocks.CRIMSON_RECORD_RACK, ZAMBlocks.WARPED_RECORD_RACK, ZAMBlocks.BAMBOO_RECORD_RACK, ZAMBlocks.PALE_OAK_RECORD_RACK
            ));

    public static final BlockEntityType<MarinersFortuneChestBlockEntity> MARINERS_FORTUNE_CHEST = register("mariners_bounty_chest", FabricBlockEntityTypeBuilder.create(MarinersFortuneChestBlockEntity::new, ZAMBlocks.MARINERS_FORTUNE));
    public static final BlockEntityType<TelevisionBlockEntity> TELEVISION = register("television", FabricBlockEntityTypeBuilder.create(TelevisionBlockEntity::new, ZAMBlocks.TELEVISION));
    public static final BlockEntityType<ProjectorBlockEntity> PROJECTOR = register("projector", FabricBlockEntityTypeBuilder.create(ProjectorBlockEntity::new, ZAMBlocks.PROJECTOR));

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, FabricBlockEntityTypeBuilder<T> builder) {
        BlockEntityType<T> type = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ZAMMod.id(name), builder.build());
        return type;
    }

    public static void registerBlockEntities() {
        ZAMMod.LOGGER.info("Registering Block Entities for {}", ZAMMod.MOD_ID);
    }
}
