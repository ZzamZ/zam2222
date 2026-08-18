package net.ron.zam.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.ron.zam.registry.ZAMBlocks;
import net.ron.zam.registry.ZAMTags;

import java.util.concurrent.CompletableFuture;

public class ZAMBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {
    public ZAMBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ZAMBlocks.OAK_RECORD_RACK.properties().blockIdOrThrow())
                .add(ZAMBlocks.SPRUCE_RECORD_RACK.properties().blockIdOrThrow())
                .add(ZAMBlocks.BIRCH_RECORD_RACK.properties().blockIdOrThrow())
                .add(ZAMBlocks.JUNGLE_RECORD_RACK.properties().blockIdOrThrow())
                .add(ZAMBlocks.ACACIA_RECORD_RACK.properties().blockIdOrThrow())
                .add(ZAMBlocks.DARK_OAK_RECORD_RACK.properties().blockIdOrThrow())
                .add(ZAMBlocks.MANGROVE_RECORD_RACK.properties().blockIdOrThrow())
                .add(ZAMBlocks.CHERRY_RECORD_RACK.properties().blockIdOrThrow())
                .add(ZAMBlocks.PALE_OAK_RECORD_RACK.properties().blockIdOrThrow())
                .add(ZAMBlocks.BAMBOO_RECORD_RACK.properties().blockIdOrThrow())
                .add(ZAMBlocks.CRIMSON_RECORD_RACK.properties().blockIdOrThrow())
                .add(ZAMBlocks.WARPED_RECORD_RACK.properties().blockIdOrThrow())
                .add(ZAMBlocks.TELEVISION.properties().blockIdOrThrow());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ZAMBlocks.MARINERS_FORTUNE.properties().blockIdOrThrow());

        tag(ZAMTags.INCORRECT_FOR_MARINE_TOOL)
                .addOptionalTag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL);
    }

    @Override
    public String getName() {
        return "ZAM Block Tags";
    }
}
