package net.ron.zam.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.ron.zam.ZAMMod;
import net.ron.zam.api.casesystem.cases.CaseStacks;
import net.ron.zam.common.item.CassetteItem;

public class ZAMItemGroups {
    public static final CreativeModeTab ZAM = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            Identifier.parse(ZAMMod.MOD_ID + ":music"),
            CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("itemgroup.zam"))
                    .icon(() -> new ItemStack(ZAMItems.CASE_KEY))
                    .displayItems((ctx, output) -> {

                        output.accept(ZAMItems.CASE_KEY);
                        output.accept(ZAMItems.MUSIC_BOX);
                        output.accept(ZAMItems.RECORD_SLEEVE);

                        output.accept(ZAMItems.WOOD_MEDAL);
                        output.accept(ZAMItems.BRONZE_MEDAL);
                        output.accept(ZAMItems.SILVER_MEDAL);
                        output.accept(ZAMItems.GOLD_MEDAL);
                        output.accept(ZAMItems.LEGENDARY_MEDAL);
                        output.accept(ZAMItems.FISHERMAN_MASTERY_CAP);

                        output.accept(ZAMBlocks.MARINERS_FORTUNE);
                        output.accept(ZAMItems.TREASURE_POUCH);

                        output.accept(ZAMItems.SEA_JELLY);
                        output.accept(ZAMItems.MESSAGE_IN_A_BOTTLE);

                        output.accept(ZAMItems.MARINE_SWORD);
                        output.accept(ZAMItems.MARINE_AXE);
                        output.accept(ZAMItems.MARINE_PICKAXE);
                        output.accept(ZAMItems.MARINE_SHOVEL);
                        output.accept(ZAMItems.MARINE_HOE);
                        output.accept(ZAMItems.MARINE_SPEAR);

                        output.accept(ZAMBlocks.TELEVISION);
                        output.accept(ZAMBlocks.PROJECTOR);

                        output.accept(ZAMItems.VIDEO_TAPE);

                        for (var id : ZAMMod.CASSETTES.all().keySet()) {
                            ItemStack stack = new ItemStack(ZAMItems.CASSETTE);
                            CassetteItem.assign(stack, id);
                            output.accept(stack);
                        }

                        output.accept(ZAMBlocks.OAK_RECORD_RACK);
                        output.accept(ZAMBlocks.SPRUCE_RECORD_RACK);
                        output.accept(ZAMBlocks.BIRCH_RECORD_RACK);
                        output.accept(ZAMBlocks.DARK_OAK_RECORD_RACK);
                        output.accept(ZAMBlocks.MANGROVE_RECORD_RACK);
                        output.accept(ZAMBlocks.CHERRY_RECORD_RACK);
                        output.accept(ZAMBlocks.JUNGLE_RECORD_RACK);
                        output.accept(ZAMBlocks.ACACIA_RECORD_RACK);
                        output.accept(ZAMBlocks.CRIMSON_RECORD_RACK);
                        output.accept(ZAMBlocks.BAMBOO_RECORD_RACK);
                        output.accept(ZAMBlocks.WARPED_RECORD_RACK);
                        output.accept(ZAMBlocks.PALE_OAK_RECORD_RACK);

                        output.accept(CaseStacks.create(ZAMMod.id("castle_crashers_case")));
                        output.accept(ZAMItems.CASTLE_CRASHERS_CHICKEN_SWORD);
                        output.accept(ZAMItems.CASTLE_CRASHERS_DEMON_SWORD);
                        output.accept(ZAMItems.CASTLE_CRASHERS_UNICORN_SWORD);
                        output.accept(ZAMItems.CASTLE_CRASHERS_RED_KNIGHT_HELMET);
                        output.accept(ZAMItems.CASTLE_CRASHERS_ORANGE_KNIGHT_HELMET);
                        output.accept(ZAMItems.CASTLE_CRASHERS_BLUE_KNIGHT_HELMET);
                        output.accept(ZAMItems.CASTLE_CRASHERS_GREEN_KNIGHT_HELMET);
                        output.accept(ZAMItems.CASTLE_CRASHERS_CHAMPIONS_HORN);
                        output.accept(ZAMItems.CASTLE_CRASHERS_FOUR_BRAVE_CHAMPS);
                        output.accept(ZAMItems.CASTLE_CRASHERS_FLUTEY);
                        output.accept(ZAMItems.CASTLE_CRASHERS_SPANISH_WALTZ);
                        output.accept(ZAMItems.CASTLE_CRASHERS_RACE_AROUND_THE_WORLD);
                        output.accept(ZAMItems.CASTLE_CRASHERS_JUMPER);
                        output.accept(ZAMItems.CASTLE_CRASHERS_SPACE_PIRATES);
                        output.accept(ZAMItems.CASTLE_CRASHERS_THE_SHOW);
                        output.accept(ZAMItems.CASTLE_CRASHERS_DARK_SKIES);
                        output.accept(ZAMItems.CASTLE_CRASHERS_RAGE_CHAMPIONS);
                        output.accept(ZAMItems.CASTLE_CRASHERS_PLEASE_DONT);
                        output.accept(ZAMItems.CASTLE_CRASHERS_THE_ABDUCTION);
                        output.accept(ZAMItems.CASTLE_CRASHERS_WINTER_BLISS);
                        output.accept(ZAMItems.CASTLE_CRASHERS_SIMPLE_SIGHT);
                        output.accept(ZAMItems.CASTLE_CRASHERS_FINAL_CONFRONTATION);
                        output.accept(ZAMItems.CASTLE_CRASHERS_ARCHETYPE);
                        output.accept(ZAMItems.CASTLE_CRASHERS_BATTLEBLOCK);

                        output.accept(CaseStacks.create(ZAMMod.id("dragon_ball_case")));


                        output.accept(CaseStacks.create(ZAMMod.id("deltarune_case")));
                        output.accept(ZAMItems.DELTARUNE_BLACK_KNIFE_SWORD);
                        output.accept(ZAMItems.DELTARUNE_MANE_AXE);
                        output.accept(ZAMItems.DELTARUNE_HOLY_HALBERD);
                        output.accept(ZAMItems.DELTARUNE_TENNA_HEAD);
                        output.accept(ZAMItems.DELTARUNE_THE_DELTARUNE);

                        output.accept(ZAMItems.DELTARUNE_LANTERN);
                        output.accept(ZAMItems.DELTARUNE_FIELD_OF_HOPES_AND_DREAMS);
                        output.accept(ZAMItems.DELTARUNE_RUDE_BUSTER);
                        output.accept(ZAMItems.DELTARUNE_CHAOS_KING);
                        output.accept(ZAMItems.DELTARUNE_THE_WORLD_REVOLVING);

                        output.accept(ZAMItems.DELTARUNE_A_CYBERS_WORLD);
                        output.accept(ZAMItems.DELTARUNE_SMART_RACE);
                        output.accept(ZAMItems.DELTARUNE_PANDORA_PALACE);
                        output.accept(ZAMItems.DELTARUNE_ATTACK_OF_THE_KILLER_QUEEN);
                        output.accept(ZAMItems.DELTARUNE_BIG_SHOT);

                        output.accept(ZAMItems.DELTARUNE_PHYSICAL_CHALLENGE);
                        output.accept(ZAMItems.DELTARUNE_WELCOME_TO_THE_GREEN_ROOM);
                        output.accept(ZAMItems.DELTARUNE_TV_WORLD);
                        output.accept(ZAMItems.DELTARUNE_ITS_TV_TIME);
                        output.accept(ZAMItems.DELTARUNE_BLACK_KNIFE);

                        output.accept(ZAMItems.DELTARUNE_CASTLE_FUNK);
                        output.accept(ZAMItems.DELTARUNE_FIREPLACE);
                        output.accept(ZAMItems.DELTARUNE_HAMMER_OF_JUSTICE);
                        output.accept(ZAMItems.DELTARUNE_THE_THIRD_SANCTUARY);
                        output.accept(ZAMItems.DELTARUNE_GUARDIAN);

                        output.accept(ZAMItems.DELTARUNE_SUNSET_OF_SEVEN_SUNS);
                        output.accept(ZAMItems.DELTARUNE_FLOWER_CASTLE);
                        output.accept(ZAMItems.DELTARUNE_THOUSAND_CAFE_ZUKAN);
                        output.accept(ZAMItems.DELTARUNE_RUNNING_SKY);
                        output.accept(ZAMItems.DELTARUNE_FLOWER_MAN);

                        output.accept(ZAMItems.DRAGON_BALL_POWER_POLL);
                        output.accept(ZAMItems.DRAGON_BALL_SICKLE_OF_SORROW);
                        output.accept(ZAMItems.DRAGON_BALL_SUPER_SAIYAN_HAIR);
                        output.accept(ZAMItems.DRAGON_BALL_DAN_DAN);
                        output.accept(ZAMItems.DRAGON_BALL_GATEBREAKER);
                        output.accept(ZAMItems.DRAGON_BALL_BROLY_VS_GOGETA);
                        output.accept(ZAMItems.DRAGON_BALL_CHA_LA);
                        output.accept(ZAMItems.DRAGON_BALL_THE_DRINK);
                        output.accept(ZAMItems.DRAGON_BALL_ULTRA_INSTINCT);
                        output.accept(ZAMItems.DRAGON_BALL_GT_RECAP);


                        output.accept(CaseStacks.create(ZAMMod.id("hxh_case")));
                        output.accept(ZAMItems.HXH_GONS_FISHING_ROD);
                        output.accept(ZAMItems.HXH_GONS_HAIR);
                        output.accept(ZAMItems.HXH_HUNTERS_LICENSE);
                        output.accept(ZAMItems.HXH_BOYS_BE_COURAGEOUS);
                        output.accept(ZAMItems.HXH_FROM_WHALE_ISLAND);
                        output.accept(ZAMItems.HXH_WORLD_OF_ADVENTURES);
                        output.accept(ZAMItems.HXH_HISOKA_THEME);
                        output.accept(ZAMItems.HXH_DEPARTURE);
                        output.accept(ZAMItems.HXH_HUNTING_FOR_YOUR_DREAM);
                        output.accept(ZAMItems.HXH_ALL_I_NEED_IS_MONEY);


                        output.accept(CaseStacks.create(ZAMMod.id("omori_case")));
                        output.accept(ZAMItems.OMORI_LOL_SWORD);
                        output.accept(ZAMItems.OMORI_SPIKED_BAT);
                        output.accept(ZAMItems.OMORI_FLOWER_CROWN);
                        output.accept(ZAMItems.OMORI_WHITESPACE_LIGHTBULB);
                        output.accept(ZAMItems.OMORI_TITLE);
                        output.accept(ZAMItems.OMORI_BY_YOUR_SIDE);
                        output.accept(ZAMItems.OMORI_SPACE_BOYFRIENDS_TAPE);
                        output.accept(ZAMItems.OMORI_YOU_WERE_WRONG_GO_BACK);
                        output.accept(ZAMItems.OMORI_FINDING_SHAPES_IN_THE_CLOUDS);
                        output.accept(ZAMItems.OMORI_I_PREFER_MY_PIZZA);
                        output.accept(ZAMItems.OMORI_SWEET_PARALYSIS);
                        output.accept(ZAMItems.OMORI_WANDERING_ROSE);
                        output.accept(ZAMItems.OMORI_WORLDS_END_VALENTINE);
                        output.accept(ZAMItems.OMORI_LOST_LIBRARY);
                        output.accept(ZAMItems.OMORI_GOLDENVENGEANCE);
                        output.accept(ZAMItems.OMORI_BREADY_STEADY_GO);
                        output.accept(ZAMItems.OMORI_A_HOME_FOR_FLOWERS);
                        output.accept(ZAMItems.OMORI_OMORI);
                        output.accept(ZAMItems.OMORI_DUET);
                        output.accept(ZAMItems.OMORI_MY_TIME);

                        output.accept(CaseStacks.create(ZAMMod.id("spongebob_case")));
                        output.accept(ZAMItems.SPONGEBOB_GOLDEN_SPATULA);
                        output.accept(ZAMItems.SPONGEBOB_JELLYFISHING_NET);
                        output.accept(ZAMItems.SPONGEBOB_MARLIN_LANCE);
                        output.accept(ZAMItems.SPONGEBOB_KRUSTY_KRAB_HAT);
                        output.accept(ZAMItems.SPONGEBOB_KRABBY_PATTY);
                        output.accept(ZAMItems.SPONGEBOB_KRUSTY_KRAB);
                        output.accept(ZAMItems.SPONGEBOB_THE_LINEMAN);
                        output.accept(ZAMItems.SPONGEBOB_PUKA_A);
                        output.accept(ZAMItems.SPONGEBOB_AWARD_WINNERS_A);
                        output.accept(ZAMItems.SPONGEBOB_ANDY_ANNORAK);
                        output.accept(ZAMItems.SPONGEBOB_SWEET_VICTORY);
                        output.accept(ZAMItems.SPONGEBOB_JELLYFISH_JAM);
                        output.accept(ZAMItems.SPONGEBOB_THE_RAKE);
                        output.accept(ZAMItems.SPONGEBOB_HAWAIIAN_TRAIN);
                        output.accept(ZAMItems.SPONGEBOB_GARYS_SONG);
                        output.accept(ZAMItems.SPONGEBOB_CHILL_OUT);
                        output.accept(ZAMItems.SPONGEBOB_MAUI_BEACH);
                        output.accept(ZAMItems.SPONGEBOB_ME_FOR_YOU);
                        output.accept(ZAMItems.SPONGEBOB_SURF_BUGGY);
                        output.accept(ZAMItems.SPONGEBOB_DRUNKEN_SAILOR);
                        output.accept(ZAMItems.SPONGEBOB_CHA_CHA_NOVA);

                        output.accept(CaseStacks.create(ZAMMod.id("stardew_valley_case")));
                        output.accept(ZAMItems.STARDEW_VALLEY_INFINITY_CROWN);
                        output.accept(ZAMItems.STARDEW_VALLEY_STRAW_HAT);
                        output.accept(ZAMItems.STARDEW_VALLEY_DRAGONTOOTH_CUTLASS);
                        output.accept(ZAMItems.STARDEW_VALLEY_IRIDIUM_HOE);
                        output.accept(ZAMItems.STARDEW_VALLEY_IRIDIUM_FISHING_ROD);
                        output.accept(ZAMItems.STARDEW_VALLEY_STARDROP);
                        output.accept(ZAMItems.STARDEW_VALLEY_OVERTURE);
                        output.accept(ZAMItems.STARDEW_VALLEY_SPRING);
                        output.accept(ZAMItems.STARDEW_VALLEY_PELICAN_TOWN);
                        output.accept(ZAMItems.STARDEW_VALLEY_DISTANT_BANJO);
                        output.accept(ZAMItems.STARDEW_VALLEY_SUMMER);
                        output.accept(ZAMItems.STARDEW_VALLEY_ADVENTURE_GUILD);
                        output.accept(ZAMItems.STARDEW_VALLEY_STARDROP_SALOON);
                        output.accept(ZAMItems.STARDEW_VALLEY_LUAU_FESTIVAL);
                        output.accept(ZAMItems.STARDEW_VALLEY_MOONLIGHT_JELLYFISH);
                        output.accept(ZAMItems.STARDEW_VALLEY_FALL);
                        output.accept(ZAMItems.STARDEW_VALLEY_LIBRARY);
                        output.accept(ZAMItems.STARDEW_VALLEY_FAIR);
                        output.accept(ZAMItems.STARDEW_VALLEY_SPIRITS_EVE);
                        output.accept(ZAMItems.STARDEW_VALLEY_WINTER);
                        output.accept(ZAMItems.STARDEW_VALLEY_WINTER_FESTIVAL);
                        output.accept(ZAMItems.STARDEW_VALLEY_COUNTRY_SHOP);
                        output.accept(ZAMItems.STARDEW_VALLEY_CALICO_DESERT);
                        output.accept(ZAMItems.STARDEW_VALLEY_CRYSTAL_BELLS);
                        output.accept(ZAMItems.STARDEW_VALLEY_FLICKER);
                        output.accept(ZAMItems.STARDEW_VALLEY_DEEP_WOODS);
                        output.accept(ZAMItems.STARDEW_VALLEY_NIGHT_MARKET);
                        output.accept(ZAMItems.STARDEW_VALLEY_SUBMARINE);
                        output.accept(ZAMItems.STARDEW_VALLEY_MOVIE_THEATER);
                        output.accept(ZAMItems.STARDEW_VALLEY_GINGER_ISLAND);
                        output.accept(ZAMItems.STARDEW_VALLEY_LEOS_SONG);

                    }).build());

    public static void registerItemGroups() {
        ZAMMod.LOGGER.info("Registering Item Groups for " + ZAMMod.MOD_ID);
    }
}
