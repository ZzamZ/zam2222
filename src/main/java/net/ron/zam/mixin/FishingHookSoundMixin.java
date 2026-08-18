package net.ron.zam.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.ron.zam.common.item.caserewards.CaseRewardFishingRodItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FishingHook.class)
public class FishingHookSoundMixin {

    @Redirect(
            method = "catchingFish",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/projectile/FishingHook;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V",
                    remap = false
            ),
            remap = false
    )
    private void zam$replaceBiteSound(FishingHook hook, SoundEvent sound, float volume, float pitch) {
        Player player = hook.getPlayerOwner();

        if (player != null && sound == SoundEvents.FISHING_BOBBER_SPLASH) {
            ItemStack stack = getRodStack(player);

            if (stack.getItem() instanceof CaseRewardFishingRodItem rod) {
                hook.playSound(rod.getBiteSound(), volume, 1.0F);
                return;
            }
        }

        hook.playSound(sound, volume, pitch);
    }

    private ItemStack getRodStack(Player player) {
        ItemStack mainHandStack = player.getMainHandItem();

        if (mainHandStack.getItem() instanceof CaseRewardFishingRodItem) {
            return mainHandStack;
        }

        return player.getOffhandItem();
    }
}