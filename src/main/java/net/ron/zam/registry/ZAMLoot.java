package net.ron.zam.registry;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.ron.zam.ZAMMod;

import java.util.Set;

public final class ZAMLoot {
    private static final Set<ResourceKey<LootTable>> CASE_CHEST_TABLES = Set.of(
            key("archaeology/trail_ruins_rare"),
            key("chests/bastion_hoglin_stable"),
            key("chests/igloo_chest"),
            key("chests/jungle_temple"),
            key("chests/woodland_mansion"),
            key("chests/stronghold_library"),
            key("chests/shipwreck_supply"),
            key("chests/simple_dungeon"),
            key("chests/nether_bridge"),
            key("chests/trial_chambers/reward_ominous_rare"),
            key("chests/ancient_city"),
            key("chests/end_city_treasure"),
            key("chests/pillager_outpost")
    );

    private static ResourceKey<LootTable> key(String path) {
        return ResourceKey.create(
                Registries.LOOT_TABLE,
                Identifier.withDefaultNamespace(path)
        );
    }

    public static void registerLoot() {
        ZAMMod.LOGGER.info("Registering Loot for {}", ZAMMod.MOD_ID);

        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (!source.isBuiltin() || !CASE_CHEST_TABLES.contains(key)) {
                return;
            }

            ZAMMod.LOGGER.info("Injecting cases into loot table: {}", key.identifier());

            tableBuilder.pool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .when(LootItemRandomChanceCondition.randomChance(0.34F))
                    .add(TagEntry.expandTag(ZAMTags.CASES))
                    .build())
                    .pool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                            .when(LootItemRandomChanceCondition.randomChance(0.20F))
                            .add(LootItem.lootTableItem(ZAMItems.CASE_KEY))
                            .build());
        });
    }
}