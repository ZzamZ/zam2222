package net.ron.zam.api.casesystem.cases;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.ron.zam.registry.ZAMComponents;
import net.ron.zam.registry.ZAMItems;

public final class CaseStacks {

    private CaseStacks() {
    }

    public static ItemStack create(Identifier caseId) {
        return create(CaseRewards.getOrThrow(caseId));
    }

    public static ItemStack create(CaseEntry entry) {
        ItemStack stack = new ItemStack(ZAMItems.CASE);

        stack.set(ZAMComponents.CASE_ID, entry.id());
        stack.set(DataComponents.ITEM_MODEL, entry.itemModel());
        stack.set(DataComponents.ITEM_NAME, entry.title());

        return stack;
    }
}