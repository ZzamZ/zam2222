package net.ron.zam.api.casesystem.cases;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.ron.zam.common.gui.cases.CaseMenu;

public class CaseMenuProvider implements ExtendedMenuProvider<CaseOpenData> {
    private final CaseEntry entry;

    public CaseMenuProvider(CaseEntry entry) {
        this.entry = entry;
    }

    @Override
    public CaseOpenData getScreenOpeningData(ServerPlayer player) {
        return CaseOpenData.of(this.entry);
    }

    @Override
    public Component getDisplayName() {
        return this.entry.title();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new CaseMenu(id, inventory, player, this.entry);
    }
}