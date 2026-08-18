package net.ron.zam.common.gui.cases;

import com.google.gson.JsonParser;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.ron.zam.api.casesystem.BaseLootBoxMenu;
import net.ron.zam.api.casesystem.cases.CaseEntry;
import net.ron.zam.api.casesystem.cases.CaseJsonLoader;
import net.ron.zam.api.casesystem.cases.CaseOpenData;
import net.ron.zam.registry.ZAMMenuTypes;

public class CaseMenu extends BaseLootBoxMenu<CaseMenu> {

    public CaseMenu(int id, Inventory inventory, CaseOpenData data) {
        this(id, inventory, inventory.player, CaseJsonLoader.load(data.caseId(), JsonParser.parseString(data.json()).getAsJsonObject()));
    }

    public CaseMenu(int id, Inventory inventory, Player player, CaseEntry entry) {
        super(ZAMMenuTypes.CASE, id, inventory, entry, player);
    }
}