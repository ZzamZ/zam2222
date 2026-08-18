package net.ron.zam.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.ron.zam.registry.ZAMEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin {
    @WrapOperation(
            method = "retrieve",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/storage/loot/LootTable;getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;"
            )
    )
    private ObjectArrayList<ItemStack> zam$applyMotherCatch(LootTable lootTable, LootParams lootParams, Operation<ObjectArrayList<ItemStack>> original, ItemStack fishingRod) {
        ObjectArrayList<ItemStack> loot = original.call(lootTable, lootParams);

        FishingHook fishingHook = (FishingHook) (Object) this;

        if (!(fishingHook.level() instanceof ServerLevel serverLevel)) {
            return loot;
        }

        Holder<Enchantment> motherCatch = serverLevel
                .registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(ZAMEnchantments.MOTHER_CATCH);

        int level = fishingRod.getEnchantments()
                .getLevel(motherCatch);

        float chance = switch (level) {
            case 1 -> 0.10F;
            case 2 -> 0.25F;
            case 3 -> 0.50F;
            default -> 0.0F;
        };

        if (serverLevel.getRandom().nextFloat() < chance) {
            loot.addAll(original.call(lootTable, lootParams));
        }

        return loot;
    }
}