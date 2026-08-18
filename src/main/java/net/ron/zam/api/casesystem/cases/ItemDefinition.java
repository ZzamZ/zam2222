package net.ron.zam.api.casesystem.cases;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record ItemDefinition(Identifier id, int count) {

    public ItemDefinition(Identifier id) {
        this(id, 1);
    }

    public ItemStack createStack(HolderLookup.Provider registries) {
        ResourceKey<Item> key = ResourceKey.create(
                Registries.ITEM,
                this.id
        );

        Item item = registries.lookupOrThrow(Registries.ITEM)
                .get(key)
                .orElseThrow(() -> new IllegalArgumentException("Unknown item: " + this.id))
                .value();

        return new ItemStack(item, this.count);
    }
}