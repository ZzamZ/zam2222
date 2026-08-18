package net.ron.zam.common.gui.record_rack;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.ron.zam.common.block.record_rack.RecordRackBlockEntity;
import net.ron.zam.registry.ZAMMenuTypes;

public class RecordRackMenu extends AbstractContainerMenu {

    private static final int RACK_SLOTS = 7;

    private final Container rack;
    private final ContainerLevelAccess access;

    public RecordRackMenu(int id, Inventory inv) {
        this(id, inv, new SimpleContainer(RACK_SLOTS));
    }

    public RecordRackMenu(int id, Inventory inv, Container rack) {
        super(ZAMMenuTypes.RECORD_RACK, id);
        this.rack = rack;

        if (rack instanceof RecordRackBlockEntity be) {
            this.access = ContainerLevelAccess.create(be.getLevel(), be.getBlockPos());
        } else {
            this.access = ContainerLevelAccess.NULL;
        }

        int startX = 26;
        int y = 20;

        for (int i = 0; i < RACK_SLOTS; i++) {
            final int slotIndex = i;
            this.addSlot(new Slot(rack, slotIndex, startX + slotIndex * 18, y) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return isRackItem(stack);
                }

                @Override
                public boolean mayPickup(Player player) {
                    return true;
                }
            });
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 52 + row * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inv, i, 8 + i * 18, 110));
        }
    }

    private static boolean isRackItem(ItemStack stack) {
        return stack.has(DataComponents.JUKEBOX_PLAYABLE);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();

        if (index < RACK_SLOTS) {
            slot.set(ItemStack.EMPTY);
            slot.setChanged();

            if (!player.getInventory().add(copy)) {
                slot.set(copy);
                slot.setChanged();
                return ItemStack.EMPTY;
            }

            return copy;
        }

        if (!isRackItem(stack)) {
            return ItemStack.EMPTY;
        }

        for (int i = 0; i < RACK_SLOTS; i++) {
            Slot rackSlot = this.slots.get(i);
            if (!rackSlot.hasItem()) {
                rackSlot.set(stack.split(1));
                rackSlot.setChanged();

                if (stack.isEmpty()) {
                    slot.set(ItemStack.EMPTY);
                } else {
                    slot.setChanged();
                }

                return copy;
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.access.evaluate((level, pos) ->
                        level.getBlockEntity(pos) instanceof RecordRackBlockEntity &&
                                player.distanceToSqr(
                                        pos.getX() + 0.5,
                                        pos.getY() + 0.5,
                                        pos.getZ() + 0.5
                                ) <= 64.0,
                true
        );
    }
}