package net.ron.zam.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.level.Level;
import net.ron.zam.api.musicbox.SoundTracker;
import net.ron.zam.common.item.MusicBoxItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Parrot.class)
public abstract class ParrotMixin extends Entity {
    @Shadow @Nullable private BlockPos jukebox;
    @Shadow private boolean partyParrot;

    public ParrotMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "aiStep", at = @At("RETURN"))
    private void zam$addAudioProviders(CallbackInfo ci) {
        if (!this.level().isClientSide()) {
            return;
        }

        List<Entity> entities = this.level().getEntities(this, this.getBoundingBox().inflate(3.45), entity -> {
            if (!entity.isAlive() || entity.isSpectator()) {
                return false;
            }

            if (entity == Minecraft.getInstance().player) {
                LivingEntity livingEntity = (LivingEntity) entity;

                if (MusicBoxItem.getPlayingHand(livingEntity) == null) {
                    return false;
                }
            }

            return SoundTracker.getEntitySound(entity.getId()) != null;
        });

        if (!entities.isEmpty()) {
            this.partyParrot = true;
            this.jukebox = this.blockPosition();
        }
    }
}