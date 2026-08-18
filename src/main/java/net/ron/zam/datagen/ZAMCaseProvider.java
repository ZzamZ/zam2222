package net.ron.zam.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.Identifier;
import net.ron.zam.ZAMMod;
import net.ron.zam.api.casesystem.cases.CaseBuilder;
import net.ron.zam.api.rarity.Rarity;
import net.ron.zam.registry.ZAMItems;
import net.ron.zam.registry.ZAMTags;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ZAMCaseProvider implements DataProvider {
    private final FabricPackOutput output;
    private final Map<String, JsonElement> cases = new LinkedHashMap<>();
    private final Map<String, JsonElement> recipes = new LinkedHashMap<>();
    private final Map<String, JsonElement> itemDefinitions = new LinkedHashMap<>();
    private final Map<String, JsonElement> itemModels = new LinkedHashMap<>();

    public ZAMCaseProvider(FabricPackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        generateCases();

        return CompletableFuture.allOf(
                saveAll(cache, this.cases, "data/zam/cases/"),
                saveAll(cache, this.recipes, "data/zam/recipe/"),
                saveAll(cache, this.itemDefinitions, "assets/zam/items/"),
                saveAll(cache, this.itemModels, "assets/zam/models/item/")
        );
    }

    private void generateCases() {
        add(
                "castle_crashers_case",
                ZAMTags.CASTLE_CRASHERS_MUSIC_DISCS.location(),
                new CaseBuilder()
                        .title("Castle Crashers Case")
                        .keyItem(ZAMItems.CASE_KEY)
                        .itemModel(ZAMMod.id("castle_crashers_case"))
                        .texture(ZAMMod.id("textures/gui/case_two.png"))
                        .rows(2)

                        .reward(ZAMItems.CASTLE_CRASHERS_FOUR_BRAVE_CHAMPS, Rarity.COMMON)
                        .reward(ZAMItems.CASTLE_CRASHERS_FLUTEY, Rarity.COMMON)
                        .reward(ZAMItems.CASTLE_CRASHERS_SPANISH_WALTZ, Rarity.COMMON)
                        .reward(ZAMItems.CASTLE_CRASHERS_RACE_AROUND_THE_WORLD, Rarity.COMMON)
                        .reward(ZAMItems.CASTLE_CRASHERS_JUMPER, Rarity.COMMON)
                        .reward(ZAMItems.CASTLE_CRASHERS_PLEASE_DONT, Rarity.COMMON)
                        .reward(ZAMItems.CASTLE_CRASHERS_THE_ABDUCTION, Rarity.COMMON)
                        .reward(ZAMItems.CASTLE_CRASHERS_WINTER_BLISS, Rarity.COMMON)

                        .reward(ZAMItems.CASTLE_CRASHERS_SPACE_PIRATES, Rarity.UNCOMMON)
                        .reward(ZAMItems.CASTLE_CRASHERS_THE_SHOW, Rarity.UNCOMMON)
                        .reward(ZAMItems.CASTLE_CRASHERS_SIMPLE_SIGHT, Rarity.UNCOMMON)
                        .reward(ZAMItems.CASTLE_CRASHERS_FINAL_CONFRONTATION, Rarity.UNCOMMON)
                        .reward(ZAMItems.CASTLE_CRASHERS_ARCHETYPE, Rarity.UNCOMMON)

                        .reward(ZAMItems.CASTLE_CRASHERS_DARK_SKIES, Rarity.RARE)
                        .reward(ZAMItems.CASTLE_CRASHERS_RAGE_CHAMPIONS, Rarity.RARE)
                        .reward(ZAMItems.CASTLE_CRASHERS_BATTLEBLOCK, Rarity.RARE)

                        .choice(
                                ZAMItems.RED_ICON,
                                Rarity.VERY_RARE,
                                ZAMItems.CASTLE_CRASHERS_RED_KNIGHT_HELMET,
                                ZAMItems.CASTLE_CRASHERS_BLUE_KNIGHT_HELMET,
                                ZAMItems.CASTLE_CRASHERS_GREEN_KNIGHT_HELMET,
                                ZAMItems.CASTLE_CRASHERS_ORANGE_KNIGHT_HELMET
                        )

                        .choice(
                                ZAMItems.GOLD_ICON,
                                Rarity.ULTRA_RARE,
                                ZAMItems.CASTLE_CRASHERS_CHICKEN_SWORD,
                                ZAMItems.CASTLE_CRASHERS_DEMON_SWORD,
                                ZAMItems.CASTLE_CRASHERS_UNICORN_SWORD
                        )
        );

        add(
                "dragon_ball_case",
                ZAMTags.DRAGON_BALL_MUSIC_DISCS.location(),
                new CaseBuilder()
                        .title("Dragon Ball Case")
                        .keyItem(ZAMItems.CASE_KEY)
                        .itemModel(ZAMMod.id("dragon_ball_case"))
                        .texture(ZAMMod.id("textures/gui/case_one.png"))
                        .rows(1)

                        .reward(ZAMItems.DRAGON_BALL_GATEBREAKER, Rarity.COMMON)
                        .reward(ZAMItems.DRAGON_BALL_BROLY_VS_GOGETA, Rarity.COMMON)
                        .reward(ZAMItems.DRAGON_BALL_ULTRA_INSTINCT, Rarity.COMMON)
                        .reward(ZAMItems.DRAGON_BALL_GT_RECAP, Rarity.COMMON)

                        .reward(ZAMItems.DRAGON_BALL_DAN_DAN, Rarity.UNCOMMON)
                        .reward(ZAMItems.DRAGON_BALL_THE_DRINK, Rarity.UNCOMMON)

                        .reward(ZAMItems.DRAGON_BALL_CHA_LA, Rarity.RARE)

                        .choice(
                                ZAMItems.RED_ICON,
                                Rarity.VERY_RARE,
                                ZAMItems.DRAGON_BALL_SUPER_SAIYAN_HAIR
                        )

                        .choice(
                                ZAMItems.GOLD_ICON,
                                Rarity.ULTRA_RARE,
                                ZAMItems.DRAGON_BALL_POWER_POLL,
                                ZAMItems.DRAGON_BALL_SICKLE_OF_SORROW
                        )
        );

        add(
                "deltarune_case",
                ZAMTags.DELTARUNE_MUSIC_DISCS.location(),
                new CaseBuilder()
                        .title("Deltarune Case")
                        .keyItem(ZAMItems.CASE_KEY)
                        .itemModel(ZAMMod.id("deltarune_case"))
                        .texture(ZAMMod.id("textures/gui/case_three.png"))
                        .rows(3)

                        .reward(ZAMItems.DELTARUNE_RUDE_BUSTER, Rarity.COMMON)
                        .reward(ZAMItems.DELTARUNE_LANTERN, Rarity.COMMON)
                        .reward(ZAMItems.DELTARUNE_FIELD_OF_HOPES_AND_DREAMS, Rarity.COMMON)
                        .reward(ZAMItems.DELTARUNE_A_CYBERS_WORLD, Rarity.COMMON)
                        .reward(ZAMItems.DELTARUNE_PANDORA_PALACE, Rarity.COMMON)
                        .reward(ZAMItems.DELTARUNE_PHYSICAL_CHALLENGE, Rarity.COMMON)
                        .reward(ZAMItems.DELTARUNE_WELCOME_TO_THE_GREEN_ROOM, Rarity.COMMON)
                        .reward(ZAMItems.DELTARUNE_CASTLE_FUNK, Rarity.COMMON)
                        .reward(ZAMItems.DELTARUNE_FIREPLACE, Rarity.COMMON)
                        .reward(ZAMItems.DELTARUNE_THE_THIRD_SANCTUARY, Rarity.COMMON)
                        .reward(ZAMItems.DELTARUNE_THOUSAND_CAFE_ZUKAN, Rarity.COMMON)
                        .reward(ZAMItems.DELTARUNE_RUNNING_SKY, Rarity.COMMON)

                        .reward(ZAMItems.DELTARUNE_CHAOS_KING, Rarity.UNCOMMON)
                        .reward(ZAMItems.DELTARUNE_THE_WORLD_REVOLVING, Rarity.UNCOMMON)
                        .reward(ZAMItems.DELTARUNE_SMART_RACE, Rarity.UNCOMMON)
                        .reward(ZAMItems.DELTARUNE_BIG_SHOT, Rarity.UNCOMMON)
                        .reward(ZAMItems.DELTARUNE_TV_WORLD, Rarity.UNCOMMON)
                        .reward(ZAMItems.DELTARUNE_GUARDIAN, Rarity.UNCOMMON)
                        .reward(ZAMItems.DELTARUNE_FLOWER_CASTLE, Rarity.UNCOMMON)
                        .reward(ZAMItems.DELTARUNE_SUNSET_OF_SEVEN_SUNS, Rarity.UNCOMMON)

                        .reward(ZAMItems.DELTARUNE_ATTACK_OF_THE_KILLER_QUEEN, Rarity.RARE)
                        .reward(ZAMItems.DELTARUNE_ITS_TV_TIME, Rarity.RARE)
                        .reward(ZAMItems.DELTARUNE_BLACK_KNIFE, Rarity.RARE)
                        .reward(ZAMItems.DELTARUNE_HAMMER_OF_JUSTICE, Rarity.RARE)
                        .reward(ZAMItems.DELTARUNE_FLOWER_MAN, Rarity.RARE)

                        .choice(
                                ZAMItems.RED_ICON,
                                Rarity.VERY_RARE,
                                ZAMItems.DELTARUNE_TENNA_HEAD
                        )

                        .choice(
                                ZAMItems.GOLD_ICON,
                                Rarity.ULTRA_RARE,
                                ZAMItems.DELTARUNE_BLACK_KNIFE_SWORD,
                                ZAMItems.DELTARUNE_MANE_AXE,
                                ZAMItems.DELTARUNE_HOLY_HALBERD
                        )
        );

        add(
                "hxh_case",
                ZAMTags.HXH_MUSIC_DISCS.location(),
                new CaseBuilder()
                        .title("Hunter x Hunter Case")
                        .keyItem(ZAMItems.CASE_KEY)
                        .itemModel(ZAMMod.id("hxh_case"))
                        .texture(ZAMMod.id("textures/gui/case_one.png"))
                        .rows(1)

                        .reward(ZAMItems.HXH_DEPARTURE, Rarity.COMMON)
                        .reward(ZAMItems.HXH_WORLD_OF_ADVENTURES, Rarity.COMMON)
                        .reward(ZAMItems.HXH_BOYS_BE_COURAGEOUS, Rarity.COMMON)
                        .reward(ZAMItems.HXH_FROM_WHALE_ISLAND, Rarity.COMMON)

                        .reward(ZAMItems.HXH_HISOKA_THEME, Rarity.UNCOMMON)
                        .reward(ZAMItems.HXH_HUNTING_FOR_YOUR_DREAM, Rarity.UNCOMMON)

                        .reward(ZAMItems.HXH_ALL_I_NEED_IS_MONEY, Rarity.RARE)

                        .choice(
                                ZAMItems.RED_ICON,
                                Rarity.VERY_RARE,
                                ZAMItems.HXH_GONS_HAIR
                        )

                        .choice(
                                ZAMItems.GOLD_ICON,
                                Rarity.ULTRA_RARE,
                                ZAMItems.HXH_GONS_FISHING_ROD
                        )
        );

        add(
                "omori_case",
                ZAMTags.OMORI_MUSIC_DISCS.location(),
                new CaseBuilder()
                        .title("OMORI Case")
                        .keyItem(ZAMItems.CASE_KEY)
                        .itemModel(ZAMMod.id("omori_case"))
                        .texture(ZAMMod.id("textures/gui/case_two.png"))
                        .rows(2)

                        .reward(ZAMItems.OMORI_TITLE, Rarity.COMMON)
                        .reward(ZAMItems.OMORI_BY_YOUR_SIDE, Rarity.COMMON)
                        .reward(ZAMItems.OMORI_SPACE_BOYFRIENDS_TAPE, Rarity.COMMON)
                        .reward(ZAMItems.OMORI_I_PREFER_MY_PIZZA, Rarity.COMMON)
                        .reward(ZAMItems.OMORI_SWEET_PARALYSIS, Rarity.COMMON)
                        .reward(ZAMItems.OMORI_LOST_LIBRARY, Rarity.COMMON)
                        .reward(ZAMItems.OMORI_A_HOME_FOR_FLOWERS, Rarity.COMMON)
                        .reward(ZAMItems.OMORI_OMORI, Rarity.COMMON)

                        .reward(ZAMItems.OMORI_FINDING_SHAPES_IN_THE_CLOUDS, Rarity.UNCOMMON)
                        .reward(ZAMItems.OMORI_WANDERING_ROSE, Rarity.UNCOMMON)
                        .reward(ZAMItems.OMORI_WORLDS_END_VALENTINE, Rarity.UNCOMMON)
                        .reward(ZAMItems.OMORI_GOLDENVENGEANCE, Rarity.UNCOMMON)
                        .reward(ZAMItems.OMORI_BREADY_STEADY_GO, Rarity.UNCOMMON)

                        .reward(ZAMItems.OMORI_YOU_WERE_WRONG_GO_BACK, Rarity.RARE)
                        .reward(ZAMItems.OMORI_DUET, Rarity.RARE)
                        .reward(ZAMItems.OMORI_MY_TIME, Rarity.RARE)

                        .choice(
                                ZAMItems.RED_ICON,
                                Rarity.VERY_RARE,
                                ZAMItems.OMORI_FLOWER_CROWN
                        )

                        .choice(
                                ZAMItems.GOLD_ICON,
                                Rarity.ULTRA_RARE,
                                ZAMItems.OMORI_SPIKED_BAT,
                                ZAMItems.OMORI_LOL_SWORD
                        )
        );

        add(
                "spongebob_case",
                ZAMTags.SPONGEBOB_MUSIC_DISCS.location(),
                new CaseBuilder()
                        .title("SpongeBob Case")
                        .keyItem(ZAMItems.CASE_KEY)
                        .itemModel(ZAMMod.id("spongebob_case"))
                        .texture(ZAMMod.id("textures/gui/case_two.png"))
                        .rows(2)

                        .reward(ZAMItems.SPONGEBOB_KRUSTY_KRAB, Rarity.COMMON)
                        .reward(ZAMItems.SPONGEBOB_PUKA_A, Rarity.COMMON)
                        .reward(ZAMItems.SPONGEBOB_HAWAIIAN_TRAIN, Rarity.COMMON)
                        .reward(ZAMItems.SPONGEBOB_CHILL_OUT, Rarity.COMMON)
                        .reward(ZAMItems.SPONGEBOB_MAUI_BEACH, Rarity.COMMON)
                        .reward(ZAMItems.SPONGEBOB_SURF_BUGGY, Rarity.COMMON)
                        .reward(ZAMItems.SPONGEBOB_THE_LINEMAN, Rarity.COMMON)
                        .reward(ZAMItems.SPONGEBOB_DRUNKEN_SAILOR, Rarity.COMMON)

                        .reward(ZAMItems.SPONGEBOB_THE_RAKE, Rarity.UNCOMMON)
                        .reward(ZAMItems.SPONGEBOB_ANDY_ANNORAK, Rarity.UNCOMMON)
                        .reward(ZAMItems.SPONGEBOB_ME_FOR_YOU, Rarity.UNCOMMON)
                        .reward(ZAMItems.SPONGEBOB_CHA_CHA_NOVA, Rarity.UNCOMMON)
                        .reward(ZAMItems.SPONGEBOB_SWEET_VICTORY, Rarity.UNCOMMON)

                        .reward(ZAMItems.SPONGEBOB_GARYS_SONG, Rarity.RARE)
                        .reward(ZAMItems.SPONGEBOB_AWARD_WINNERS_A, Rarity.RARE)
                        .reward(ZAMItems.SPONGEBOB_JELLYFISH_JAM, Rarity.RARE)

                        .choice(
                                ZAMItems.RED_ICON,
                                Rarity.VERY_RARE,
                                ZAMItems.SPONGEBOB_KRUSTY_KRAB_HAT
                        )

                        .choice(
                                ZAMItems.GOLD_ICON,
                                Rarity.ULTRA_RARE,
                                ZAMItems.SPONGEBOB_JELLYFISHING_NET,
                                ZAMItems.SPONGEBOB_GOLDEN_SPATULA,
                                ZAMItems.SPONGEBOB_MARLIN_LANCE
                        )
        );

        add(
                "stardew_valley_case",
                ZAMTags.STARDEW_VALLEY_MUSIC_DISCS.location(),
                new CaseBuilder()
                        .title("Stardew Valley Case")
                        .keyItem(ZAMItems.CASE_KEY)
                        .itemModel(ZAMMod.id("stardew_valley_case"))
                        .texture(ZAMMod.id("textures/gui/case_three.png"))
                        .rows(3)

                        .reward(ZAMItems.STARDEW_VALLEY_OVERTURE, Rarity.COMMON)
                        .reward(ZAMItems.STARDEW_VALLEY_SPRING, Rarity.COMMON)
                        .reward(ZAMItems.STARDEW_VALLEY_SUMMER, Rarity.COMMON)
                        .reward(ZAMItems.STARDEW_VALLEY_ADVENTURE_GUILD, Rarity.COMMON)
                        .reward(ZAMItems.STARDEW_VALLEY_FALL, Rarity.COMMON)
                        .reward(ZAMItems.STARDEW_VALLEY_LIBRARY, Rarity.COMMON)
                        .reward(ZAMItems.STARDEW_VALLEY_FAIR, Rarity.COMMON)
                        .reward(ZAMItems.STARDEW_VALLEY_WINTER, Rarity.COMMON)
                        .reward(ZAMItems.STARDEW_VALLEY_COUNTRY_SHOP, Rarity.COMMON)
                        .reward(ZAMItems.STARDEW_VALLEY_CRYSTAL_BELLS, Rarity.COMMON)
                        .reward(ZAMItems.STARDEW_VALLEY_FLICKER, Rarity.COMMON)
                        .reward(ZAMItems.STARDEW_VALLEY_SUBMARINE, Rarity.COMMON)

                        .reward(ZAMItems.STARDEW_VALLEY_PELICAN_TOWN, Rarity.UNCOMMON)
                        .reward(ZAMItems.STARDEW_VALLEY_STARDROP_SALOON, Rarity.UNCOMMON)
                        .reward(ZAMItems.STARDEW_VALLEY_LUAU_FESTIVAL, Rarity.UNCOMMON)
                        .reward(ZAMItems.STARDEW_VALLEY_CALICO_DESERT, Rarity.UNCOMMON)
                        .reward(ZAMItems.STARDEW_VALLEY_NIGHT_MARKET, Rarity.UNCOMMON)
                        .reward(ZAMItems.STARDEW_VALLEY_DEEP_WOODS, Rarity.UNCOMMON)
                        .reward(ZAMItems.STARDEW_VALLEY_MOVIE_THEATER, Rarity.UNCOMMON)
                        .reward(ZAMItems.STARDEW_VALLEY_LEOS_SONG, Rarity.UNCOMMON)

                        .reward(ZAMItems.STARDEW_VALLEY_DISTANT_BANJO, Rarity.RARE)
                        .reward(ZAMItems.STARDEW_VALLEY_MOONLIGHT_JELLYFISH, Rarity.RARE)
                        .reward(ZAMItems.STARDEW_VALLEY_SPIRITS_EVE, Rarity.RARE)
                        .reward(ZAMItems.STARDEW_VALLEY_WINTER_FESTIVAL, Rarity.RARE)
                        .reward(ZAMItems.STARDEW_VALLEY_GINGER_ISLAND, Rarity.RARE)

                        .choice(
                                ZAMItems.RED_ICON,
                                Rarity.VERY_RARE,
                                ZAMItems.STARDEW_VALLEY_INFINITY_CROWN,
                                ZAMItems.STARDEW_VALLEY_STRAW_HAT
                        )

                        .choice(
                                ZAMItems.GOLD_ICON,
                                Rarity.ULTRA_RARE,
                                ZAMItems.STARDEW_VALLEY_DRAGONTOOTH_CUTLASS,
                                ZAMItems.STARDEW_VALLEY_IRIDIUM_HOE,
                                ZAMItems.STARDEW_VALLEY_IRIDIUM_FISHING_ROD
                        )
        );
    }

    private void add(String id, Identifier craftingTag, CaseBuilder builder) {
        JsonObject caseJson = builder.build();
        String title = caseJson.get("title").getAsString();

        this.cases.put(id, caseJson);
        this.recipes.put(id, createRecipe(id, title, craftingTag));
        this.itemDefinitions.put(id, createItemDefinition(id));
        this.itemModels.put(id, createItemModel(id));
    }

    private JsonObject createRecipe(String id, String title, Identifier craftingTag) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:crafting_shaped");
        json.addProperty("category", "misc");

        JsonArray pattern = new JsonArray();
        pattern.add("CC");
        pattern.add("CC");
        json.add("pattern", pattern);

        JsonObject key = new JsonObject();
        key.addProperty("C", "#" + craftingTag);
        json.add("key", key);

        JsonObject result = new JsonObject();
        result.addProperty("id", ZAMMod.id("case").toString());
        result.addProperty("count", 1);

        JsonObject components = new JsonObject();

        components.addProperty(
                ZAMMod.id("case_id").toString(),
                ZAMMod.id(id).toString()
        );

        components.addProperty(
                "minecraft:item_model",
                ZAMMod.id(id).toString()
        );

        JsonObject itemName = new JsonObject();
        itemName.addProperty("text", title);
        itemName.addProperty("italic", false);

        components.add(
                "minecraft:item_name",
                itemName
        );

        result.add("components", components);
        json.add("result", result);

        return json;
    }

    private JsonObject createItemDefinition(String id) {
        JsonObject root = new JsonObject();
        JsonObject model = new JsonObject();

        model.addProperty("type", "minecraft:model");
        model.addProperty("model", ZAMMod.id("item/" + id).toString());

        root.add("model", model);

        return root;
    }

    private JsonObject createItemModel(String id) {
        JsonObject root = new JsonObject();
        JsonObject textures = new JsonObject();

        root.addProperty("parent", "minecraft:item/generated");
        textures.addProperty("layer0", ZAMMod.id("item/" + id).toString());

        root.add("textures", textures);

        return root;
    }

    private CompletableFuture<?> saveAll(CachedOutput cache, Map<String, JsonElement> entries, String folder) {
        CompletableFuture<?>[] futures = entries.entrySet().stream()
                .map(entry -> {
                    Path path = this.output.getOutputFolder()
                            .resolve(folder + entry.getKey() + ".json");

                    return DataProvider.saveStable(
                            cache,
                            entry.getValue(),
                            path
                    );
                })
                .toArray(CompletableFuture[]::new);

        return CompletableFuture.allOf(futures);
    }

    @Override
    public String getName() {
        return "ZAM Cases";
    }
}