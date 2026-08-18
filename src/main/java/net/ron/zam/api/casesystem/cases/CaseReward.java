package net.ron.zam.api.casesystem.cases;

import net.ron.zam.api.rarity.Rarity;

import java.util.List;

public record CaseReward(ItemDefinition display, List<ItemDefinition> rewards, Rarity rarity, boolean choice) {
    public CaseReward {
        rewards = List.copyOf(rewards);
    }

    public boolean isPool() {
        return this.rewards.size() > 1;
    }
}