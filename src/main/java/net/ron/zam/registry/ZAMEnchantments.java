package net.ron.zam.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.ron.zam.ZAMMod;

public final class ZAMEnchantments {
    public static final ResourceKey<Enchantment> MOTHER_CATCH = ResourceKey.create(Registries.ENCHANTMENT, ZAMMod.id("mother_catch"));

    public static void registerEnchantments() {
        ZAMMod.LOGGER.info("Registering Enchantments for " + ZAMMod.MOD_ID);
    }
}