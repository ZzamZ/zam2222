package net.ron.zam.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.ron.zam.registry.ZAMItems;
import net.ron.zam.registry.ZAMTags;

import java.util.concurrent.CompletableFuture;

public class ZAMItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public ZAMItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ItemTags.SWORDS)
                .add(ZAMItems.getRK(ZAMItems.MARINE_SWORD))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_CHICKEN_SWORD))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_DEMON_SWORD))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_UNICORN_SWORD))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_BLACK_KNIFE_SWORD))
                .add(ZAMItems.getRK(ZAMItems.OMORI_LOL_SWORD))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_DRAGONTOOTH_CUTLASS));

        tag(ItemTags.SHOVELS)
                .add(ZAMItems.getRK(ZAMItems.MARINE_SHOVEL))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_JELLYFISHING_NET))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_GOLDEN_SPATULA));

        tag(ItemTags.SPEARS)
                .add(ZAMItems.getRK(ZAMItems.MARINE_SPEAR))
                .add(ZAMItems.getRK(ZAMItems.DRAGON_BALL_POWER_POLL))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_MARLIN_LANCE));

        tag(ItemTags.MACE_ENCHANTABLE)
                .add(ZAMItems.getRK(ZAMItems.OMORI_SPIKED_BAT));

        tag(ItemTags.PICKAXES)
                .add(ZAMItems.getRK(ZAMItems.MARINE_PICKAXE));

        tag(ItemTags.AXES)
                .add(ZAMItems.getRK(ZAMItems.MARINE_AXE))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_MANE_AXE))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_HOLY_HALBERD));

        tag(ItemTags.HOES)
                .add(ZAMItems.getRK(ZAMItems.MARINE_HOE))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_IRIDIUM_HOE))
                .add(ZAMItems.getRK(ZAMItems.DRAGON_BALL_SICKLE_OF_SORROW));

        tag(ItemTags.FISHING_ENCHANTABLE)
                .add(ZAMItems.getRK(ZAMItems.HXH_GONS_FISHING_ROD))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_IRIDIUM_FISHING_ROD));

        tag(ItemTags.DURABILITY_ENCHANTABLE)
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_IRIDIUM_FISHING_ROD));

        tag(ItemTags.HEAD_ARMOR)
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_RED_KNIGHT_HELMET))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_BLUE_KNIGHT_HELMET))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_GREEN_KNIGHT_HELMET))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_ORANGE_KNIGHT_HELMET))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_TENNA_HEAD))
                .add(ZAMItems.getRK(ZAMItems.DRAGON_BALL_SUPER_SAIYAN_HAIR))
                .add(ZAMItems.getRK(ZAMItems.HXH_GONS_HAIR))
                .add(ZAMItems.getRK(ZAMItems.OMORI_FLOWER_CROWN))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_KRUSTY_KRAB_HAT))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_INFINITY_CROWN))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_STRAW_HAT))
                .add(ZAMItems.getRK(ZAMItems.FISHERMAN_MASTERY_CAP));

        tag(ConventionalItemTags.MUSIC_DISCS)
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_FOUR_BRAVE_CHAMPS))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_FLUTEY))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_SPANISH_WALTZ))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_RACE_AROUND_THE_WORLD))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_JUMPER))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_SPACE_PIRATES))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_THE_SHOW))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_SIMPLE_SIGHT))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_FINAL_CONFRONTATION))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_ARCHETYPE))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_DARK_SKIES))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_RAGE_CHAMPIONS))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_BATTLEBLOCK))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_PLEASE_DONT))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_THE_ABDUCTION))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_WINTER_BLISS))

                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_LANTERN))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_FIELD_OF_HOPES_AND_DREAMS))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_RUDE_BUSTER))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_CHAOS_KING))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_THE_WORLD_REVOLVING))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_A_CYBERS_WORLD))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_SMART_RACE))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_PANDORA_PALACE))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_ATTACK_OF_THE_KILLER_QUEEN))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_BIG_SHOT))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_PHYSICAL_CHALLENGE))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_WELCOME_TO_THE_GREEN_ROOM))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_TV_WORLD))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_ITS_TV_TIME))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_BLACK_KNIFE))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_CASTLE_FUNK))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_FIREPLACE))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_HAMMER_OF_JUSTICE))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_THE_THIRD_SANCTUARY))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_GUARDIAN))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_FLOWER_CASTLE))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_THOUSAND_CAFE_ZUKAN))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_RUNNING_SKY))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_SUNSET_OF_SEVEN_SUNS))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_FLOWER_MAN))

                .add(ZAMItems.getRK(ZAMItems.DRAGON_BALL_GT_RECAP))
                .add(ZAMItems.getRK(ZAMItems.DRAGON_BALL_THE_DRINK))
                .add(ZAMItems.getRK(ZAMItems.DRAGON_BALL_BROLY_VS_GOGETA))
                .add(ZAMItems.getRK(ZAMItems.DRAGON_BALL_GATEBREAKER))
                .add(ZAMItems.getRK(ZAMItems.DRAGON_BALL_DAN_DAN))
                .add(ZAMItems.getRK(ZAMItems.DRAGON_BALL_CHA_LA))
                .add(ZAMItems.getRK(ZAMItems.DRAGON_BALL_ULTRA_INSTINCT))

                .add(ZAMItems.getRK(ZAMItems.HXH_DEPARTURE))
                .add(ZAMItems.getRK(ZAMItems.HXH_ALL_I_NEED_IS_MONEY))
                .add(ZAMItems.getRK(ZAMItems.HXH_FROM_WHALE_ISLAND))
                .add(ZAMItems.getRK(ZAMItems.HXH_BOYS_BE_COURAGEOUS))
                .add(ZAMItems.getRK(ZAMItems.HXH_WORLD_OF_ADVENTURES))
                .add(ZAMItems.getRK(ZAMItems.HXH_HUNTING_FOR_YOUR_DREAM))
                .add(ZAMItems.getRK(ZAMItems.HXH_HISOKA_THEME))

                .add(ZAMItems.getRK(ZAMItems.OMORI_TITLE))
                .add(ZAMItems.getRK(ZAMItems.OMORI_BY_YOUR_SIDE))
                .add(ZAMItems.getRK(ZAMItems.OMORI_SPACE_BOYFRIENDS_TAPE))
                .add(ZAMItems.getRK(ZAMItems.OMORI_YOU_WERE_WRONG_GO_BACK))
                .add(ZAMItems.getRK(ZAMItems.OMORI_FINDING_SHAPES_IN_THE_CLOUDS))
                .add(ZAMItems.getRK(ZAMItems.OMORI_I_PREFER_MY_PIZZA))
                .add(ZAMItems.getRK(ZAMItems.OMORI_SWEET_PARALYSIS))
                .add(ZAMItems.getRK(ZAMItems.OMORI_WANDERING_ROSE))
                .add(ZAMItems.getRK(ZAMItems.OMORI_WORLDS_END_VALENTINE))
                .add(ZAMItems.getRK(ZAMItems.OMORI_LOST_LIBRARY))
                .add(ZAMItems.getRK(ZAMItems.OMORI_GOLDENVENGEANCE))
                .add(ZAMItems.getRK(ZAMItems.OMORI_BREADY_STEADY_GO))
                .add(ZAMItems.getRK(ZAMItems.OMORI_A_HOME_FOR_FLOWERS))
                .add(ZAMItems.getRK(ZAMItems.OMORI_OMORI))
                .add(ZAMItems.getRK(ZAMItems.OMORI_DUET))
                .add(ZAMItems.getRK(ZAMItems.OMORI_MY_TIME))

                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_KRUSTY_KRAB))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_THE_LINEMAN))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_PUKA_A))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_AWARD_WINNERS_A))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_ANDY_ANNORAK))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_SWEET_VICTORY))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_JELLYFISH_JAM))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_THE_RAKE))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_HAWAIIAN_TRAIN))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_GARYS_SONG))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_CHILL_OUT))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_MAUI_BEACH))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_ME_FOR_YOU))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_SURF_BUGGY))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_DRUNKEN_SAILOR))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_CHA_CHA_NOVA))

                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_OVERTURE))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_SPRING))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_PELICAN_TOWN))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_DISTANT_BANJO))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_SUMMER))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_ADVENTURE_GUILD))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_STARDROP_SALOON))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_LUAU_FESTIVAL))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_MOONLIGHT_JELLYFISH))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_FALL))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_LIBRARY))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_FAIR))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_SPIRITS_EVE))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_WINTER))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_WINTER_FESTIVAL))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_COUNTRY_SHOP))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_CALICO_DESERT))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_CRYSTAL_BELLS))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_FLICKER))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_DEEP_WOODS))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_NIGHT_MARKET))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_SUBMARINE))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_MOVIE_THEATER))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_GINGER_ISLAND))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_LEOS_SONG));

        tag(ZAMTags.CASTLE_CRASHERS_MUSIC_DISCS)
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_FOUR_BRAVE_CHAMPS))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_FLUTEY))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_SPANISH_WALTZ))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_RACE_AROUND_THE_WORLD))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_JUMPER))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_SPACE_PIRATES))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_THE_SHOW))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_SIMPLE_SIGHT))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_FINAL_CONFRONTATION))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_ARCHETYPE))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_DARK_SKIES))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_RAGE_CHAMPIONS))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_BATTLEBLOCK))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_PLEASE_DONT))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_THE_ABDUCTION))
                .add(ZAMItems.getRK(ZAMItems.CASTLE_CRASHERS_WINTER_BLISS));

        tag(ZAMTags.DRAGON_BALL_MUSIC_DISCS)
                .add(ZAMItems.getRK(ZAMItems.DRAGON_BALL_GT_RECAP))
                .add(ZAMItems.getRK(ZAMItems.DRAGON_BALL_THE_DRINK))
                .add(ZAMItems.getRK(ZAMItems.DRAGON_BALL_BROLY_VS_GOGETA))
                .add(ZAMItems.getRK(ZAMItems.DRAGON_BALL_GATEBREAKER))
                .add(ZAMItems.getRK(ZAMItems.DRAGON_BALL_DAN_DAN))
                .add(ZAMItems.getRK(ZAMItems.DRAGON_BALL_CHA_LA))
                .add(ZAMItems.getRK(ZAMItems.DRAGON_BALL_ULTRA_INSTINCT));

        tag(ZAMTags.SPONGEBOB_MUSIC_DISCS)
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_KRUSTY_KRAB))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_THE_LINEMAN))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_PUKA_A))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_AWARD_WINNERS_A))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_ANDY_ANNORAK))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_SWEET_VICTORY))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_JELLYFISH_JAM))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_THE_RAKE))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_HAWAIIAN_TRAIN))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_GARYS_SONG))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_CHILL_OUT))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_MAUI_BEACH))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_ME_FOR_YOU))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_SURF_BUGGY))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_DRUNKEN_SAILOR))
                .add(ZAMItems.getRK(ZAMItems.SPONGEBOB_CHA_CHA_NOVA));

        tag(ZAMTags.STARDEW_VALLEY_MUSIC_DISCS)
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_OVERTURE))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_SPRING))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_PELICAN_TOWN))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_DISTANT_BANJO))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_SUMMER))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_ADVENTURE_GUILD))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_STARDROP_SALOON))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_LUAU_FESTIVAL))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_MOONLIGHT_JELLYFISH))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_FALL))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_LIBRARY))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_FAIR))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_SPIRITS_EVE))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_WINTER))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_WINTER_FESTIVAL))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_COUNTRY_SHOP))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_CALICO_DESERT))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_CRYSTAL_BELLS))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_FLICKER))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_DEEP_WOODS))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_NIGHT_MARKET))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_SUBMARINE))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_MOVIE_THEATER))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_GINGER_ISLAND))
                .add(ZAMItems.getRK(ZAMItems.STARDEW_VALLEY_LEOS_SONG));

        tag(ZAMTags.OMORI_MUSIC_DISCS)
                .add(ZAMItems.getRK(ZAMItems.OMORI_TITLE))
                .add(ZAMItems.getRK(ZAMItems.OMORI_BY_YOUR_SIDE))
                .add(ZAMItems.getRK(ZAMItems.OMORI_SPACE_BOYFRIENDS_TAPE))
                .add(ZAMItems.getRK(ZAMItems.OMORI_YOU_WERE_WRONG_GO_BACK))
                .add(ZAMItems.getRK(ZAMItems.OMORI_FINDING_SHAPES_IN_THE_CLOUDS))
                .add(ZAMItems.getRK(ZAMItems.OMORI_I_PREFER_MY_PIZZA))
                .add(ZAMItems.getRK(ZAMItems.OMORI_SWEET_PARALYSIS))
                .add(ZAMItems.getRK(ZAMItems.OMORI_WANDERING_ROSE))
                .add(ZAMItems.getRK(ZAMItems.OMORI_WORLDS_END_VALENTINE))
                .add(ZAMItems.getRK(ZAMItems.OMORI_LOST_LIBRARY))
                .add(ZAMItems.getRK(ZAMItems.OMORI_GOLDENVENGEANCE))
                .add(ZAMItems.getRK(ZAMItems.OMORI_BREADY_STEADY_GO))
                .add(ZAMItems.getRK(ZAMItems.OMORI_A_HOME_FOR_FLOWERS))
                .add(ZAMItems.getRK(ZAMItems.OMORI_OMORI))
                .add(ZAMItems.getRK(ZAMItems.OMORI_DUET))
                .add(ZAMItems.getRK(ZAMItems.OMORI_MY_TIME));

        tag(ZAMTags.DELTARUNE_MUSIC_DISCS)
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_LANTERN))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_FIELD_OF_HOPES_AND_DREAMS))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_RUDE_BUSTER))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_CHAOS_KING))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_THE_WORLD_REVOLVING))

                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_A_CYBERS_WORLD))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_SMART_RACE))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_PANDORA_PALACE))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_ATTACK_OF_THE_KILLER_QUEEN))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_BIG_SHOT))

                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_PHYSICAL_CHALLENGE))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_WELCOME_TO_THE_GREEN_ROOM))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_TV_WORLD))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_ITS_TV_TIME))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_BLACK_KNIFE))

                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_CASTLE_FUNK))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_FIREPLACE))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_HAMMER_OF_JUSTICE))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_THE_THIRD_SANCTUARY))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_GUARDIAN))

                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_FLOWER_CASTLE))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_THOUSAND_CAFE_ZUKAN))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_RUNNING_SKY))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_SUNSET_OF_SEVEN_SUNS))
                .add(ZAMItems.getRK(ZAMItems.DELTARUNE_FLOWER_MAN));

        tag(ZAMTags.HXH_MUSIC_DISCS)
                .add(ZAMItems.getRK(ZAMItems.HXH_DEPARTURE))
                .add(ZAMItems.getRK(ZAMItems.HXH_ALL_I_NEED_IS_MONEY))
                .add(ZAMItems.getRK(ZAMItems.HXH_FROM_WHALE_ISLAND))
                .add(ZAMItems.getRK(ZAMItems.HXH_BOYS_BE_COURAGEOUS))
                .add(ZAMItems.getRK(ZAMItems.HXH_WORLD_OF_ADVENTURES))
                .add(ZAMItems.getRK(ZAMItems.HXH_HUNTING_FOR_YOUR_DREAM))
                .add(ZAMItems.getRK(ZAMItems.HXH_HISOKA_THEME));

        tag(ZAMTags.CASES)
                .add(ZAMItems.getRK(ZAMItems.CASE));
    }

    @Override
    public String getName() {
        return "ZAM Item Tags";
    }
}