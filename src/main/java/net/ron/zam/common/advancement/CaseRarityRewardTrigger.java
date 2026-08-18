package net.ron.zam.common.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.ron.zam.api.rarity.Rarity;
import net.ron.zam.registry.ZAMCriteriaTriggers;

import java.util.Locale;
import java.util.Optional;

public class CaseRarityRewardTrigger extends SimpleCriterionTrigger<CaseRarityRewardTrigger.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, ItemStack stack, Identifier caseId, Rarity rarity) {
        this.trigger(player, instance -> instance.matches(stack, caseId, rarity));
    }

    public record TriggerInstance(
            Optional<ContextAwarePredicate> player,
            Optional<ItemPredicate> item,
            Optional<Identifier> id,
            Optional<Rarity> rarity
    ) implements SimpleInstance {

        private static final Codec<Rarity> RARITY_CODEC = Codec.STRING.xmap(
                value -> Rarity.valueOf(value.toUpperCase(Locale.ROOT)),
                value -> value.name().toLowerCase(Locale.ROOT)
        );

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                ItemPredicate.CODEC.optionalFieldOf("item").forGetter(TriggerInstance::item),
                Identifier.CODEC.optionalFieldOf("id").forGetter(TriggerInstance::id),
                RARITY_CODEC.optionalFieldOf("rarity").forGetter(TriggerInstance::rarity)
        ).apply(instance, TriggerInstance::new));

        public boolean matches(ItemStack stack, Identifier caseId, Rarity actual) {
            if (item.isPresent() && !item.get().test(stack)) return false;
            if (id.isPresent() && !id.get().equals(caseId)) return false;
            return rarity.map(value -> value == actual).orElse(actual == Rarity.ULTRA_RARE);
        }

        public static Criterion<TriggerInstance> ultra() {
            return ZAMCriteriaTriggers.CASE_RARITY_REWARD.createCriterion(
                    new TriggerInstance(
                            Optional.empty(),
                            Optional.empty(),
                            Optional.empty(),
                            Optional.of(Rarity.ULTRA_RARE)
                    )
            );
        }

        @Override
        public Optional<ContextAwarePredicate> player() {
            return player;
        }
    }
}