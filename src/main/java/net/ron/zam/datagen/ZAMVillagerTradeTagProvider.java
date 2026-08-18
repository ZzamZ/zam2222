package net.ron.zam.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.VillagerTradeTags;
import net.minecraft.world.item.trading.VillagerTrade;
import net.ron.zam.registry.ZAMVillagerTrades;

import java.util.concurrent.CompletableFuture;

public class ZAMVillagerTradeTagProvider extends FabricTagsProvider<VillagerTrade> {
    public ZAMVillagerTradeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, Registries.VILLAGER_TRADE, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        getOrCreateRawBuilder(VillagerTradeTags.CARTOGRAPHER_LEVEL_5)
                .add(TagEntry.element(ZAMVillagerTrades.CARTOGRAPHER_5_EMERALD_CASTLE_CRASHERS_CASE.identifier()))
                .add(TagEntry.element(ZAMVillagerTrades.CARTOGRAPHER_5_EMERALD_DELTARUNE_CASE.identifier()))
                .add(TagEntry.element(ZAMVillagerTrades.CARTOGRAPHER_5_EMERALD_DRAGON_BALL_CASE.identifier()))
                .add(TagEntry.element(ZAMVillagerTrades.CARTOGRAPHER_5_EMERALD_HXH_CASE.identifier()))
                .add(TagEntry.element(ZAMVillagerTrades.CARTOGRAPHER_5_EMERALD_OMORI_CASE.identifier()))
                .add(TagEntry.element(ZAMVillagerTrades.CARTOGRAPHER_5_EMERALD_SPONGEBOB_CASE.identifier()))
                .add(TagEntry.element(ZAMVillagerTrades.CARTOGRAPHER_5_EMERALD_STARDEW_VALLEY_CASE.identifier()))

                .add(TagEntry.element(ZAMVillagerTrades.WANDERING_TRADER_RARE_EMERALD_CASE_KEY.identifier()));
    }
}

