package net.ron.zam.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.world.item.JukeboxSong;
import net.ron.zam.ZAMMod;

public class ZAMJukeboxSongs {

    //Castle Crashers
    public static final ResourceKey<JukeboxSong> CASTLE_CRASHERS_FOUR_BRAVE_CHAMPS = create("castle_crashers_four_brave_champs");
    public static final ResourceKey<JukeboxSong> CASTLE_CRASHERS_FLUTEY = create("castle_crashers_flutey");
    public static final ResourceKey<JukeboxSong> CASTLE_CRASHERS_SPANISH_WALTZ = create("castle_crashers_spanish_waltz");
    public static final ResourceKey<JukeboxSong> CASTLE_CRASHERS_RACE_AROUND_THE_WORLD = create("castle_crashers_race_around_the_world");
    public static final ResourceKey<JukeboxSong> CASTLE_CRASHERS_JUMPER = create("castle_crashers_jumper");
    public static final ResourceKey<JukeboxSong> CASTLE_CRASHERS_SPACE_PIRATES = create("castle_crashers_space_pirates");
    public static final ResourceKey<JukeboxSong> CASTLE_CRASHERS_THE_SHOW = create("castle_crashers_the_show");
    public static final ResourceKey<JukeboxSong> CASTLE_CRASHERS_DARK_SKIES = create("castle_crashers_dark_skies");
    public static final ResourceKey<JukeboxSong> CASTLE_CRASHERS_RAGE_CHAMPIONS = create("castle_crashers_rage_champions");
    public static final ResourceKey<JukeboxSong> CASTLE_CRASHERS_PLEASE_DONT = create("castle_crashers_please_dont");
    public static final ResourceKey<JukeboxSong> CASTLE_CRASHERS_THE_ABDUCTION = create("castle_crashers_the_abduction");
    public static final ResourceKey<JukeboxSong> CASTLE_CRASHERS_WINTER_BLISS = create("castle_crashers_winter_bliss");
    public static final ResourceKey<JukeboxSong> CASTLE_CRASHERS_SIMPLE_SIGHT = create("castle_crashers_simple_sight");
    public static final ResourceKey<JukeboxSong> CASTLE_CRASHERS_FINAL_CONFRONTATION = create("castle_crashers_final_confrontation");
    public static final ResourceKey<JukeboxSong> CASTLE_CRASHERS_ARCHETYPE = create("castle_crashers_archetype");
    public static final ResourceKey<JukeboxSong> CASTLE_CRASHERS_BATTLEBLOCK = create("castle_crashers_battleblock");

    //Deltarune
    public static final ResourceKey<JukeboxSong> DELTARUNE_RUDE_BUSTER = create("deltarune_rude_buster");
    public static final ResourceKey<JukeboxSong> DELTARUNE_FIELD_OF_HOPES_AND_DREAMS = create("deltarune_field_of_hopes_and_dreams");
    public static final ResourceKey<JukeboxSong> DELTARUNE_CHAOS_KING = create("deltarune_chaos_king");
    public static final ResourceKey<JukeboxSong> DELTARUNE_THE_WORLD_REVOLVING = create("deltarune_the_world_revolving");
    public static final ResourceKey<JukeboxSong> DELTARUNE_A_CYBERS_WORLD = create("deltarune_a_cybers_world");
    public static final ResourceKey<JukeboxSong> DELTARUNE_SMART_RACE = create("deltarune_smart_race");
    public static final ResourceKey<JukeboxSong> DELTARUNE_PANDORA_PALACE = create("deltarune_pandora_palace");
    public static final ResourceKey<JukeboxSong> DELTARUNE_ATTACK_OF_THE_KILLER_QUEEN = create("deltarune_attack_of_the_killer_queen");
    public static final ResourceKey<JukeboxSong> DELTARUNE_BIG_SHOT = create("deltarune_big_shot");
    public static final ResourceKey<JukeboxSong> DELTARUNE_PHYSICAL_CHALLENGE = create("deltarune_physical_challenge");
    public static final ResourceKey<JukeboxSong> DELTARUNE_TV_WORLD = create("deltarune_tv_world");
    public static final ResourceKey<JukeboxSong> DELTARUNE_ITS_TV_TIME = create("deltarune_its_tv_time");
    public static final ResourceKey<JukeboxSong> DELTARUNE_BLACK_KNIFE = create("deltarune_black_knife");
    public static final ResourceKey<JukeboxSong> DELTARUNE_CASTLE_FUNK = create("deltarune_castle_funk");
    public static final ResourceKey<JukeboxSong> DELTARUNE_HAMMER_OF_JUSTICE = create("deltarune_hammer_of_justice");
    public static final ResourceKey<JukeboxSong> DELTARUNE_THE_THIRD_SANCTUARY = create("deltarune_the_third_sanctuary");
    public static final ResourceKey<JukeboxSong> DELTARUNE_GUARDIAN = create("deltarune_guardian");
    public static final ResourceKey<JukeboxSong> DELTARUNE_SUNSET_OF_SEVEN_SUNS = create("deltarune_sunset_of_seven_suns");
    public static final ResourceKey<JukeboxSong> DELTARUNE_FLOWER_CASTLE = create("deltarune_flower_castle");
    public static final ResourceKey<JukeboxSong> DELTARUNE_RUNNING_SKY = create("deltarune_running_sky");
    public static final ResourceKey<JukeboxSong> DELTARUNE_FLOWER_MAN = create("deltarune_flower_man");
    public static final ResourceKey<JukeboxSong> DELTARUNE_WELCOME_TO_THE_GREEN_ROOM = create("deltarune_welcome_to_the_green_room");
    public static final ResourceKey<JukeboxSong> DELTARUNE_FIREPLACE = create("deltarune_fireplace");
    public static final ResourceKey<JukeboxSong> DELTARUNE_LANTERN = create("deltarune_lantern");
    public static final ResourceKey<JukeboxSong> DELTARUNE_THOUSAND_CAFE_ZUKAN = create("deltarune_thousand_cafe_zukan");

    //Dragon Ball
    public static final ResourceKey<JukeboxSong> DRAGON_BALL_GATEBREAKER = create("dragon_ball_gatebreaker");
    public static final ResourceKey<JukeboxSong> DRAGON_BALL_DAN_DAN = create("dragon_ball_dan_dan");
    public static final ResourceKey<JukeboxSong> DRAGON_BALL_CHA_LA = create("dragon_ball_cha_la");
    public static final ResourceKey<JukeboxSong> DRAGON_BALL_ULTRA_INSTINCT = create("dragon_ball_ultra_instinct");
    public static final ResourceKey<JukeboxSong> DRAGON_BALL_BROLY_VS_GOGETA = create("dragon_ball_broly_vs_gogeta");
    public static final ResourceKey<JukeboxSong> DRAGON_BALL_GT_RECAP = create("dragon_ball_gt_recap");
    public static final ResourceKey<JukeboxSong> DRAGON_BALL_THE_DRINK = create("dragon_ball_the_drink");

    //Hunter X Hunter
    public static final ResourceKey<JukeboxSong> HXH_HUNTING_FOR_YOUR_DREAM = create("hxh_hunting_for_your_dream");
    public static final ResourceKey<JukeboxSong> HXH_ALL_I_NEED_IS_MONEY = create("hxh_all_i_need_is_money");
    public static final ResourceKey<JukeboxSong> HXH_DEPARTURE = create("hxh_departure");
    public static final ResourceKey<JukeboxSong> HXH_FROM_WHALE_ISLAND = create("hxh_from_whale_island");
    public static final ResourceKey<JukeboxSong> HXH_HISOKA_THEME = create("hxh_hisoka_theme");
    public static final ResourceKey<JukeboxSong> HXH_WORLD_OF_ADVENTURES = create("hxh_world_of_adventures");
    public static final ResourceKey<JukeboxSong> HXH_BOYS_BE_COURAGEOUS = create("hxh_boys_be_courageous");

    //Omori
    public static final ResourceKey<JukeboxSong> OMORI_MY_TIME = create("omori_my_time");
    public static final ResourceKey<JukeboxSong> OMORI_DUET = create("omori_duet");
    public static final ResourceKey<JukeboxSong> OMORI_SWEET_PARALYSIS = create("omori_sweet_paralysis");
    public static final ResourceKey<JukeboxSong> OMORI_LOST_LIBRARY = create("omori_lost_library");
    public static final ResourceKey<JukeboxSong> OMORI_A_HOME_FOR_FLOWERS = create("omori_a_home_for_flowers");
    public static final ResourceKey<JukeboxSong> OMORI_TITLE = create("omori_title");
    public static final ResourceKey<JukeboxSong> OMORI_BY_YOUR_SIDE = create("omori_by_your_side");
    public static final ResourceKey<JukeboxSong> OMORI_WORLDS_END_VALENTINE = create("omori_worlds_end_valentine");
    public static final ResourceKey<JukeboxSong> OMORI_BREADY_STEADY_GO = create("omori_bready_steady_go");
    public static final ResourceKey<JukeboxSong> OMORI_YOU_WERE_WRONG_GO_BACK = create("omori_you_were_wrong_go_back");
    public static final ResourceKey<JukeboxSong> OMORI_GOLDENVENGEANCE = create("omori_goldenvengeance");
    public static final ResourceKey<JukeboxSong> OMORI_SPACE_BOYFRIENDS_TAPE = create("omori_space_boyfriends_tape");
    public static final ResourceKey<JukeboxSong> OMORI_WANDERING_ROSE = create("omori_wandering_rose");
    public static final ResourceKey<JukeboxSong> OMORI_I_PREFER_MY_PIZZA = create("omori_i_prefer_my_pizza");
    public static final ResourceKey<JukeboxSong> OMORI_OMORI = create("omori_omori");
    public static final ResourceKey<JukeboxSong> OMORI_FINDING_SHAPES_IN_THE_CLOUDS = create("omori_finding_shapes_in_the_clouds");

    //Spongebob
    public static final ResourceKey<JukeboxSong> SPONGEBOB_KRUSTY_KRAB = create("spongebob_krusty_krab");
    public static final ResourceKey<JukeboxSong> SPONGEBOB_THE_LINEMAN = create("spongebob_the_lineman");
    public static final ResourceKey<JukeboxSong> SPONGEBOB_PUKA_A = create("spongebob_puka_a");
    public static final ResourceKey<JukeboxSong> SPONGEBOB_AWARD_WINNERS_A = create("spongebob_award_winners_a");
    public static final ResourceKey<JukeboxSong> SPONGEBOB_ANDY_ANNORAK = create("spongebob_andy_annorak");
    public static final ResourceKey<JukeboxSong> SPONGEBOB_SWEET_VICTORY = create("spongebob_sweet_victory");
    public static final ResourceKey<JukeboxSong> SPONGEBOB_JELLYFISH_JAM = create("spongebob_jellyfish_jam");
    public static final ResourceKey<JukeboxSong> SPONGEBOB_THE_RAKE = create("spongebob_the_rake");
    public static final ResourceKey<JukeboxSong> SPONGEBOB_HAWAIIAN_TRAIN = create("spongebob_hawaiian_train");
    public static final ResourceKey<JukeboxSong> SPONGEBOB_GARYS_SONG = create("spongebob_garys_song");
    public static final ResourceKey<JukeboxSong> SPONGEBOB_CHILL_OUT = create("spongebob_chill_out");
    public static final ResourceKey<JukeboxSong> SPONGEBOB_MAUI_BEACH = create("spongebob_maui_beach");
    public static final ResourceKey<JukeboxSong> SPONGEBOB_ME_FOR_YOU = create("spongebob_me_for_you");
    public static final ResourceKey<JukeboxSong> SPONGEBOB_SURF_BUGGY = create("spongebob_surf_buggy");
    public static final ResourceKey<JukeboxSong> SPONGEBOB_DRUNKEN_SAILOR = create("spongebob_drunken_sailor");
    public static final ResourceKey<JukeboxSong> SPONGEBOB_CHA_CHA_NOVA = create("spongebob_cha_cha_nova");

    //Stardew Valley
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_OVERTURE = create("stardew_valley_overture");
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_SPRING = create("stardew_valley_spring");
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_PELICAN_TOWN = create("stardew_valley_pelican_town");
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_DISTANT_BANJO = create("stardew_valley_distant_banjo");
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_SUMMER = create("stardew_valley_summer");
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_ADVENTURE_GUILD = create("stardew_valley_adventure_guild");
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_STARDROP_SALOON = create("stardew_valley_stardrop_saloon");
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_LUAU_FESTIVAL = create("stardew_valley_luau_festival");
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_MOONLIGHT_JELLYFISH = create("stardew_valley_moonlight_jellyfish");
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_FALL = create("stardew_valley_fall");
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_LIBRARY = create("stardew_valley_library");
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_FAIR = create("stardew_valley_fair");
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_SPIRITS_EVE = create("stardew_valley_spirits_eve");
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_WINTER = create("stardew_valley_winter");
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_WINTER_FESTIVAL = create("stardew_valley_winter_festival");
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_COUNTRY_SHOP = create("stardew_valley_country_shop");
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_CALICO_DESERT = create("stardew_valley_calico_desert");
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_CRYSTAL_BELLS = create("stardew_valley_crystal_bells");
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_FLICKER = create("stardew_valley_flicker");
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_DEEP_WOODS = create("stardew_valley_deep_woods");
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_NIGHT_MARKET = create("stardew_valley_night_market");
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_SUBMARINE = create("stardew_valley_submarine");
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_MOVIE_THEATER = create("stardew_valley_movie_theater");
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_GINGER_ISLAND = create("stardew_valley_ginger_island");
    public static final ResourceKey<JukeboxSong> STARDEW_VALLEY_LEOS_SONG = create("stardew_valley_leos_song");

    public static void bootstrap(BootstrapContext<JukeboxSong> context) {

        //Castle Crashers
        register(context, CASTLE_CRASHERS_FOUR_BRAVE_CHAMPS, ZAMSounds.CASTLE_CRASHERS_FOUR_BRAVE_CHAMPS, 170, 1);
        register(context, CASTLE_CRASHERS_FLUTEY, ZAMSounds.CASTLE_CRASHERS_FLUTEY, 76, 1);
        register(context, CASTLE_CRASHERS_SPANISH_WALTZ, ZAMSounds.CASTLE_CRASHERS_SPANISH_WALTZ, 91, 1);
        register(context, CASTLE_CRASHERS_RACE_AROUND_THE_WORLD, ZAMSounds.CASTLE_CRASHERS_RACE_AROUND_THE_WORLD, 66, 1);
        register(context, CASTLE_CRASHERS_JUMPER, ZAMSounds.CASTLE_CRASHERS_JUMPER, 100, 1);
        register(context, CASTLE_CRASHERS_SPACE_PIRATES, ZAMSounds.CASTLE_CRASHERS_SPACE_PIRATES, 167, 1);
        register(context, CASTLE_CRASHERS_THE_SHOW, ZAMSounds.CASTLE_CRASHERS_THE_SHOW, 171, 1);
        register(context, CASTLE_CRASHERS_DARK_SKIES, ZAMSounds.CASTLE_CRASHERS_DARK_SKIES, 208, 1);
        register(context, CASTLE_CRASHERS_RAGE_CHAMPIONS, ZAMSounds.CASTLE_CRASHERS_RAGE_CHAMPIONS, 151, 1);
        register(context, CASTLE_CRASHERS_PLEASE_DONT, ZAMSounds.CASTLE_CRASHERS_PLEASE_DONT, 210, 1);
        register(context, CASTLE_CRASHERS_WINTER_BLISS, ZAMSounds.CASTLE_CRASHERS_WINTER_BLISS, 185, 1);
        register(context, CASTLE_CRASHERS_SIMPLE_SIGHT, ZAMSounds.CASTLE_CRASHERS_SIMPLE_SIGHT, 211, 1);
        register(context, CASTLE_CRASHERS_FINAL_CONFRONTATION, ZAMSounds.CASTLE_CRASHERS_FINAL_CONFRONTATION, 134, 1);
        register(context, CASTLE_CRASHERS_ARCHETYPE, ZAMSounds.CASTLE_CRASHERS_ARCHETYPE, 242, 1);
        register(context, CASTLE_CRASHERS_BATTLEBLOCK, ZAMSounds.CASTLE_CRASHERS_BATTLEBLOCK, 240, 1);
        register(context, CASTLE_CRASHERS_THE_ABDUCTION, ZAMSounds.CASTLE_CRASHERS_THE_ABDUCTION, 170, 1);

        //Deltarune
        register(context, DELTARUNE_RUDE_BUSTER, ZAMSounds.DELTARUNE_RUDE_BUSTER, 75, 2);
        register(context, DELTARUNE_FIELD_OF_HOPES_AND_DREAMS, ZAMSounds.DELTARUNE_FIELD_OF_HOPES_AND_DREAMS, 162, 2);
        register(context, DELTARUNE_CHAOS_KING, ZAMSounds.DELTARUNE_CHAOS_KING, 106, 2);
        register(context, DELTARUNE_THE_WORLD_REVOLVING, ZAMSounds.DELTARUNE_THE_WORLD_REVOLVING, 101, 2);
        register(context, DELTARUNE_A_CYBERS_WORLD, ZAMSounds.DELTARUNE_A_CYBERS_WORLD, 166, 2);
        register(context, DELTARUNE_SMART_RACE, ZAMSounds.DELTARUNE_SMART_RACE, 66, 2);
        register(context, DELTARUNE_PANDORA_PALACE, ZAMSounds.DELTARUNE_PANDORA_PALACE, 99, 2);
        register(context, DELTARUNE_ATTACK_OF_THE_KILLER_QUEEN, ZAMSounds.DELTARUNE_ATTACK_OF_THE_KILLER_QUEEN, 121, 2);
        register(context, DELTARUNE_BIG_SHOT, ZAMSounds.DELTARUNE_BIG_SHOT, 142, 2);
        register(context, DELTARUNE_PHYSICAL_CHALLENGE, ZAMSounds.DELTARUNE_PHYSICAL_CHALLENGE, 106, 2);
        register(context, DELTARUNE_TV_WORLD, ZAMSounds.DELTARUNE_TV_WORLD, 130, 2);
        register(context, DELTARUNE_ITS_TV_TIME, ZAMSounds.DELTARUNE_ITS_TV_TIME, 166, 2);
        register(context, DELTARUNE_BLACK_KNIFE, ZAMSounds.DELTARUNE_BLACK_KNIFE, 118, 2);
        register(context, DELTARUNE_CASTLE_FUNK, ZAMSounds.DELTARUNE_CASTLE_FUNK, 177, 2);
        register(context, DELTARUNE_HAMMER_OF_JUSTICE, ZAMSounds.DELTARUNE_HAMMER_OF_JUSTICE, 134, 2);
        register(context, DELTARUNE_THE_THIRD_SANCTUARY, ZAMSounds.DELTARUNE_THE_THIRD_SANCTUARY, 147, 2);
        register(context, DELTARUNE_GUARDIAN, ZAMSounds.DELTARUNE_GUARDIAN, 227, 2);
        register(context, DELTARUNE_SUNSET_OF_SEVEN_SUNS, ZAMSounds.DELTARUNE_SUNSET_OF_SEVEN_SUNS, 96, 2);
        register(context, DELTARUNE_FLOWER_CASTLE, ZAMSounds.DELTARUNE_FLOWER_CASTLE, 270, 2);
        register(context, DELTARUNE_RUNNING_SKY, ZAMSounds.DELTARUNE_RUNNING_SKY, 142, 2);
        register(context, DELTARUNE_FLOWER_MAN, ZAMSounds.DELTARUNE_FLOWER_MAN, 192, 2);
        register(context, DELTARUNE_WELCOME_TO_THE_GREEN_ROOM, ZAMSounds.DELTARUNE_WELCOME_TO_THE_GREEN_ROOM, 90, 2);
        register(context, DELTARUNE_FIREPLACE, ZAMSounds.DELTARUNE_FIREPLACE, 158, 2);
        register(context, DELTARUNE_LANTERN, ZAMSounds.DELTARUNE_LANTERN, 69, 2);
        register(context, DELTARUNE_THOUSAND_CAFE_ZUKAN, ZAMSounds.DELTARUNE_THOUSAND_CAFE_ZUKAN, 89, 2);

        //Dragon Ball
        register(context, DRAGON_BALL_GATEBREAKER, ZAMSounds.DRAGON_BALL_GATEBREAKER, 106, 1);
        register(context, DRAGON_BALL_DAN_DAN, ZAMSounds.DRAGON_BALL_DAN_DAN, 211, 1);
        register(context, DRAGON_BALL_CHA_LA, ZAMSounds.DRAGON_BALL_CHA_LA, 196, 1);
        register(context, DRAGON_BALL_ULTRA_INSTINCT, ZAMSounds.DRAGON_BALL_ULTRA_INSTINCT, 163, 1);
        register(context, DRAGON_BALL_BROLY_VS_GOGETA, ZAMSounds.DRAGON_BALL_BROLY_VS_GOGETA, 235, 1);
        register(context, DRAGON_BALL_GT_RECAP, ZAMSounds.DRAGON_BALL_GT_RECAP, 89, 1);
        register(context, DRAGON_BALL_THE_DRINK, ZAMSounds.DRAGON_BALL_THE_DRINK, 134, 1);

        //Hunter X Hunter
        register(context, HXH_HUNTING_FOR_YOUR_DREAM, ZAMSounds.HXH_HUNTING_FOR_YOUR_DREAM, 81, 2);
        register(context, HXH_ALL_I_NEED_IS_MONEY, ZAMSounds.HXH_ALL_I_NEED_IS_MONEY, 91, 2);
        register(context, HXH_DEPARTURE, ZAMSounds.HXH_DEPARTURE, 90, 2);
        register(context, HXH_FROM_WHALE_ISLAND, ZAMSounds.HXH_FROM_WHALE_ISLAND,121, 2);
        register(context, HXH_HISOKA_THEME, ZAMSounds.HXH_HISOKA_THEME, 161, 2);
        register(context, HXH_WORLD_OF_ADVENTURES, ZAMSounds.HXH_WORLD_OF_ADVENTURES, 200, 2);
        register(context, HXH_BOYS_BE_COURAGEOUS, ZAMSounds.HXH_BOYS_BE_COURAGEOUS, 70, 2);

        //Omori
        register(context, OMORI_MY_TIME, ZAMSounds.OMORI_MY_TIME, 180, 2);
        register(context, OMORI_DUET, ZAMSounds.OMORI_DUET, 128, 2);
        register(context, OMORI_SWEET_PARALYSIS, ZAMSounds.OMORI_SWEET_PARALYSIS, 70, 2);
        register(context, OMORI_LOST_LIBRARY, ZAMSounds.OMORI_LOST_LIBRARY, 75, 2);
        register(context, OMORI_A_HOME_FOR_FLOWERS, ZAMSounds.OMORI_A_HOME_FOR_FLOWERS, 79, 2);
        register(context, OMORI_TITLE, ZAMSounds.OMORI_TITLE, 37, 1);
        register(context, OMORI_BY_YOUR_SIDE, ZAMSounds.OMORI_BY_YOUR_SIDE, 96, 2);
        register(context, OMORI_WORLDS_END_VALENTINE, ZAMSounds.OMORI_WORLDS_END_VALENTINE, 147, 2);
        register(context, OMORI_BREADY_STEADY_GO, ZAMSounds.OMORI_BREADY_STEADY_GO, 125, 1);
        register(context, OMORI_YOU_WERE_WRONG_GO_BACK, ZAMSounds.OMORI_YOU_WERE_WRONG_GO_BACK, 200, 3);
        register(context, OMORI_GOLDENVENGEANCE, ZAMSounds.OMORI_GOLDENVENGEANCE, 149, 2);
        register(context, OMORI_SPACE_BOYFRIENDS_TAPE, ZAMSounds.OMORI_SPACE_BOYFRIENDS_TAPE, 29, 2);
        register(context, OMORI_WANDERING_ROSE, ZAMSounds.OMORI_WANDERING_ROSE, 113, 1);
        register(context, OMORI_I_PREFER_MY_PIZZA, ZAMSounds.OMORI_I_PREFER_MY_PIZZA, 44, 1);
        register(context, OMORI_OMORI, ZAMSounds.OMORI_OMORI, 153, 3);
        register(context, OMORI_FINDING_SHAPES_IN_THE_CLOUDS, ZAMSounds.OMORI_FINDING_SHAPES_IN_THE_CLOUDS, 91, 2);

        //Spongebob
        register(context, SPONGEBOB_KRUSTY_KRAB, ZAMSounds.SPONGEBOB_KRUSTY_KRAB, 147, 6);
        register(context, SPONGEBOB_THE_LINEMAN, ZAMSounds.SPONGEBOB_THE_LINEMAN, 156, 6);
        register(context, SPONGEBOB_PUKA_A, ZAMSounds.SPONGEBOB_PUKA_A, 71, 6);
        register(context, SPONGEBOB_AWARD_WINNERS_A, ZAMSounds.SPONGEBOB_AWARD_WINNERS_A, 48, 6);
        register(context, SPONGEBOB_ANDY_ANNORAK, ZAMSounds.SPONGEBOB_ANDY_ANNORAK, 154, 6);
        register(context, SPONGEBOB_SWEET_VICTORY, ZAMSounds.SPONGEBOB_SWEET_VICTORY, 126, 6);
        register(context, SPONGEBOB_JELLYFISH_JAM, ZAMSounds.SPONGEBOB_JELLYFISH_JAM, 67, 6);
        register(context, SPONGEBOB_THE_RAKE, ZAMSounds.SPONGEBOB_THE_RAKE, 150, 6);
        register(context, SPONGEBOB_HAWAIIAN_TRAIN, ZAMSounds.SPONGEBOB_HAWAIIAN_TRAIN, 75, 6);
        register(context, SPONGEBOB_GARYS_SONG, ZAMSounds.SPONGEBOB_GARYS_SONG, 144, 6);
        register(context, SPONGEBOB_CHILL_OUT, ZAMSounds.SPONGEBOB_CHILL_OUT, 194, 6);
        register(context, SPONGEBOB_MAUI_BEACH, ZAMSounds.SPONGEBOB_MAUI_BEACH, 143, 6);
        register(context, SPONGEBOB_ME_FOR_YOU, ZAMSounds.SPONGEBOB_ME_FOR_YOU, 170, 6);
        register(context, SPONGEBOB_SURF_BUGGY, ZAMSounds.SPONGEBOB_SURF_BUGGY, 159, 6);
        register(context, SPONGEBOB_DRUNKEN_SAILOR, ZAMSounds.SPONGEBOB_DRUNKEN_SAILOR, 162, 6);
        register(context, SPONGEBOB_CHA_CHA_NOVA, ZAMSounds.SPONGEBOB_CHA_CHA_NOVA, 170, 6);

        //Stardew Valley
        register(context, STARDEW_VALLEY_OVERTURE, ZAMSounds.STARDEW_VALLEY_OVERTURE, 144, 1);
        register(context, STARDEW_VALLEY_SPRING, ZAMSounds.STARDEW_VALLEY_SPRING, 242, 2);
        register(context, STARDEW_VALLEY_PELICAN_TOWN, ZAMSounds.STARDEW_VALLEY_PELICAN_TOWN, 170, 2);
        register(context, STARDEW_VALLEY_DISTANT_BANJO, ZAMSounds.STARDEW_VALLEY_DISTANT_BANJO, 114, 1);
        register(context, STARDEW_VALLEY_SUMMER, ZAMSounds.STARDEW_VALLEY_SUMMER, 214, 2);
        register(context, STARDEW_VALLEY_ADVENTURE_GUILD, ZAMSounds.STARDEW_VALLEY_ADVENTURE_GUILD, 51, 2);
        register(context, STARDEW_VALLEY_STARDROP_SALOON, ZAMSounds.STARDEW_VALLEY_STARDROP_SALOON, 84, 2);
        register(context, STARDEW_VALLEY_LUAU_FESTIVAL, ZAMSounds.STARDEW_VALLEY_LUAU_FESTIVAL, 91, 2);
        register(context, STARDEW_VALLEY_MOONLIGHT_JELLYFISH, ZAMSounds.STARDEW_VALLEY_MOONLIGHT_JELLYFISH, 140, 1);
        register(context, STARDEW_VALLEY_FALL, ZAMSounds.STARDEW_VALLEY_FALL, 153, 2);
        register(context, STARDEW_VALLEY_LIBRARY, ZAMSounds.STARDEW_VALLEY_LIBRARY, 108, 1);
        register(context, STARDEW_VALLEY_FAIR, ZAMSounds.STARDEW_VALLEY_FAIR, 111, 1);
        register(context, STARDEW_VALLEY_SPIRITS_EVE, ZAMSounds.STARDEW_VALLEY_SPIRITS_EVE, 123, 2);
        register(context, STARDEW_VALLEY_WINTER, ZAMSounds.STARDEW_VALLEY_WINTER, 197, 2);
        register(context, STARDEW_VALLEY_WINTER_FESTIVAL, ZAMSounds.STARDEW_VALLEY_WINTER_FESTIVAL, 160, 2);
        register(context, STARDEW_VALLEY_COUNTRY_SHOP, ZAMSounds.STARDEW_VALLEY_COUNTRY_SHOP, 140, 1);
        register(context, STARDEW_VALLEY_CALICO_DESERT, ZAMSounds.STARDEW_VALLEY_CALICO_DESERT, 127, 2);
        register(context, STARDEW_VALLEY_CRYSTAL_BELLS, ZAMSounds.STARDEW_VALLEY_CRYSTAL_BELLS, 171, 1);
        register(context, STARDEW_VALLEY_FLICKER, ZAMSounds.STARDEW_VALLEY_FLICKER, 83, 1);
        register(context, STARDEW_VALLEY_DEEP_WOODS, ZAMSounds.STARDEW_VALLEY_DEEP_WOODS, 127, 2);
        register(context, STARDEW_VALLEY_NIGHT_MARKET, ZAMSounds.STARDEW_VALLEY_NIGHT_MARKET, 131, 2);
        register(context, STARDEW_VALLEY_SUBMARINE, ZAMSounds.STARDEW_VALLEY_SUBMARINE, 123, 1);
        register(context, STARDEW_VALLEY_MOVIE_THEATER, ZAMSounds.STARDEW_VALLEY_MOVIE_THEATER, 105, 2);
        register(context, STARDEW_VALLEY_GINGER_ISLAND, ZAMSounds.STARDEW_VALLEY_GINGER_ISLAND, 158, 2);
        register(context, STARDEW_VALLEY_LEOS_SONG, ZAMSounds.STARDEW_VALLEY_LEOS_SONG, 97, 1);
    }

    private static void register(BootstrapContext<JukeboxSong> context, ResourceKey<JukeboxSong> key, SoundEvent soundEvent, int lengthInSeconds, int comparatorOutput) {
        context.register(key, new JukeboxSong(Holder.direct(soundEvent), Component.translatable(Util.makeDescriptionId("jukebox_song", key.identifier())), (float) lengthInSeconds, comparatorOutput));
    }

    private static ResourceKey<JukeboxSong> create(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, ZAMMod.id(name));
    }
}
