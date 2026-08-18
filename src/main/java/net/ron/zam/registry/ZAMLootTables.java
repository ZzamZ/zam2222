package net.ron.zam.registry;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.entity.FishingHookPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.ron.zam.ZAMMod;

public class ZAMLootTables {

    public static final ResourceKey<LootTable> FISH = register("gameplay/fishing/fish");
    public static final ResourceKey<LootTable> JUNK = register("gameplay/fishing/junk");
    public static final ResourceKey<LootTable> TREASURE_POUCH = register("gameplay/fishing/treasure_pouch");
    public static final ResourceKey<LootTable> MARINERS_FORTUNE = register("gameplay/fishing/mariners_fortune");
    public static final ResourceKey<LootTable> MARINE = register("gameplay/fishing/marine");

    private static ResourceKey<LootTable> register(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, ZAMMod.id(path));
    }

    public static void registerLootTables() {
        LootTableEvents.MODIFY.register((key, table, source, registries) -> {
            if (!key.equals(BuiltInLootTables.FISHING)) {
                return;
            }

            table.modifyPools(pool -> {
                pool.add(NestedLootTable.lootTableReference(FISH).setWeight(85).setQuality(-1));
                pool.add(NestedLootTable.lootTableReference(JUNK).setWeight(10).setQuality(-2));
                pool.add(NestedLootTable.lootTableReference(TREASURE_POUCH).setWeight(5).setQuality(1).when(openWater()));
                pool.add(NestedLootTable.lootTableReference(MARINERS_FORTUNE).setWeight(1).setQuality(2).when(openWater()));
                pool.add(NestedLootTable.lootTableReference(MARINE).setWeight(1).setQuality(2).when(openWater()));
            });
        });
    }

    private static LootItemEntityPropertyCondition.Builder openWater() {
        return LootItemEntityPropertyCondition.hasProperties(
                LootContext.EntityTarget.THIS,
                EntityPredicate.Builder.entity()
                        .fishingHook(FishingHookPredicate.inOpenWater(true))
                        .build()
        );
    }
}