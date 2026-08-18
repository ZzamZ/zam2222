package net.ron.zam.registry;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.ron.zam.ZAMMod;
import net.ron.zam.api.casesystem.cases.CaseOpenData;
import net.ron.zam.common.gui.cases.CaseMenu;
import net.ron.zam.common.gui.record_rack.RecordRackMenu;

public class ZAMMenuTypes {

    public static final MenuType<CaseMenu> CASE = Registry.register(BuiltInRegistries.MENU, ZAMMod.id("case_menu"), new ExtendedMenuType<>(CaseMenu::new, CaseOpenData.STREAM_CODEC));
    public static final MenuType<RecordRackMenu> RECORD_RACK = register("record_rack_menu", RecordRackMenu::new);

    public static <T extends AbstractContainerMenu> MenuType<T> register(String name, MenuType.MenuSupplier<T> factory) {
        return Registry.register(
                BuiltInRegistries.MENU,
                ZAMMod.id(name),
                new MenuType<>(factory, FeatureFlags.VANILLA_SET)
        );
    }

    public static void registerMenuTypes() {
        ZAMMod.LOGGER.info("Registering Menu Types for " + ZAMMod.MOD_ID);
    }
}