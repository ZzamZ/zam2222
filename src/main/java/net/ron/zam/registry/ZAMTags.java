package net.ron.zam.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.ron.zam.ZAMMod;

public class ZAMTags {
    public static final TagKey<Item> CASES = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ZAMMod.MOD_ID,"cases"));
    public static final TagKey<Item> CASTLE_CRASHERS_MUSIC_DISCS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ZAMMod.MOD_ID,"castle_crashers_music_discs"));
    public static final TagKey<Item> DELTARUNE_MUSIC_DISCS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ZAMMod.MOD_ID,"deltarune_music_discs"));
    public static final TagKey<Item> DRAGON_BALL_MUSIC_DISCS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ZAMMod.MOD_ID,"dragon_ball_music_discs"));
    public static final TagKey<Item> HXH_MUSIC_DISCS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ZAMMod.MOD_ID,"hxh_music_discs"));
    public static final TagKey<Item> OMORI_MUSIC_DISCS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ZAMMod.MOD_ID,"omori_music_discs"));
    public static final TagKey<Item> SPONGEBOB_MUSIC_DISCS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ZAMMod.MOD_ID,"spongebob_music_discs"));
    public static final TagKey<Item> STARDEW_VALLEY_MUSIC_DISCS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ZAMMod.MOD_ID,"stardew_valley_music_discs"));

    public static final TagKey<Item> MARINE_REPAIRABLE = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ZAMMod.MOD_ID,"marine_repairable"));
    public static final TagKey<Block> INCORRECT_FOR_MARINE_TOOL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(ZAMMod.MOD_ID,"incorrect_for_marine_tool"));
}
