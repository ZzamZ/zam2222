package net.ron.zam.registry;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;
import net.ron.zam.ZAMMod;

import java.util.List;
import java.util.Optional;

public class ZAMVillagerTrades {
    public static final ResourceKey<VillagerTrade> CARTOGRAPHER_5_EMERALD_CASTLE_CRASHERS_CASE = createKey("cartographer/5/emerald_castle_crashers_case");
    public static final ResourceKey<VillagerTrade> CARTOGRAPHER_5_EMERALD_DELTARUNE_CASE = createKey("cartographer/5/emerald_deltarune_case");
    public static final ResourceKey<VillagerTrade> CARTOGRAPHER_5_EMERALD_DRAGON_BALL_CASE = createKey("cartographer/5/emerald_dragon_ball_case");
    public static final ResourceKey<VillagerTrade> CARTOGRAPHER_5_EMERALD_HXH_CASE = createKey("cartographer/5/emerald_hxh_case");
    public static final ResourceKey<VillagerTrade> CARTOGRAPHER_5_EMERALD_OMORI_CASE = createKey("cartographer/5/emerald_omori_case");
    public static final ResourceKey<VillagerTrade> CARTOGRAPHER_5_EMERALD_SPONGEBOB_CASE = createKey("cartographer/5/emerald_spongebob_case");
    public static final ResourceKey<VillagerTrade> CARTOGRAPHER_5_EMERALD_STARDEW_VALLEY_CASE = createKey("cartographer/5/emerald_stardew_valley_case");
    public static final ResourceKey<VillagerTrade> WANDERING_TRADER_RARE_EMERALD_CASE_KEY = createKey("wandering_trader/emerald_case_key");

    public static void bootstrap(BootstrapContext<VillagerTrade> context) {
        register(context, CARTOGRAPHER_5_EMERALD_CASTLE_CRASHERS_CASE, caseTrade("castle_crashers_case", 12));
        register(context, CARTOGRAPHER_5_EMERALD_DELTARUNE_CASE, caseTrade("deltarune_case", 12));
        register(context, CARTOGRAPHER_5_EMERALD_DRAGON_BALL_CASE, caseTrade("dragon_ball_case", 12));
        register(context, CARTOGRAPHER_5_EMERALD_HXH_CASE, caseTrade("hxh_case", 12));
        register(context, CARTOGRAPHER_5_EMERALD_OMORI_CASE, caseTrade("omori_case", 12));
        register(context, CARTOGRAPHER_5_EMERALD_SPONGEBOB_CASE, caseTrade("spongebob_case", 12));
        register(context, CARTOGRAPHER_5_EMERALD_STARDEW_VALLEY_CASE, caseTrade("stardew_valley_case", 12));

        register(context, WANDERING_TRADER_RARE_EMERALD_CASE_KEY, new VillagerTrade(
                new TradeCost(Items.EMERALD, 18),
                new ItemStackTemplate(ZAMItems.CASE_KEY),
                3, 10, 0.05f,
                Optional.empty(), List.of()));
    }

    private static VillagerTrade caseTrade(String id, int emeraldCost) {
        return new VillagerTrade(new TradeCost(Items.EMERALD, emeraldCost),
                caseTemplate(ZAMMod.id(id)), 8, 10, 0.05f,
                Optional.empty(), List.of());
    }

    private static ItemStackTemplate caseTemplate(Identifier caseId) {
        DataComponentPatch components = DataComponentPatch.builder()
                .set(ZAMComponents.CASE_ID, caseId)
                .set(DataComponents.ITEM_MODEL, caseId)
                .build();

        return new ItemStackTemplate(ZAMItems.CASE.builtInRegistryHolder(), 1, components);
    }

    private static ResourceKey<VillagerTrade> createKey(String name) {
        return ResourceKey.create(
                Registries.VILLAGER_TRADE,
                ZAMMod.id(name)
        );
    }

    private static void register(BootstrapContext<VillagerTrade> context, ResourceKey<VillagerTrade> key, VillagerTrade trade) {
        context.register(key, trade);
    }
}