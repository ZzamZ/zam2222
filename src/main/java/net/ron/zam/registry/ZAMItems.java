package net.ron.zam.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.ron.zam.ZAMMod;
import net.ron.zam.common.item.*;
import net.ron.zam.common.item.caserewards.*;
import net.ron.zam.common.item.tools.*;

import java.util.function.Function;

public class ZAMItems {

    public static final Item GOLD_ICON = registerItem("gold_icon", s -> new Item(s.rarity(Rarity.EPIC)));
    public static final Item RED_ICON = registerItem("red_icon", s -> new Item(s.rarity(Rarity.EPIC)));

    public static final Item MUSIC_BOX = registerItem("music_box", MusicBoxItem::new);
    public static final Item RECORD_SLEEVE = registerItem("record_sleeve", RecordSleeveItem::new);
    public static final Item CASE_KEY = registerItem("case_key", s -> new Item(s.rarity(Rarity.UNCOMMON)));
    public static final Item CASSETTE = registerItem("cassette", s -> new CassetteItem(s.stacksTo(1).component(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT.withHidden(DataComponents.DYED_COLOR, true))));
    public static final Item CASE = registerItem("case", s -> new CaseItem(s.rarity(Rarity.UNCOMMON)));
    public static final Item VIDEO_TAPE = registerItem("video_tape", properties -> new VideoTapeItem(properties.stacksTo(1)));

    //Corn
    public static final Item CORN_KERNELS = registerItem("corn_kernels", properties -> new BlockItem(ZAMBlocks.CORN, properties));
    public static final Item CORN = registerItem("corn", properties -> new Item(properties.food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3F).build())));
    public static final Item POPCORN = registerItem("popcorn", properties -> new Item(properties.food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.4F).build()).component(DataComponents.CONSUMABLE, Consumables.defaultFood().consumeSeconds(0.8F).build())));
    public static final Item CORN_ON_THE_COB = registerItem("corn_on_the_cob", properties -> new Item(properties.food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.6F).build())));

    //Fishing
    public static final Item FISHERMAN_MASTERY_CAP = registerItem("fish_cap", s -> new HatItem(s.stacksTo(1).rarity(Rarity.EPIC), "Fishing Mastery", ZAMSounds.FISH_CAP_EQUIP));
    public static final Item WOOD_MEDAL = registerItem("wood_medal", s -> new RewardItem("Fishing Mastery", s.rarity(Rarity.COMMON)));
    public static final Item BRONZE_MEDAL = registerItem("bronze_medal", s -> new RewardItem("Fishing Mastery", s.rarity(Rarity.COMMON)));
    public static final Item SILVER_MEDAL = registerItem("silver_medal", s -> new RewardItem("Fishing Mastery", s.rarity(Rarity.UNCOMMON)));
    public static final Item GOLD_MEDAL = registerItem("gold_medal", s -> new RewardItem("Fishing Mastery", s.rarity(Rarity.RARE)));
    public static final Item LEGENDARY_MEDAL = registerItem("legendary_medal", s -> new RewardItem("Fishing Mastery", s.rarity(Rarity.EPIC)));

    public static final Item SEA_JELLY = registerItem("sea_jelly", SeaJellyItem::new);
    public static final Item MESSAGE_IN_A_BOTTLE = registerItem("message_in_a_bottle", s -> new MessageInABottleItem(s.rarity(Rarity.UNCOMMON).component(ZAMComponents.SECRET_MESSAGE, 0)));
    public static final Item SECRET_MESSAGE = registerItem("secret_message", s -> new SecretMessageItem(s.rarity(Rarity.UNCOMMON).component(ZAMComponents.SECRET_MESSAGE, 0)));
    public static final Item TREASURE_POUCH = registerItem("treasure_pouch", s -> new LootBoxItem(ZAMLootTables.TREASURE_POUCH, s.rarity(Rarity.UNCOMMON)));

    //Marine
    public static final Item MARINE_SWORD = registerItem("marine_sword", s -> new MarineSwordItem(s.rarity(Rarity.RARE).sword(ZAMToolTiers.MARINE, 3.0F, -2.4F)));
    public static final Item MARINE_PICKAXE = registerItem("marine_pickaxe", s -> new MarinePickaxeItem(s.rarity(Rarity.RARE).pickaxe(ZAMToolTiers.MARINE, 1.0F, -2.8F)));
    public static final Item MARINE_AXE = registerItem("marine_axe", s -> new MarineAxeItem(s.rarity(Rarity.RARE).axe(ZAMToolTiers.MARINE, 5.0F, -3.0F)));
    public static final Item MARINE_SHOVEL = registerItem("marine_shovel", s -> new MarineShovelItem(s.rarity(Rarity.RARE).shovel(ZAMToolTiers.MARINE, 1.5F, -3.0F)));
    public static final Item MARINE_HOE = registerItem("marine_hoe", s -> new MarineHoeItem(s.rarity(Rarity.RARE).hoe(ZAMToolTiers.MARINE, -3.0F, 0.0F)));
    public static final Item MARINE_SPEAR = registerItem("marine_spear", s -> new MarineSpearItem(s.rarity(Rarity.RARE).spear(ZAMToolTiers.MARINE, 1.15F, 1.2F, 0.4F, 2.5F, 7.0F, 5.5F, 5.1F, 8.75F, 4.6F)));

    // Castle Crashers
    public static final Item CASTLE_CRASHERS_CHICKEN_SWORD = registerItem("castle_crashers_chicken_sword", s -> new CaseRewardItem("Castle Crashers", s.sword(ToolMaterial.NETHERITE, 3, -2.4f).rarity(Rarity.EPIC).fireResistant()));
    public static final Item CASTLE_CRASHERS_DEMON_SWORD = registerItem("castle_crashers_demon_sword", s -> new CaseRewardItem("Castle Crashers", s.sword(ToolMaterial.NETHERITE, 3, -2.4f).rarity(Rarity.EPIC).fireResistant()));
    public static final Item CASTLE_CRASHERS_UNICORN_SWORD = registerItem("castle_crashers_unicorn_sword", s -> new CaseRewardItem("Castle Crashers", s.sword(ToolMaterial.NETHERITE, 3, -2.4f).rarity(Rarity.EPIC).fireResistant()));
    public static final Item CASTLE_CRASHERS_RED_KNIGHT_HELMET = registerItem("castle_crashers_red_knight_helmet", s -> new HatItem(s.stacksTo(1).humanoidArmor(ArmorMaterials.NETHERITE, ArmorType.HELMET).rarity(Rarity.RARE).humanoidArmor(ArmorMaterials.NETHERITE, ArmorType.HELMET).fireResistant(), "Castle Crashers", ZAMSounds.RED_KNIGHT_EQUIP));
    public static final Item CASTLE_CRASHERS_ORANGE_KNIGHT_HELMET = registerItem("castle_crashers_orange_knight_helmet", s -> new HatItem(s.stacksTo(1).humanoidArmor(ArmorMaterials.NETHERITE, ArmorType.HELMET).rarity(Rarity.RARE).humanoidArmor(ArmorMaterials.NETHERITE, ArmorType.HELMET).fireResistant(), "Castle Crashers", ZAMSounds.ORANGE_KNIGHT_EQUIP));
    public static final Item CASTLE_CRASHERS_BLUE_KNIGHT_HELMET = registerItem("castle_crashers_blue_knight_helmet", s -> new HatItem(s.stacksTo(1).humanoidArmor(ArmorMaterials.NETHERITE, ArmorType.HELMET).rarity(Rarity.RARE).humanoidArmor(ArmorMaterials.NETHERITE, ArmorType.HELMET).fireResistant(), "Castle Crashers", ZAMSounds.BLUE_KNIGHT_EQUIP));
    public static final Item CASTLE_CRASHERS_GREEN_KNIGHT_HELMET = registerItem("castle_crashers_green_knight_helmet", s -> new HatItem(s.stacksTo(1).humanoidArmor(ArmorMaterials.NETHERITE, ArmorType.HELMET).rarity(Rarity.RARE).humanoidArmor(ArmorMaterials.NETHERITE, ArmorType.HELMET).fireResistant(), "Castle Crashers", ZAMSounds.GREEN_KNIGHT_EQUIP));
    public static final Item CASTLE_CRASHERS_CHAMPIONS_HORN = registerItem("castle_crashers_champions_horn", s -> new RewardItem("Castle Crashers", s.rarity(Rarity.EPIC)));
    public static final Item CASTLE_CRASHERS_FOUR_BRAVE_CHAMPS = registerItem("castle_crashers_four_brave_champs", s -> new CaseRewardItem("Castle Crashers", s.jukeboxPlayable(ZAMJukeboxSongs.CASTLE_CRASHERS_FOUR_BRAVE_CHAMPS).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item CASTLE_CRASHERS_FLUTEY = registerItem("castle_crashers_flutey", s -> new CaseRewardItem("Castle Crashers", s.jukeboxPlayable(ZAMJukeboxSongs.CASTLE_CRASHERS_FLUTEY).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item CASTLE_CRASHERS_SPANISH_WALTZ = registerItem("castle_crashers_spanish_waltz", s -> new CaseRewardItem("Castle Crashers", s.jukeboxPlayable(ZAMJukeboxSongs.CASTLE_CRASHERS_SPANISH_WALTZ).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item CASTLE_CRASHERS_RACE_AROUND_THE_WORLD = registerItem("castle_crashers_race_around_the_world", s -> new CaseRewardItem("Castle Crashers", s.jukeboxPlayable(ZAMJukeboxSongs.CASTLE_CRASHERS_RACE_AROUND_THE_WORLD).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item CASTLE_CRASHERS_JUMPER = registerItem("castle_crashers_jumper", s -> new CaseRewardItem("Castle Crashers", s.jukeboxPlayable(ZAMJukeboxSongs.CASTLE_CRASHERS_JUMPER).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item CASTLE_CRASHERS_SPACE_PIRATES = registerItem("castle_crashers_space_pirates", s -> new CaseRewardItem("Castle Crashers", s.jukeboxPlayable(ZAMJukeboxSongs.CASTLE_CRASHERS_SPACE_PIRATES).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item CASTLE_CRASHERS_THE_SHOW = registerItem("castle_crashers_the_show", s -> new CaseRewardItem("Castle Crashers", s.jukeboxPlayable(ZAMJukeboxSongs.CASTLE_CRASHERS_THE_SHOW).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item CASTLE_CRASHERS_SIMPLE_SIGHT = registerItem("castle_crashers_simple_sight", s -> new CaseRewardItem("Castle Crashers", s.jukeboxPlayable(ZAMJukeboxSongs.CASTLE_CRASHERS_SIMPLE_SIGHT).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item CASTLE_CRASHERS_FINAL_CONFRONTATION = registerItem("castle_crashers_final_confrontation", s -> new CaseRewardItem("Castle Crashers", s.jukeboxPlayable(ZAMJukeboxSongs.CASTLE_CRASHERS_FINAL_CONFRONTATION).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item CASTLE_CRASHERS_ARCHETYPE = registerItem("castle_crashers_archetype", s -> new CaseRewardItem("Castle Crashers", s.jukeboxPlayable(ZAMJukeboxSongs.CASTLE_CRASHERS_ARCHETYPE).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item CASTLE_CRASHERS_DARK_SKIES = registerItem("castle_crashers_dark_skies", s -> new CaseRewardItem("Castle Crashers", s.jukeboxPlayable(ZAMJukeboxSongs.CASTLE_CRASHERS_DARK_SKIES).stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item CASTLE_CRASHERS_RAGE_CHAMPIONS = registerItem("castle_crashers_rage_champions", s -> new CaseRewardItem("Castle Crashers", s.jukeboxPlayable(ZAMJukeboxSongs.CASTLE_CRASHERS_RAGE_CHAMPIONS).stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item CASTLE_CRASHERS_BATTLEBLOCK = registerItem("castle_crashers_battleblock", s -> new CaseRewardItem("Castle Crashers", s.jukeboxPlayable(ZAMJukeboxSongs.CASTLE_CRASHERS_BATTLEBLOCK).stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item CASTLE_CRASHERS_PLEASE_DONT = registerItem("castle_crashers_please_dont", s -> new CaseRewardItem("Castle Crashers", s.jukeboxPlayable(ZAMJukeboxSongs.CASTLE_CRASHERS_PLEASE_DONT).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item CASTLE_CRASHERS_THE_ABDUCTION = registerItem("castle_crashers_the_abduction", s -> new CaseRewardItem("Castle Crashers", s.jukeboxPlayable(ZAMJukeboxSongs.CASTLE_CRASHERS_THE_ABDUCTION).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item CASTLE_CRASHERS_WINTER_BLISS = registerItem("castle_crashers_winter_bliss", s -> new CaseRewardItem("Castle Crashers", s.jukeboxPlayable(ZAMJukeboxSongs.CASTLE_CRASHERS_WINTER_BLISS).stacksTo(1).rarity(Rarity.UNCOMMON)));

    //Deltarune
    public static final Item DELTARUNE_BLACK_KNIFE_SWORD = registerItem("deltarune_black_knife_sword", s -> new CaseRewardItem("Deltarune", s.sword(ToolMaterial.NETHERITE, 3, -2.4f).rarity(Rarity.EPIC).fireResistant()));
    public static final Item DELTARUNE_MANE_AXE = registerItem("deltarune_mane_axe", s -> new CaseRewardAxeItem("Deltarune", ToolMaterial.NETHERITE, 5.0f, -3.0f, s.rarity(Rarity.EPIC).fireResistant()));
    public static final Item DELTARUNE_HOLY_HALBERD = registerItem("deltarune_holy_halberd", s -> new CaseRewardAxeItem("Deltarune", ToolMaterial.NETHERITE, 5.0f, -3.0f, s.rarity(Rarity.EPIC).fireResistant()));
    public static final Item DELTARUNE_TENNA_HEAD = registerItem("deltarune_tenna_head", s -> new HatItem(s.stacksTo(1).humanoidArmor(ArmorMaterials.NETHERITE, ArmorType.HELMET).rarity(Rarity.EPIC), "Deltarune", ZAMSounds.DELTARUNE_EQUIP));
    public static final Item DELTARUNE_THE_DELTARUNE = registerItem("deltarune_the_deltarune", s -> new RewardItem("Deltarune", s.rarity(Rarity.EPIC)));
    public static final Item DELTARUNE_LANTERN = registerItem("deltarune_lantern", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_LANTERN).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item DELTARUNE_FIELD_OF_HOPES_AND_DREAMS = registerItem("deltarune_field_of_hopes_and_dreams", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_FIELD_OF_HOPES_AND_DREAMS).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item DELTARUNE_RUDE_BUSTER = registerItem("deltarune_rude_buster", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_RUDE_BUSTER).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item DELTARUNE_CHAOS_KING = registerItem("deltarune_chaos_king", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_CHAOS_KING).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item DELTARUNE_THE_WORLD_REVOLVING = registerItem("deltarune_the_world_revolving", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_THE_WORLD_REVOLVING).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item DELTARUNE_A_CYBERS_WORLD = registerItem("deltarune_a_cybers_world", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_A_CYBERS_WORLD).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item DELTARUNE_SMART_RACE = registerItem("deltarune_smart_race", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_SMART_RACE).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item DELTARUNE_PANDORA_PALACE = registerItem("deltarune_pandora_palace", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_PANDORA_PALACE).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item DELTARUNE_ATTACK_OF_THE_KILLER_QUEEN = registerItem("deltarune_attack_of_the_killer_queen", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_ATTACK_OF_THE_KILLER_QUEEN).stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item DELTARUNE_BIG_SHOT = registerItem("deltarune_big_shot", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_BIG_SHOT).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item DELTARUNE_PHYSICAL_CHALLENGE = registerItem("deltarune_physical_challenge", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_PHYSICAL_CHALLENGE).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item DELTARUNE_WELCOME_TO_THE_GREEN_ROOM = registerItem("deltarune_welcome_to_the_green_room", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_WELCOME_TO_THE_GREEN_ROOM).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item DELTARUNE_TV_WORLD = registerItem("deltarune_tv_world", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_TV_WORLD).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item DELTARUNE_ITS_TV_TIME = registerItem("deltarune_its_tv_time", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_ITS_TV_TIME).stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item DELTARUNE_BLACK_KNIFE = registerItem("deltarune_black_knife", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_BLACK_KNIFE).stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item DELTARUNE_CASTLE_FUNK = registerItem("deltarune_castle_funk", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_CASTLE_FUNK).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item DELTARUNE_FIREPLACE = registerItem("deltarune_fireplace", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_FIREPLACE).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item DELTARUNE_HAMMER_OF_JUSTICE = registerItem("deltarune_hammer_of_justice", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_HAMMER_OF_JUSTICE).stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item DELTARUNE_THE_THIRD_SANCTUARY = registerItem("deltarune_the_third_sanctuary", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_THE_THIRD_SANCTUARY).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item DELTARUNE_GUARDIAN = registerItem("deltarune_guardian", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_GUARDIAN).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item DELTARUNE_FLOWER_CASTLE = registerItem("deltarune_flower_castle", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_FLOWER_CASTLE).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item DELTARUNE_RUNNING_SKY = registerItem("deltarune_running_sky", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_RUNNING_SKY).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item DELTARUNE_SUNSET_OF_SEVEN_SUNS = registerItem("deltarune_sunset_of_seven_suns", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_SUNSET_OF_SEVEN_SUNS).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item DELTARUNE_THOUSAND_CAFE_ZUKAN = registerItem("deltarune_thousand_cafe_zukan", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_THOUSAND_CAFE_ZUKAN).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item DELTARUNE_FLOWER_MAN = registerItem("deltarune_flower_man", s -> new CaseRewardItem("Deltarune", s.jukeboxPlayable(ZAMJukeboxSongs.DELTARUNE_FLOWER_MAN).stacksTo(1).rarity(Rarity.EPIC)));

    //Dragon Ball
    public static final Item DRAGON_BALL_POWER_POLL = registerItem("dragon_ball_power_poll", s -> new CaseRewardItem("Dragon Ball", s.spear(ToolMaterial.NETHERITE, 1.15F, 1.2F, 0.4F, 2.5F, 7.0F, 5.5F, 5.1F, 8.75F, 4.6F).rarity(Rarity.EPIC).fireResistant()));
    public static final Item DRAGON_BALL_SICKLE_OF_SORROW = registerItem("dragon_ball_sickle_of_sorrow", s -> new CaseRewardHoeItem("Dragon Ball", ToolMaterial.NETHERITE, -4.0F, 0.0F, s.rarity(Rarity.EPIC).fireResistant()));
    public static final Item DRAGON_BALL_SUPER_SAIYAN_HAIR = registerItem("dragon_ball_super_saiyan_hair", s -> new HatItem(s.stacksTo(1).humanoidArmor(ArmorMaterials.NETHERITE, ArmorType.HELMET).rarity(Rarity.EPIC), "Dragon Ball", ZAMSounds.DRAGON_BALL_EQUIP));
    public static final Item DRAGON_BALL_GATEBREAKER = registerItem("dragon_ball_gatebreaker", s -> new CaseRewardItem("Dragon Ball", s.jukeboxPlayable(ZAMJukeboxSongs.DRAGON_BALL_GATEBREAKER).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item DRAGON_BALL_DAN_DAN = registerItem("dragon_ball_dan_dan", s -> new CaseRewardItem("Dragon Ball", s.jukeboxPlayable(ZAMJukeboxSongs.DRAGON_BALL_DAN_DAN).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item DRAGON_BALL_CHA_LA = registerItem("dragon_ball_cha_la", s -> new CaseRewardItem("Dragon Ball", s.jukeboxPlayable(ZAMJukeboxSongs.DRAGON_BALL_CHA_LA).stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item DRAGON_BALL_ULTRA_INSTINCT = registerItem("dragon_ball_ultra_instinct", s -> new CaseRewardItem("Dragon Ball", s.jukeboxPlayable(ZAMJukeboxSongs.DRAGON_BALL_ULTRA_INSTINCT).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item DRAGON_BALL_BROLY_VS_GOGETA = registerItem("dragon_ball_broly_vs_gogeta", s -> new CaseRewardItem("Dragon Ball", s.jukeboxPlayable(ZAMJukeboxSongs.DRAGON_BALL_BROLY_VS_GOGETA).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item DRAGON_BALL_GT_RECAP = registerItem("dragon_ball_gt_recap", s -> new CaseRewardItem("Dragon Ball", s.jukeboxPlayable(ZAMJukeboxSongs.DRAGON_BALL_GT_RECAP).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item DRAGON_BALL_THE_DRINK = registerItem("dragon_ball_the_drink", s -> new CaseRewardItem("Dragon Ball", s.jukeboxPlayable(ZAMJukeboxSongs.DRAGON_BALL_THE_DRINK).stacksTo(1).rarity(Rarity.RARE)));

    //Hunter X Hunter
    public static final Item HXH_GONS_FISHING_ROD = registerItem("hxh_gons_fishing_rod", s -> new CaseRewardFishingRodItem("Hunter X Hunter", s.stacksTo(1).rarity(Rarity.EPIC).durability(134).fireResistant()));
    public static final Item HXH_GONS_HAIR = registerItem("hxh_gons_hair", s -> new HatItem(s.stacksTo(1).humanoidArmor(ArmorMaterials.NETHERITE, ArmorType.HELMET).rarity(Rarity.EPIC), "Hunter X Hunter", ZAMSounds.HXH_EQUIP));
    public static final Item HXH_HUNTERS_LICENSE = registerItem("hxh_hunters_license", s -> new RewardItem("Hunter X Hunter", s.rarity(Rarity.EPIC)));
    public static final Item HXH_HUNTING_FOR_YOUR_DREAM = registerItem("hxh_hunting_for_your_dream", s -> new CaseRewardItem("Hunter X Hunter", s.jukeboxPlayable(ZAMJukeboxSongs.HXH_HUNTING_FOR_YOUR_DREAM).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item HXH_ALL_I_NEED_IS_MONEY = registerItem("hxh_all_i_need_is_money", s -> new CaseRewardItem("Hunter X Hunter", s.jukeboxPlayable(ZAMJukeboxSongs.HXH_ALL_I_NEED_IS_MONEY).stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item HXH_DEPARTURE = registerItem("hxh_departure", s -> new CaseRewardItem("Hunter X Hunter", s.jukeboxPlayable(ZAMJukeboxSongs.HXH_DEPARTURE).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item HXH_FROM_WHALE_ISLAND = registerItem("hxh_from_whale_island", s -> new CaseRewardItem("Hunter X Hunter", s.jukeboxPlayable(ZAMJukeboxSongs.HXH_FROM_WHALE_ISLAND).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item HXH_HISOKA_THEME = registerItem("hxh_hisoka_theme", s -> new CaseRewardItem("Hunter X Hunter", s.jukeboxPlayable(ZAMJukeboxSongs.HXH_HISOKA_THEME).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item HXH_WORLD_OF_ADVENTURES = registerItem("hxh_world_of_adventures", s -> new CaseRewardItem("Hunter X Hunter", s.jukeboxPlayable(ZAMJukeboxSongs.HXH_WORLD_OF_ADVENTURES).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item HXH_BOYS_BE_COURAGEOUS = registerItem("hxh_boys_be_courageous", s -> new CaseRewardItem("Hunter X Hunter", s.jukeboxPlayable(ZAMJukeboxSongs.HXH_BOYS_BE_COURAGEOUS).stacksTo(1).rarity(Rarity.UNCOMMON)));

    //Omori
    public static final Item OMORI_LOL_SWORD = registerItem("omori_lol_sword", s -> new CaseRewardItem("Omori", s.sword(ToolMaterial.NETHERITE, 3, -2.4f).rarity(Rarity.EPIC).fireResistant()));
    public static final Item OMORI_SPIKED_BAT = registerItem("omori_spiked_bat", s -> new CaseRewardItem("Omori", s.rarity(Rarity.EPIC)));
    public static final Item OMORI_FLOWER_CROWN = registerItem("omori_flower_crown", s -> new HatItem(s.stacksTo(1).humanoidArmor(ArmorMaterials.NETHERITE, ArmorType.HELMET).rarity(Rarity.EPIC), "Omori", ZAMSounds.OMORI_EQUIP));
    public static final Item OMORI_WHITESPACE_LIGHTBULB = registerItem("omori_whitespace_lightbulb", s -> new RewardItem("Omori", s.rarity(Rarity.EPIC)));
    public static final Item OMORI_TITLE = registerItem("omori_title", s -> new CaseRewardItem("Omori", s.jukeboxPlayable(ZAMJukeboxSongs.OMORI_TITLE).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item OMORI_BY_YOUR_SIDE = registerItem("omori_by_your_side", s -> new CaseRewardItem("Omori", s.jukeboxPlayable(ZAMJukeboxSongs.OMORI_BY_YOUR_SIDE).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item OMORI_SPACE_BOYFRIENDS_TAPE = registerItem("omori_space_boyfriends_tape", s -> new CaseRewardItem("Omori", s.jukeboxPlayable(ZAMJukeboxSongs.OMORI_SPACE_BOYFRIENDS_TAPE).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item OMORI_YOU_WERE_WRONG_GO_BACK = registerItem("omori_you_were_wrong_go_back", s -> new CaseRewardItem("Omori", s.jukeboxPlayable(ZAMJukeboxSongs.OMORI_YOU_WERE_WRONG_GO_BACK).stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item OMORI_FINDING_SHAPES_IN_THE_CLOUDS = registerItem("omori_finding_shapes_in_the_clouds", s -> new CaseRewardItem("Omori", s.jukeboxPlayable(ZAMJukeboxSongs.OMORI_FINDING_SHAPES_IN_THE_CLOUDS).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item OMORI_I_PREFER_MY_PIZZA = registerItem("omori_i_prefer_my_pizza", s -> new CaseRewardItem("Omori", s.jukeboxPlayable(ZAMJukeboxSongs.OMORI_I_PREFER_MY_PIZZA).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item OMORI_SWEET_PARALYSIS = registerItem("omori_sweet_paralysis", s -> new CaseRewardItem("Omori", s.jukeboxPlayable(ZAMJukeboxSongs.OMORI_SWEET_PARALYSIS).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item OMORI_WANDERING_ROSE = registerItem("omori_wandering_rose", s -> new CaseRewardItem("Omori", s.jukeboxPlayable(ZAMJukeboxSongs.OMORI_WANDERING_ROSE).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item OMORI_WORLDS_END_VALENTINE = registerItem("omori_worlds_end_valentine", s -> new CaseRewardItem("Omori", s.jukeboxPlayable(ZAMJukeboxSongs.OMORI_WORLDS_END_VALENTINE).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item OMORI_LOST_LIBRARY = registerItem("omori_lost_library", s -> new CaseRewardItem("Omori", s.jukeboxPlayable(ZAMJukeboxSongs.OMORI_LOST_LIBRARY).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item OMORI_GOLDENVENGEANCE = registerItem("omori_goldenvengeance", s -> new CaseRewardItem("Omori", s.jukeboxPlayable(ZAMJukeboxSongs.OMORI_GOLDENVENGEANCE).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item OMORI_BREADY_STEADY_GO = registerItem("omori_bready_steady_go", s -> new CaseRewardItem("Omori", s.jukeboxPlayable(ZAMJukeboxSongs.OMORI_BREADY_STEADY_GO).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item OMORI_A_HOME_FOR_FLOWERS = registerItem("omori_a_home_for_flowers", s -> new CaseRewardItem("Omori", s.jukeboxPlayable(ZAMJukeboxSongs.OMORI_A_HOME_FOR_FLOWERS).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item OMORI_OMORI = registerItem("omori_omori", s -> new CaseRewardItem("Omori", s.jukeboxPlayable(ZAMJukeboxSongs.OMORI_OMORI).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item OMORI_DUET = registerItem("omori_duet", s -> new CaseRewardItem("Omori", s.jukeboxPlayable(ZAMJukeboxSongs.OMORI_DUET).stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item OMORI_MY_TIME = registerItem("omori_my_time", s -> new CaseRewardItem("Omori", s.jukeboxPlayable(ZAMJukeboxSongs.OMORI_MY_TIME).stacksTo(1).rarity(Rarity.EPIC)));

    //Spongebob
    public static final Item SPONGEBOB_KRUSTY_KRAB_HAT = registerItem("spongebob_krusty_krab_hat", s -> new HatItem(s.stacksTo(1).humanoidArmor(ArmorMaterials.NETHERITE, ArmorType.HELMET).rarity(Rarity.RARE), "SpongeBob SquarePants", ZAMSounds.SPONGEBOB_EQUIP));
    public static final Item SPONGEBOB_JELLYFISHING_NET = registerItem("spongebob_jellyfishing_net", s -> new CaseRewardShovelItem("SpongeBob SquarePants", ToolMaterial.NETHERITE, 1.5F, -3.0F, s.rarity(Rarity.EPIC).fireResistant()));
    public static final Item SPONGEBOB_GOLDEN_SPATULA = registerItem("spongebob_golden_spatula", s -> new CaseRewardShovelItem("SpongeBob SquarePants", ToolMaterial.NETHERITE, 1.5F, -3.0F, s.rarity(Rarity.EPIC).fireResistant()));
    public static final Item SPONGEBOB_MARLIN_LANCE = registerItem("spongebob_marlin_lance", s -> new CaseRewardItem("SpongeBob SquarePants", s.spear(ToolMaterial.NETHERITE, 1.15F, 1.2F, 0.4F, 2.5F, 7.0F, 5.5F, 5.1F, 8.75F, 4.6F).rarity(Rarity.EPIC).fireResistant()));
    public static final Item SPONGEBOB_KRABBY_PATTY = registerItem("spongebob_krabby_patty", s -> new RewardItem("SpongeBob SquarePants", s.rarity(Rarity.EPIC)));
    public static final Item SPONGEBOB_KRUSTY_KRAB = registerItem("spongebob_krusty_krab", s -> new CaseRewardItem("SpongeBob SquarePants", s.jukeboxPlayable(ZAMJukeboxSongs.SPONGEBOB_KRUSTY_KRAB).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item SPONGEBOB_THE_LINEMAN = registerItem("spongebob_the_lineman", s -> new CaseRewardItem("SpongeBob SquarePants", s.jukeboxPlayable(ZAMJukeboxSongs.SPONGEBOB_THE_LINEMAN).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item SPONGEBOB_PUKA_A = registerItem("spongebob_puka_a", s -> new CaseRewardItem("SpongeBob SquarePants", s.jukeboxPlayable(ZAMJukeboxSongs.SPONGEBOB_PUKA_A).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item SPONGEBOB_AWARD_WINNERS_A = registerItem("spongebob_award_winners_a", s -> new CaseRewardItem("SpongeBob SquarePants", s.jukeboxPlayable(ZAMJukeboxSongs.SPONGEBOB_AWARD_WINNERS_A).stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item SPONGEBOB_ANDY_ANNORAK = registerItem("spongebob_andy_annorak", s -> new CaseRewardItem("SpongeBob SquarePants", s.jukeboxPlayable(ZAMJukeboxSongs.SPONGEBOB_ANDY_ANNORAK).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item SPONGEBOB_SWEET_VICTORY = registerItem("spongebob_sweet_victory", s -> new CaseRewardItem("SpongeBob SquarePants", s.jukeboxPlayable(ZAMJukeboxSongs.SPONGEBOB_SWEET_VICTORY).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item SPONGEBOB_JELLYFISH_JAM = registerItem("spongebob_jellyfish_jam", s -> new CaseRewardItem("SpongeBob SquarePants", s.jukeboxPlayable(ZAMJukeboxSongs.SPONGEBOB_JELLYFISH_JAM).stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item SPONGEBOB_THE_RAKE = registerItem("spongebob_the_rake", s -> new CaseRewardItem("SpongeBob SquarePants", s.jukeboxPlayable(ZAMJukeboxSongs.SPONGEBOB_THE_RAKE).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item SPONGEBOB_HAWAIIAN_TRAIN = registerItem("spongebob_hawaiian_train", s -> new CaseRewardItem("SpongeBob SquarePants", s.jukeboxPlayable(ZAMJukeboxSongs.SPONGEBOB_HAWAIIAN_TRAIN).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item SPONGEBOB_GARYS_SONG = registerItem("spongebob_garys_song", s -> new CaseRewardItem("SpongeBob SquarePants", s.jukeboxPlayable(ZAMJukeboxSongs.SPONGEBOB_GARYS_SONG).stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item SPONGEBOB_CHILL_OUT = registerItem("spongebob_chill_out", s -> new CaseRewardItem("SpongeBob SquarePants", s.jukeboxPlayable(ZAMJukeboxSongs.SPONGEBOB_CHILL_OUT).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item SPONGEBOB_MAUI_BEACH = registerItem("spongebob_maui_beach", s -> new CaseRewardItem("SpongeBob SquarePants", s.jukeboxPlayable(ZAMJukeboxSongs.SPONGEBOB_MAUI_BEACH).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item SPONGEBOB_ME_FOR_YOU = registerItem("spongebob_me_for_you", s -> new CaseRewardItem("SpongeBob SquarePants", s.jukeboxPlayable(ZAMJukeboxSongs.SPONGEBOB_ME_FOR_YOU).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item SPONGEBOB_SURF_BUGGY = registerItem("spongebob_surf_buggy", s -> new CaseRewardItem("SpongeBob SquarePants", s.jukeboxPlayable(ZAMJukeboxSongs.SPONGEBOB_SURF_BUGGY).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item SPONGEBOB_DRUNKEN_SAILOR = registerItem("spongebob_drunken_sailor", s -> new CaseRewardItem("SpongeBob SquarePants", s.jukeboxPlayable(ZAMJukeboxSongs.SPONGEBOB_DRUNKEN_SAILOR).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item SPONGEBOB_CHA_CHA_NOVA = registerItem("spongebob_cha_cha_nova", s -> new CaseRewardItem("SpongeBob SquarePants", s.jukeboxPlayable(ZAMJukeboxSongs.SPONGEBOB_CHA_CHA_NOVA).stacksTo(1).rarity(Rarity.RARE)));

    //Stardew Valley
    public static final Item STARDEW_VALLEY_DRAGONTOOTH_CUTLASS = registerItem("stardew_valley_dragontooth_cutlass", s -> new CaseRewardItem("Stardew Valley", s.sword(ToolMaterial.NETHERITE, 3, -2.4f).rarity(Rarity.EPIC).fireResistant()));
    public static final Item STARDEW_VALLEY_IRIDIUM_HOE = registerItem("stardew_valley_iridium_hoe", s -> new CaseRewardHoeItem("Stardew Valley", ToolMaterial.NETHERITE, -4.0F, 0.0F, s.rarity(Rarity.EPIC).fireResistant()));
    public static final Item STARDEW_VALLEY_IRIDIUM_FISHING_ROD = registerItem("stardew_valley_iridium_fishing_rod", s -> new CaseRewardFishingRodItem("Stardew Valley", ZAMSounds.STARDEW_VALLEY_FISHING_CAST, ZAMSounds.STARDEW_VALLEY_FISHING_BITE, ZAMSounds.STARDEW_VALLEY_FISHING_PULL, s.stacksTo(1).rarity(Rarity.EPIC).durability(134).fireResistant().enchantable(1)));
    public static final Item STARDEW_VALLEY_INFINITY_CROWN = registerItem("stardew_valley_infinity_crown", s -> new HatItem(s.stacksTo(1).humanoidArmor(ArmorMaterials.NETHERITE, ArmorType.HELMET).rarity(Rarity.RARE), "Stardew Valley", ZAMSounds.STARDEW_VALLEY_EQUIP_1));
    public static final Item STARDEW_VALLEY_STRAW_HAT = registerItem("stardew_valley_straw_hat", s -> new HatItem(s.stacksTo(1).humanoidArmor(ArmorMaterials.NETHERITE, ArmorType.HELMET).rarity(Rarity.RARE), "Stardew Valley", ZAMSounds.STARDEW_VALLEY_EQUIP_2));
    public static final Item STARDEW_VALLEY_STARDROP = registerItem("stardew_valley_stardrop", s -> new RewardItem("Stardew Valley", s.rarity(Rarity.EPIC)));
    public static final Item STARDEW_VALLEY_OVERTURE = registerItem("stardew_valley_overture", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_OVERTURE).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item STARDEW_VALLEY_SPRING = registerItem("stardew_valley_spring", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_SPRING).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item STARDEW_VALLEY_PELICAN_TOWN = registerItem("stardew_valley_pelican_town", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_PELICAN_TOWN).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item STARDEW_VALLEY_DISTANT_BANJO = registerItem("stardew_valley_distant_banjo", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_DISTANT_BANJO).stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item STARDEW_VALLEY_SUMMER = registerItem("stardew_valley_summer", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_SUMMER).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item STARDEW_VALLEY_ADVENTURE_GUILD = registerItem("stardew_valley_adventure_guild", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_ADVENTURE_GUILD).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item STARDEW_VALLEY_STARDROP_SALOON = registerItem("stardew_valley_stardrop_saloon", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_STARDROP_SALOON).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item STARDEW_VALLEY_LUAU_FESTIVAL = registerItem("stardew_valley_luau_festival", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_LUAU_FESTIVAL).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item STARDEW_VALLEY_MOONLIGHT_JELLYFISH = registerItem("stardew_valley_moonlight_jellyfish", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_MOONLIGHT_JELLYFISH).stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item STARDEW_VALLEY_FALL = registerItem("stardew_valley_fall", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_FALL).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item STARDEW_VALLEY_LIBRARY = registerItem("stardew_valley_library", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_LIBRARY).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item STARDEW_VALLEY_FAIR = registerItem("stardew_valley_fair", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_FAIR).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item STARDEW_VALLEY_SPIRITS_EVE = registerItem("stardew_valley_spirits_eve", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_SPIRITS_EVE).stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item STARDEW_VALLEY_WINTER = registerItem("stardew_valley_winter", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_WINTER).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item STARDEW_VALLEY_WINTER_FESTIVAL = registerItem("stardew_valley_winter_festival", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_WINTER_FESTIVAL).stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item STARDEW_VALLEY_COUNTRY_SHOP = registerItem("stardew_valley_country_shop", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_COUNTRY_SHOP).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item STARDEW_VALLEY_CALICO_DESERT = registerItem("stardew_valley_calico_desert", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_CALICO_DESERT).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item STARDEW_VALLEY_CRYSTAL_BELLS = registerItem("stardew_valley_crystal_bells", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_CRYSTAL_BELLS).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item STARDEW_VALLEY_FLICKER = registerItem("stardew_valley_flicker", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_FLICKER).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item STARDEW_VALLEY_DEEP_WOODS = registerItem("stardew_valley_deep_woods", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_DEEP_WOODS).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item STARDEW_VALLEY_NIGHT_MARKET = registerItem("stardew_valley_night_market", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_NIGHT_MARKET).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item STARDEW_VALLEY_SUBMARINE = registerItem("stardew_valley_submarine", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_SUBMARINE).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item STARDEW_VALLEY_MOVIE_THEATER = registerItem("stardew_valley_movie_theater", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_MOVIE_THEATER).stacksTo(1).rarity(Rarity.RARE)));
    public static final Item STARDEW_VALLEY_GINGER_ISLAND = registerItem("stardew_valley_ginger_island", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_GINGER_ISLAND).stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item STARDEW_VALLEY_LEOS_SONG = registerItem("stardew_valley_leos_song", s -> new CaseRewardItem("Stardew Valley", s.jukeboxPlayable(ZAMJukeboxSongs.STARDEW_VALLEY_LEOS_SONG).stacksTo(1).rarity(Rarity.RARE)));

    public static ResourceKey<Item> getRK(Item item) {
        return BuiltInRegistries.ITEM.getResourceKey(item).get();
    }

    private static Item registerItem(String name, Function<Item.Properties, Item> function) {
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(ZAMMod.MOD_ID, name),
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ZAMMod.MOD_ID, name)))));
    }

    public static void registerItems() {
        ZAMMod.LOGGER.info("Registering Items for " + ZAMMod.MOD_ID);
    }
}
