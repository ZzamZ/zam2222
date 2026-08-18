package net.ron.zam.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.ron.zam.registry.ZAMBlocks;

import java.util.concurrent.CompletableFuture;

public class ZAMLootTableProvider extends FabricBlockLootSubProvider {
    public ZAMLootTableProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture);
    }

    @Override
    public void generate() {
        dropSelf(ZAMBlocks.OAK_RECORD_RACK);
        dropSelf(ZAMBlocks.SPRUCE_RECORD_RACK);
        dropSelf(ZAMBlocks.BIRCH_RECORD_RACK);
        dropSelf(ZAMBlocks.JUNGLE_RECORD_RACK);
        dropSelf(ZAMBlocks.ACACIA_RECORD_RACK);
        dropSelf(ZAMBlocks.DARK_OAK_RECORD_RACK);
        dropSelf(ZAMBlocks.MANGROVE_RECORD_RACK);
        dropSelf(ZAMBlocks.CHERRY_RECORD_RACK);
        dropSelf(ZAMBlocks.PALE_OAK_RECORD_RACK);
        dropSelf(ZAMBlocks.BAMBOO_RECORD_RACK);
        dropSelf(ZAMBlocks.CRIMSON_RECORD_RACK);
        dropSelf(ZAMBlocks.WARPED_RECORD_RACK);
    }
}
