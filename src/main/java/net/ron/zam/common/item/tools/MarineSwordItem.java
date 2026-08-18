package net.ron.zam.common.item.tools;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.ron.zam.common.item.AbilityItem;
import org.jetbrains.annotations.Nullable;

public class MarineSwordItem extends AbilityItem {

    public MarineSwordItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, level, entity, slot);

        if (!(entity instanceof Player player)) {
            return;
        }

        if (slot != EquipmentSlot.MAINHAND) {
            return;
        }

        player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 1, 0, true, false, true));
    }
}