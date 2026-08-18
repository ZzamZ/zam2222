package net.ron.zam.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.ron.zam.ZAMMod;

public final class ZAMWolfVariants {

    public static final ResourceKey<WolfVariant> HONEY =
            ResourceKey.create(Registries.WOLF_VARIANT, ZAMMod.id("honey"));

    private ZAMWolfVariants() {
    }
}