package net.ron.zam.common.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.ron.zam.registry.ZAMCriteriaTriggers;

import java.util.Optional;

public class CaseCompletitionTrigger extends SimpleCriterionTrigger<CaseCompletitionTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, Identifier id) {
        this.trigger(player, instance -> instance.matches(id));
    }

    public record TriggerInstance(
        Optional<ContextAwarePredicate> player,
        Identifier id
    ) implements SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                    Identifier.CODEC.fieldOf("id").forGetter(TriggerInstance::id)
            ).apply(instance, TriggerInstance::new)
        );

        public boolean matches(Identifier id) {
            return this.id.equals(id);
        }

        public static Criterion<TriggerInstance> fromCase(Identifier id) {
            return ZAMCriteriaTriggers.CASE_COMPLETITION.createCriterion(new TriggerInstance(Optional.empty(), id));
        }

        @Override
        public Optional<ContextAwarePredicate> player() {
            return this.player;
        }
    }
}