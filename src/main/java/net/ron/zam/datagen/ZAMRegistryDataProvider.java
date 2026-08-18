package net.ron.zam.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;

import java.util.concurrent.CompletableFuture;

public class ZAMRegistryDataProvider extends FabricDynamicRegistryProvider {
    public ZAMRegistryDataProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        entries.addAll(provider.lookupOrThrow(Registries.JUKEBOX_SONG));
        entries.addAll(provider.lookupOrThrow(Registries.VILLAGER_TRADE));
        entries.addAll(provider.lookupOrThrow(Registries.ENCHANTMENT));
        entries.addAll(provider.lookupOrThrow(Registries.WOLF_VARIANT));
    }

    @Override
    public String getName() {
        return "";
    }
}
