package net.ron.zam.common.gui.cases;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.ron.zam.api.casesystem.BaseLootBoxScreen;

public class CaseScreen extends BaseLootBoxScreen<CaseMenu> {

    public CaseScreen(CaseMenu menu, Inventory inventory, Component title) {
        super(menu, inventory);
    }
}