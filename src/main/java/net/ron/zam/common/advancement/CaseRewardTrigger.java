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
import net.ron.zam.registry.ZAMCriteriaTriggers;

import java.util.Optional;

public class CaseRewardTrigger extends SimpleCriterionTrigger<CaseRewardTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, ItemStack itemStack, Identifier id) {
        this.trigger(player, instance -> instance.matches(itemStack, id));
    }

    public record TriggerInstance(
            Optional<ContextAwarePredicate> player,
            Optional<ItemPredicate> item,
            Optional<Identifier> id
    ) implements SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                        ItemPredicate.CODEC.optionalFieldOf("item").forGetter(TriggerInstance::item),
                        Identifier.CODEC.optionalFieldOf("id").forGetter(TriggerInstance::id)
                ).apply(instance, TriggerInstance::new)
        );

        public boolean matches(ItemStack itemStack, Identifier id) {
            if (this.item.isPresent() && !this.item.get().test(itemStack)) return false;
            return this.id.isEmpty() || this.id.get().equals(id);
        }

        public static Criterion<TriggerInstance> anyCaseItem() {
            return ZAMCriteriaTriggers.CASE_REWARD.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
        }

        @Override
        public Optional<ContextAwarePredicate> player() {
            return this.player;
        }
    }
}
