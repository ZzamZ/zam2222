package net.ron.zam.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.ron.zam.ZAMMod;
import net.ron.zam.common.block.MarineFarmlandBlock;
import net.ron.zam.common.block.CornCropBlock;
import net.ron.zam.common.block.fortune.MarinersFortuneChestBlock;
import net.ron.zam.common.block.projector.ProjectorBlock;
import net.ron.zam.common.block.record_rack.RecordRackBlock;
import net.ron.zam.common.block.television.TelevisionBlock;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ZAMBlocks {

    public static final Block MARINERS_FORTUNE = registerBlock("mariners_fortune", properties -> new MarinersFortuneChestBlock(() -> ZAMBlockEntities.MARINERS_FORTUNE_CHEST, properties.mapColor(MapColor.METAL).strength(3.0F, 6.0F).sound(SoundType.METAL)), properties -> properties.rarity(Rarity.RARE));
    public static final Block MARINE_FARMLAND = registerBlock("marine_farmland", properties -> new MarineFarmlandBlock(properties.mapColor(MapColor.DIRT).randomTicks().strength(0.6F).sound(SoundType.GRAVEL).isViewBlocking(Blocks::never).isSuffocating(Blocks::never)));
    public static final Block TELEVISION = registerBlock("television", properties -> new TelevisionBlock(properties.mapColor(MapColor.COLOR_GRAY).strength(1.5F).sound(SoundType.WOOD).lightLevel(state -> state.getValue(TelevisionBlock.POWER_STATE).isOn() ? 3 : 0)));
    public static final Block PROJECTOR = registerBlock("projector", properties -> new ProjectorBlock(properties.mapColor(MapColor.METAL).strength(2.0F).sound(SoundType.METAL).noOcclusion()));

    public static final Block CORN = registerBlockWithoutItem("corn",
            properties -> new CornCropBlock(properties
                    .noCollision()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.CROP)));
    //Record Racks
    public static final Block OAK_RECORD_RACK = registerBlock("oak_record_rack", properties -> new RecordRackBlock(properties.overrideLootTable(Blocks.OAK_FENCE.getLootTable()).strength(1.0F).sound(SoundType.WOOD)));
    public static final Block SPRUCE_RECORD_RACK = registerBlock("spruce_record_rack", properties -> new RecordRackBlock(properties.overrideLootTable(Blocks.SPRUCE_FENCE.getLootTable()).strength(1.0F).sound(SoundType.WOOD)));
    public static final Block BIRCH_RECORD_RACK = registerBlock("birch_record_rack", properties -> new RecordRackBlock(properties.overrideLootTable(Blocks.BIRCH_FENCE.getLootTable()).strength(1.0F).sound(SoundType.WOOD)));
    public static final Block DARK_OAK_RECORD_RACK = registerBlock("dark_oak_record_rack", properties -> new RecordRackBlock(properties.overrideLootTable(Blocks.DARK_OAK_FENCE.getLootTable()).strength(1.0F).sound(SoundType.WOOD)));
    public static final Block MANGROVE_RECORD_RACK = registerBlock("mangrove_record_rack", properties -> new RecordRackBlock(properties.overrideLootTable(Blocks.MANGROVE_FENCE.getLootTable()).strength(1.0F).sound(SoundType.WOOD)));
    public static final Block CHERRY_RECORD_RACK = registerBlock("cherry_record_rack", properties -> new RecordRackBlock(properties.overrideLootTable(Blocks.CHERRY_FENCE.getLootTable()).strength(1.0F).sound(SoundType.CHERRY_WOOD)));
    public static final Block JUNGLE_RECORD_RACK = registerBlock("jungle_record_rack", properties -> new RecordRackBlock(properties.overrideLootTable(Blocks.JUNGLE_FENCE.getLootTable()).strength(1.0F).sound(SoundType.WOOD)));
    public static final Block ACACIA_RECORD_RACK = registerBlock("acacia_record_rack", properties -> new RecordRackBlock(properties.overrideLootTable(Blocks.ACACIA_FENCE.getLootTable()).strength(1.0F).sound(SoundType.WOOD)));
    public static final Block CRIMSON_RECORD_RACK = registerBlock("crimson_record_rack", properties -> new RecordRackBlock(properties.overrideLootTable(Blocks.CRIMSON_FENCE.getLootTable()).strength(1.0F).sound(SoundType.WOOD)));
    public static final Block BAMBOO_RECORD_RACK = registerBlock("bamboo_record_rack", properties -> new RecordRackBlock(properties.overrideLootTable(Blocks.BAMBOO_FENCE.getLootTable()).strength(1.0F).sound(SoundType.BAMBOO_WOOD)));
    public static final Block WARPED_RECORD_RACK = registerBlock("warped_record_rack", properties -> new RecordRackBlock(properties.overrideLootTable(Blocks.WARPED_FENCE.getLootTable()).strength(1.0F).sound(SoundType.WOOD)));
    public static final Block PALE_OAK_RECORD_RACK = registerBlock("pale_oak_record_rack", properties -> new RecordRackBlock(properties.overrideLootTable(Blocks.PALE_OAK_FENCE.getLootTable()).strength(1.0F).sound(SoundType.WOOD)));

    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> function) {
        return registerBlock(name, function, properties -> properties);
    }

    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> function, UnaryOperator<Item.Properties> itemProperties) {
        Block toRegister = function.apply(BlockBehaviour.Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(ZAMMod.MOD_ID, name))));
        registerBlockItem(name, toRegister, itemProperties);
        return Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(ZAMMod.MOD_ID, name), toRegister);
    }

    private static void registerBlockItem(String name, Block block, UnaryOperator<Item.Properties> itemProperties) {
        Item.Properties properties = new Item.Properties().useBlockDescriptionPrefix()
                .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ZAMMod.MOD_ID, name)));

        Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(ZAMMod.MOD_ID, name),
                new BlockItem(block, itemProperties.apply(properties)));
    }

    private static Block registerBlockWithoutItem(String name, Function<BlockBehaviour.Properties, Block> function) {
        Block block = function.apply(BlockBehaviour.Properties.of()
                .setId(ResourceKey.create(
                        Registries.BLOCK,
                        Identifier.fromNamespaceAndPath(ZAMMod.MOD_ID, name)
                )));

        return Registry.register(
                BuiltInRegistries.BLOCK,
                Identifier.fromNamespaceAndPath(ZAMMod.MOD_ID, name),
                block
        );
    }

    public static void registerBlocks() {
        ZAMMod.LOGGER.info("Registering Blocks for " + ZAMMod.MOD_ID);
    }
}
