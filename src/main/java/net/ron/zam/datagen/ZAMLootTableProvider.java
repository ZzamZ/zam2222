package net.ron.zam.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.ron.zam.common.block.CornCropBlock;
import net.ron.zam.registry.ZAMBlocks;
import net.ron.zam.registry.ZAMItems;

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

        add(ZAMBlocks.CORN, LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(ZAMItems.CORN_KERNELS)))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ZAMBlocks.CORN)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CornCropBlock.AGE, 3))).add(LootItem.lootTableItem(ZAMItems.CORN)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                        .apply(ApplyBonusCount.addBonusBinomialDistributionCount(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE), 0.25F, 1))))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ZAMBlocks.CORN)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CornCropBlock.AGE, 3))).add(LootItem.lootTableItem(ZAMItems.CORN_KERNELS)
                        .apply(ApplyBonusCount.addBonusBinomialDistributionCount(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3)))));
    }
}
