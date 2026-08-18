package net.ron.zam.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.ron.zam.ZAMMod;
import net.ron.zam.registry.ZAMBlocks;
import net.ron.zam.registry.ZAMItems;

public class ZAMModelProvider extends FabricModelProvider {
    public ZAMModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerator) {
        blockModelGenerator.createChest(ZAMBlocks.MARINERS_FORTUNE, ZAMBlocks.MARINERS_FORTUNE, ZAMMod.id("mariners_fortune"), false);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(ZAMItems.GOLD_ICON, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.RED_ICON, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(ZAMItems.MUSIC_BOX, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.CASE_KEY, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(ZAMItems.WOOD_MEDAL, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.BRONZE_MEDAL, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.SILVER_MEDAL, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.GOLD_MEDAL, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.LEGENDARY_MEDAL, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(ZAMItems.SEA_JELLY, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.MESSAGE_IN_A_BOTTLE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.SECRET_MESSAGE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.TREASURE_POUCH, ModelTemplates.FLAT_ITEM);


        itemModelGenerator.generateFlatItem(ZAMItems.MARINE_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.MARINE_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.MARINE_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.MARINE_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.MARINE_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateSpear(ZAMItems.MARINE_SPEAR);

        itemModelGenerator.generateFlatItem(ZAMItems.CASTLE_CRASHERS_CHICKEN_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.CASTLE_CRASHERS_DEMON_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.CASTLE_CRASHERS_UNICORN_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.CASTLE_CRASHERS_CHAMPIONS_HORN, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.CASTLE_CRASHERS_FOUR_BRAVE_CHAMPS, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.CASTLE_CRASHERS_FLUTEY, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.CASTLE_CRASHERS_SPANISH_WALTZ, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.CASTLE_CRASHERS_RACE_AROUND_THE_WORLD, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.CASTLE_CRASHERS_JUMPER, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.CASTLE_CRASHERS_SPACE_PIRATES, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.CASTLE_CRASHERS_THE_SHOW, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.CASTLE_CRASHERS_SIMPLE_SIGHT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.CASTLE_CRASHERS_FINAL_CONFRONTATION, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.CASTLE_CRASHERS_ARCHETYPE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.CASTLE_CRASHERS_DARK_SKIES, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.CASTLE_CRASHERS_RAGE_CHAMPIONS, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.CASTLE_CRASHERS_BATTLEBLOCK, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.CASTLE_CRASHERS_PLEASE_DONT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.CASTLE_CRASHERS_THE_ABDUCTION, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.CASTLE_CRASHERS_WINTER_BLISS, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_BLACK_KNIFE_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_HOLY_HALBERD, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_MANE_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_THE_DELTARUNE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_FIELD_OF_HOPES_AND_DREAMS, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_CHAOS_KING, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_THE_WORLD_REVOLVING, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_A_CYBERS_WORLD, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_SMART_RACE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_PANDORA_PALACE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_ATTACK_OF_THE_KILLER_QUEEN, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_BIG_SHOT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_PHYSICAL_CHALLENGE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_TV_WORLD, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_ITS_TV_TIME, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_BLACK_KNIFE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_CASTLE_FUNK, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_HAMMER_OF_JUSTICE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_THE_THIRD_SANCTUARY, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_GUARDIAN, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_SUNSET_OF_SEVEN_SUNS, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_FLOWER_CASTLE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_RUNNING_SKY, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_FLOWER_MAN, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_RUDE_BUSTER, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_FIREPLACE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_LANTERN, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_WELCOME_TO_THE_GREEN_ROOM, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DELTARUNE_THOUSAND_CAFE_ZUKAN, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(ZAMItems.DRAGON_BALL_SICKLE_OF_SORROW, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DRAGON_BALL_GATEBREAKER, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DRAGON_BALL_DAN_DAN, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DRAGON_BALL_CHA_LA, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DRAGON_BALL_ULTRA_INSTINCT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DRAGON_BALL_BROLY_VS_GOGETA, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DRAGON_BALL_GT_RECAP, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.DRAGON_BALL_THE_DRINK, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFishingRod(ZAMItems.HXH_GONS_FISHING_ROD);
        itemModelGenerator.generateFlatItem(ZAMItems.HXH_HUNTERS_LICENSE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.HXH_HUNTING_FOR_YOUR_DREAM, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.HXH_ALL_I_NEED_IS_MONEY, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.HXH_HISOKA_THEME, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.HXH_FROM_WHALE_ISLAND, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.HXH_BOYS_BE_COURAGEOUS, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.HXH_DEPARTURE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.HXH_WORLD_OF_ADVENTURES, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(ZAMItems.OMORI_LOL_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.OMORI_SPIKED_BAT, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.OMORI_WHITESPACE_LIGHTBULB, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.OMORI_TITLE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.OMORI_BY_YOUR_SIDE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.OMORI_SPACE_BOYFRIENDS_TAPE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.OMORI_YOU_WERE_WRONG_GO_BACK, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.OMORI_FINDING_SHAPES_IN_THE_CLOUDS, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.OMORI_I_PREFER_MY_PIZZA, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.OMORI_SWEET_PARALYSIS, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.OMORI_WANDERING_ROSE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.OMORI_WORLDS_END_VALENTINE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.OMORI_LOST_LIBRARY, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.OMORI_GOLDENVENGEANCE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.OMORI_BREADY_STEADY_GO, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.OMORI_A_HOME_FOR_FLOWERS, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.OMORI_OMORI, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.OMORI_DUET, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.OMORI_MY_TIME, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(ZAMItems.SPONGEBOB_JELLYFISHING_NET, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.SPONGEBOB_GOLDEN_SPATULA, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.SPONGEBOB_KRABBY_PATTY, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.SPONGEBOB_KRUSTY_KRAB, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.SPONGEBOB_THE_LINEMAN, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.SPONGEBOB_PUKA_A, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.SPONGEBOB_AWARD_WINNERS_A, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.SPONGEBOB_ANDY_ANNORAK, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.SPONGEBOB_SWEET_VICTORY, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.SPONGEBOB_JELLYFISH_JAM, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.SPONGEBOB_THE_RAKE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.SPONGEBOB_HAWAIIAN_TRAIN, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.SPONGEBOB_GARYS_SONG, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.SPONGEBOB_CHILL_OUT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.SPONGEBOB_MAUI_BEACH, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.SPONGEBOB_ME_FOR_YOU, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.SPONGEBOB_SURF_BUGGY, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.SPONGEBOB_DRUNKEN_SAILOR, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.SPONGEBOB_CHA_CHA_NOVA, ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_DRAGONTOOTH_CUTLASS, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_IRIDIUM_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFishingRod(ZAMItems.STARDEW_VALLEY_IRIDIUM_FISHING_ROD);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_STARDROP, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_OVERTURE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_SPRING, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_PELICAN_TOWN, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_DISTANT_BANJO, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_SUMMER, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_ADVENTURE_GUILD, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_STARDROP_SALOON, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_LUAU_FESTIVAL, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_MOONLIGHT_JELLYFISH, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_FALL, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_LIBRARY, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_FAIR, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_SPIRITS_EVE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_WINTER, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_WINTER_FESTIVAL, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_COUNTRY_SHOP, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_CALICO_DESERT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_CRYSTAL_BELLS, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_FLICKER, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_DEEP_WOODS, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_NIGHT_MARKET, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_SUBMARINE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_MOVIE_THEATER, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_GINGER_ISLAND, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ZAMItems.STARDEW_VALLEY_LEOS_SONG, ModelTemplates.FLAT_ITEM);
    }
}
