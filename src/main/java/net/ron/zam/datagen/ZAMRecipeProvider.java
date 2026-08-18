package net.ron.zam.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.Blocks;
import net.ron.zam.ZAMMod;
import net.ron.zam.registry.ZAMBlocks;
import net.ron.zam.registry.ZAMItems;
import net.ron.zam.registry.ZAMTags;

import java.util.concurrent.CompletableFuture;

public class ZAMRecipeProvider extends FabricRecipeProvider {
    public ZAMRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
        return new RecipeProvider(provider, recipeOutput) {
            @Override
            public void buildRecipes() {
                shapeless(RecipeCategory.MISC, ZAMItems.CASE_KEY)
                        .requires(Items.DIAMOND)
                        .requires(Items.GOLD_NUGGET)
                        .unlockedBy("has_case", has(ZAMTags.CASES))
                        .save(output);

                shaped(RecipeCategory.MISC, ZAMBlocks.OAK_RECORD_RACK)
                        .pattern("SSS")
                        .pattern("WWW")
                        .define('S', Items.STICK)
                        .define('W', Blocks.OAK_PLANKS)
                        .unlockedBy("has_record_for_oak_record_rack", has(ConventionalItemTags.MUSIC_DISCS))
                        .save(output);

                shaped(RecipeCategory.MISC, ZAMBlocks.SPRUCE_RECORD_RACK)
                        .pattern("SSS")
                        .pattern("WWW")
                        .define('S', Items.STICK)
                        .define('W', Blocks.SPRUCE_PLANKS)
                        .unlockedBy("has_record_for_spruce_record_rack", has(ConventionalItemTags.MUSIC_DISCS))
                        .save(output);

                shaped(RecipeCategory.MISC, ZAMBlocks.BIRCH_RECORD_RACK)
                        .pattern("SSS")
                        .pattern("WWW")
                        .define('S', Items.STICK)
                        .define('W', Blocks.BIRCH_PLANKS)
                        .unlockedBy("has_record_for_birch_record_rack", has(ConventionalItemTags.MUSIC_DISCS))
                        .save(output);

                shaped(RecipeCategory.MISC, ZAMBlocks.JUNGLE_RECORD_RACK)
                        .pattern("SSS")
                        .pattern("WWW")
                        .define('S', Items.STICK)
                        .define('W', Blocks.JUNGLE_PLANKS)
                        .unlockedBy("has_record_for_jungle_record_rack", has(ConventionalItemTags.MUSIC_DISCS))
                        .save(output);

                shaped(RecipeCategory.MISC, ZAMBlocks.ACACIA_RECORD_RACK)
                        .pattern("SSS")
                        .pattern("WWW")
                        .define('S', Items.STICK)
                        .define('W', Blocks.ACACIA_PLANKS)
                        .unlockedBy("has_record_for_acacia_record_rack", has(ConventionalItemTags.MUSIC_DISCS))
                        .save(output);

                shaped(RecipeCategory.MISC, ZAMBlocks.DARK_OAK_RECORD_RACK)
                        .pattern("SSS")
                        .pattern("WWW")
                        .define('S', Items.STICK)
                        .define('W', Blocks.DARK_OAK_PLANKS)
                        .unlockedBy("has_record_for_dark_oak_record_rack", has(ConventionalItemTags.MUSIC_DISCS))
                        .save(output);

                shaped(RecipeCategory.MISC, ZAMBlocks.MANGROVE_RECORD_RACK)
                        .pattern("SSS")
                        .pattern("WWW")
                        .define('S', Items.STICK)
                        .define('W', Blocks.MANGROVE_PLANKS)
                        .unlockedBy("has_record_for_mangrove_record_rack", has(ConventionalItemTags.MUSIC_DISCS))
                        .save(output);

                shaped(RecipeCategory.MISC, ZAMBlocks.CHERRY_RECORD_RACK)
                        .pattern("SSS")
                        .pattern("WWW")
                        .define('S', Items.STICK)
                        .define('W', Blocks.CHERRY_PLANKS)
                        .unlockedBy("has_record_for_cherry_record_rack", has(ConventionalItemTags.MUSIC_DISCS))
                        .save(output);

                shaped(RecipeCategory.MISC, ZAMBlocks.PALE_OAK_RECORD_RACK)
                        .pattern("SSS")
                        .pattern("WWW")
                        .define('S', Items.STICK)
                        .define('W', Blocks.PALE_OAK_PLANKS)
                        .unlockedBy("has_record_for_pale_oak_record_rack", has(ConventionalItemTags.MUSIC_DISCS))
                        .save(output);

                shaped(RecipeCategory.MISC, ZAMBlocks.BAMBOO_RECORD_RACK)
                        .pattern("SSS")
                        .pattern("WWW")
                        .define('S', Items.STICK)
                        .define('W', Blocks.BAMBOO_PLANKS)
                        .unlockedBy("has_record_for_bamboo_record_rack", has(ConventionalItemTags.MUSIC_DISCS))
                        .save(output);

                shaped(RecipeCategory.MISC, ZAMBlocks.CRIMSON_RECORD_RACK)
                        .pattern("SSS")
                        .pattern("WWW")
                        .define('S', Items.STICK)
                        .define('W', Blocks.CRIMSON_PLANKS)
                        .unlockedBy("has_record_for_crimson_record_rack", has(ConventionalItemTags.MUSIC_DISCS))
                        .save(output);

                shaped(RecipeCategory.MISC, ZAMBlocks.WARPED_RECORD_RACK)
                        .pattern("SSS")
                        .pattern("WWW")
                        .define('S', Items.STICK)
                        .define('W', Blocks.WARPED_PLANKS)
                        .unlockedBy("has_record_for_warped_record_rack", has(ConventionalItemTags.MUSIC_DISCS))
                        .save(output);

                shaped(RecipeCategory.MISC, ZAMItems.RECORD_SLEEVE)
                        .pattern("PPP")
                        .pattern("PEP")
                        .pattern("PPP")
                        .define('P', Items.PAPER)
                        .define('E', Items.EMERALD)
                        .unlockedBy("has_record_for_record_sleeve", has(ConventionalItemTags.MUSIC_DISCS))
                        .save(output);

                shaped(RecipeCategory.MISC, ZAMItems.MUSIC_BOX)
                        .pattern("  G")
                        .pattern("RDE")
                        .pattern("PPP")
                        .define('P', ItemTags.PLANKS)
                        .define('R', Items.REPEATER)
                        .define('D', Items.DIAMOND)
                        .define('E', Items.ECHO_SHARD)
                        .define('G', Items.GOLD_INGOT)
                        .unlockedBy("has_record_for_music_box", has(ConventionalItemTags.MUSIC_DISCS))
                        .save(output);

                SimpleCookingRecipeBuilder.smelting(Ingredient.of(ZAMItems.CORN), RecipeCategory.FOOD, CookingBookCategory.FOOD, ZAMItems.CORN_ON_THE_COB, 0.35F, 200
                        ).unlockedBy("has_corn", has(ZAMItems.CORN)).save(output);

                SimpleCookingRecipeBuilder.smoking(Ingredient.of(ZAMItems.CORN), RecipeCategory.FOOD, ZAMItems.CORN_ON_THE_COB, 0.35F, 100
                        ).unlockedBy("has_corn", has(ZAMItems.CORN)).save(output, ResourceKey.create(Registries.RECIPE, ZAMMod.id("corn_on_the_cob_from_smoking")));

                SimpleCookingRecipeBuilder.smelting(Ingredient.of(ZAMItems.CORN_KERNELS), RecipeCategory.FOOD, CookingBookCategory.FOOD, ZAMItems.POPCORN, 0.35F, 200
                        ).unlockedBy("has_corn_kernels", has(ZAMItems.CORN_KERNELS)).save(output);

                SimpleCookingRecipeBuilder.smoking(Ingredient.of(ZAMItems.CORN_KERNELS), RecipeCategory.FOOD, ZAMItems.POPCORN, 0.35F, 100
                        ).unlockedBy("has_corn_kernels", has(ZAMItems.CORN_KERNELS)).save(output, ResourceKey.create(Registries.RECIPE, ZAMMod.id("popcorn_from_smoking")));
            }
        };
    }

    @Override
    public String getName() {
        return "ZAM Recipes";
    }
}

