package net.ron.zam.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.ron.zam.registry.ZAMItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class PeacefulPokeMixin {

    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    private void zam$peacefulPoke(ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity target = (LivingEntity) (Object) this;
        Entity attackerEntity = source.getEntity();

        if (!(attackerEntity instanceof LivingEntity attacker))
            return;

        ItemStack weapon = source.getWeaponItem();

        if (weapon == null || !weapon.is(ZAMItems.MARINE_SPEAR))
            return;

        if (target instanceof Enemy || target instanceof Player)
            return;

        double x = attacker.getX() - target.getX();
        double z = attacker.getZ() - target.getZ();

        if (x * x + z * z < 1.0E-4D) {
            x = -attacker.getLookAngle().x;
            z = -attacker.getLookAngle().z;
        }

        target.knockback(1.5D, x, z, source, amount);
        target.hurtMarked = true;
        cir.setReturnValue(false);
    }
}