package net.ron.zam;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.ron.zam.datagen.*;
import net.ron.zam.registry.ZAMJukeboxSongs;
import net.ron.zam.registry.ZAMVillagerTrades;

public class ZAMModDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ZAMModelProvider::new);
        pack.addProvider(ZAMRegistryDataProvider::new);
        pack.addProvider(ZAMCaseProvider::new);

        pack.addProvider(ZAMBlockTagProvider::new);
        pack.addProvider(ZAMItemTagProvider::new);

        pack.addProvider(ZAMLootTableProvider::new);
        pack.addProvider(ZAMRecipeProvider::new);
        pack.addProvider(ZAMVillagerTradeTagProvider::new);
        pack.addProvider(ZAMEnchantmentProvider::new);
        pack.addProvider(ZAMWolfVariantProvider::new);


    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.JUKEBOX_SONG, ZAMJukeboxSongs::bootstrap);
        registryBuilder.add(Registries.VILLAGER_TRADE, ZAMVillagerTrades::bootstrap);
        registryBuilder.add(Registries.ENCHANTMENT, ZAMEnchantmentProvider::bootstrap);
        registryBuilder.add(Registries.WOLF_VARIANT, ZAMWolfVariantProvider::bootstrap);
    }
}
