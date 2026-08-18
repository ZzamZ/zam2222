package net.ron.zam.registry;

import net.minecraft.advancements.triggers.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.ron.zam.ZAMMod;
import net.ron.zam.common.advancement.CaseCompletitionTrigger;
import net.ron.zam.common.advancement.CaseRarityRewardTrigger;
import net.ron.zam.common.advancement.CaseRewardTrigger;
import net.ron.zam.common.advancement.FishAmountTrigger;


public class ZAMCriteriaTriggers {
    public static final CaseRewardTrigger CASE_REWARD = register(
            "case_reward",
            new CaseRewardTrigger()
    );

    public static final CaseCompletitionTrigger CASE_COMPLETITION = register(
            "case_completition",
            new CaseCompletitionTrigger()
    );

    public static final CaseRarityRewardTrigger CASE_RARITY_REWARD = register(
            "case_rarity_reward",
            new CaseRarityRewardTrigger()
    );

    public static final FishAmountTrigger FISH_AMOUNT = register(
            "fish_amount",
            new FishAmountTrigger()
    );

    public static <T extends CriterionTrigger<?>> T register(String name, T trigger) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, ZAMMod.id(name), trigger);
    }

    public static void registerCriteriaTriggers() {
        ZAMMod.LOGGER.info("Registering Criteria Triggers for " + ZAMMod.MOD_ID);
    }
}
