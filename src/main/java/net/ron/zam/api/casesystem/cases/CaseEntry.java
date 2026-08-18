package net.ron.zam.api.casesystem.cases;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;

public record CaseEntry(Identifier id, Component title, ItemDefinition keyItem, Identifier itemModel, Identifier texture, int rows, List<CaseReward> possibleRewards, String sourceJson) {
    public CaseEntry {
        possibleRewards = List.copyOf(possibleRewards);
        rows = Math.max(1, Math.min(rows, 6));
    }
}