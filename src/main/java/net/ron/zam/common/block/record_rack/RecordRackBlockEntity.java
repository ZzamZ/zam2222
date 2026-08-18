package net.ron.zam.common.block.record_rack;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.ron.zam.common.gui.record_rack.RecordRackMenu;
import net.ron.zam.registry.ZAMBlockEntities;
import org.jetbrains.annotations.Nullable;

public class RecordRackBlockEntity extends BlockEntity implements WorldlyContainer, MenuProvider {

    public static final int SIZE = 7;
    private static final int[] SLOTS = {0, 1, 2, 3, 4, 5, 6};

    private final ItemStack[] items = new ItemStack[SIZE];

    public RecordRackBlockEntity(BlockPos pos, BlockState state) {
        super(ZAMBlockEntities.RECORD_RACK, pos, state);
        for (int i = 0; i < SIZE; i++) {
            items[i] = ItemStack.EMPTY;
        }
    }

    private static boolean isRackItem(ItemStack stack) {
        return !stack.isEmpty()
                && (stack.has(DataComponents.JUKEBOX_PLAYABLE));
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        var list = output.list("Items", CompoundTag.CODEC);

        for (int i = 0; i < SIZE; i++) {
            ItemStack stack = items[i];

            if (!stack.isEmpty()) {
                CompoundTag tag = new CompoundTag();
                tag.putInt("Slot", i);
                tag.put("Item", ItemStack.CODEC.encodeStart(net.minecraft.nbt.NbtOps.INSTANCE, stack).getOrThrow());

                list.add(tag);
            }
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        for (int i = 0; i < SIZE; i++) {
            items[i] = ItemStack.EMPTY;
        }

        var list = input.listOrEmpty("Items", CompoundTag.CODEC);

        list.forEach(tag -> {
            int slot = tag.getInt("Slot").orElse(-1);

            if (slot >= 0 && slot < SIZE) {
                ItemStack stack = ItemStack.CODEC
                        .parse(net.minecraft.nbt.NbtOps.INSTANCE, tag.get("Item"))
                        .getOrThrow();

                if (isRackItem(stack)) {
                    items[slot] = stack;
                }
            }
        });
    }


    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveWithoutMetadata(provider);
    }

    private void sync() {
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public int getContainerSize() {
        return SIZE;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return items[slot];
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = items[slot];
        if (stack.isEmpty()) return ItemStack.EMPTY;

        ItemStack result = stack.split(amount);
        if (stack.isEmpty()) {
            items[slot] = ItemStack.EMPTY;
        }

        sync();
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = items[slot];
        items[slot] = ItemStack.EMPTY;
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (!stack.isEmpty() && !isRackItem(stack)) return;
        items[slot] = stack;
        sync();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return isRackItem(stack);
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < SIZE; i++) {
            items[i] = ItemStack.EMPTY;
        }
        sync();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
        return isRackItem(stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    public boolean stillValid(Player player) {
        return level != null
                && level.getBlockEntity(worldPosition) == this
                && player.distanceToSqr(
                worldPosition.getX() + 0.5,
                worldPosition.getY() + 0.5,
                worldPosition.getZ() + 0.5
        ) <= 64.0;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new RecordRackMenu(id, inv, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("menu.zam.record_rack");
    }
}
