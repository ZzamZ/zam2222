package net.ron.zam.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.ron.zam.ZAMMod;

public class ZAMStats {

    public static final Stat<?> CASES_OPENED = makeCustomStat("cases_opened");

    private static Stat<?> makeCustomStat(String string) {
        Identifier identifier = Identifier.fromNamespaceAndPath(ZAMMod.MOD_ID, string);
        Identifier newStat = Registry.register(BuiltInRegistries.CUSTOM_STAT, string, identifier);

        return Stats.CUSTOM.get(newStat, StatFormatter.DEFAULT);
    }

    public static void registerStats() {
        ZAMMod.LOGGER.info("Registering Stats for " + ZAMMod.MOD_ID);
    }
}
