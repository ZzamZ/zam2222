package net.ron.zam.common.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.ron.zam.registry.ZAMCriteriaTriggers;

import java.util.Optional;

public class FishAmountTrigger extends SimpleCriterionTrigger<FishAmountTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, int amount) {
        this.trigger(player, instance -> instance.matches(amount));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, int amount) implements SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                        Codec.INT.fieldOf("amount").forGetter(TriggerInstance::amount)
                ).apply(instance, TriggerInstance::new)
        );

        public boolean matches(int actualAmount) {
            return actualAmount >= this.amount;
        }

        @Override
        public Optional<ContextAwarePredicate> player() {
            return player;
        }

        public static Criterion<TriggerInstance> fishAmount(int amount) {
            return ZAMCriteriaTriggers.FISH_AMOUNT.createCriterion(new TriggerInstance(Optional.empty(), amount));
        }
    }
}
