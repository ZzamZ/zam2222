package net.ron.zam.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.ron.zam.common.item.MusicBoxItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        method = "tick",
        at = @At("HEAD")
    )
    public void zam$onTick(CallbackInfo ci) {
        if (this.level().isClientSide()) {
            MusicBoxItem.onLivingEntityUpdateClient((LivingEntity) (Object) this);
        }
    }
}
