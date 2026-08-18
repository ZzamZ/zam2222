package net.ron.zam.api.casesystem;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public class ReadOnlyInventory implements Container {
    private final ItemStack[] items;

    public ReadOnlyInventory(int size) {
        this.items = new ItemStack[size];
        for (int i = 0; i < size; i++) {
            this.items[i] = ItemStack.EMPTY;
        }
    }

    @Override
    public int getContainerSize() {
        return this.items.length;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return index >= 0 && index < this.items.length ? this.items[index] : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        if (!this.items[index].isEmpty()) {
            ItemStack itemstack;

            if (this.items[index].getCount() <= count) {
                itemstack = this.items[index];
                this.items[index] = ItemStack.EMPTY;
            } else {
                itemstack = this.items[index].split(count);

                if (this.items[index].isEmpty()) {
                    this.items[index] = ItemStack.EMPTY;
                }
            }

            this.setChanged();
            return itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if (!this.items[index].isEmpty()) {
            ItemStack itemstack = this.items[index];
            this.items[index] = ItemStack.EMPTY;
            return itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (index >= 0 && index < this.items.length) {
            this.items[index] = stack;
            if (!stack.isEmpty() && stack.getCount() > this.getMaxStackSize()) {
                stack.setCount(this.getMaxStackSize());
            }
            this.setChanged();
        }
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        Arrays.fill(this.items, ItemStack.EMPTY);
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }
}