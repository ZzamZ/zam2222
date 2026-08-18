package net.ron.zam.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ScrollWheelHandler;
import net.minecraft.client.gui.BundleMouseActions;
import net.minecraft.client.gui.ItemSlotMouseAction;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ServerboundSelectBundleItemPacket;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.ron.zam.common.item.RecordSleeveItem;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(BundleMouseActions.class)
public abstract class BundleMouseActionsMixin implements ItemSlotMouseAction {
    @Shadow private Minecraft minecraft;
    @Shadow private ScrollWheelHandler scrollWheelHandler;

    @Inject(method = "matches", at = @At("HEAD"), cancellable = true)
    private void ZAMMod$matches(Slot slot, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = slot.getItem();
        if (!stack.isEmpty() && stack.getItem() instanceof RecordSleeveItem) {
            cir.setReturnValue(true); // treat our sleeve like a bundle for mouse actions
        }
    }

    @Inject(method = "onMouseScrolled", at = @At("HEAD"), cancellable = true)
    private void ZAMMod$onMouseScrolled(double mouseX, double mouseY, int slotIndex, ItemStack stack,
                                            CallbackInfoReturnable<Boolean> cir) {
        if (!(stack.getItem() instanceof RecordSleeveItem)) return;

        int total = BundleItem.getNumberOfItemsToShow(stack);
        if (total == 0) { cir.setReturnValue(false); return; }

        Vector2i delta = this.scrollWheelHandler.onMouseScroll(mouseX, mouseY);
        int step = (delta.y == 0 ? -delta.x : delta.y);

        // reverse scroll direction ONLY for the record sleeve
        step = -step;

        int cur = BundleItem.getSelectedItemIndex(stack);
        int next = ScrollWheelHandler.getNextScrollWheelSelection(step, cur, total);

        if (next != cur) {
            BundleItem.toggleSelectedItem(stack, next);
            ClientPacketListener conn = this.minecraft.getConnection();
            if (conn != null) conn.send(new ServerboundSelectBundleItemPacket(slotIndex, next));
        }

        cir.setReturnValue(true);
    }
}
