package net.ron.zam.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import net.ron.zam.ZAMMod;
import net.ron.zam.registry.ZAMWolfVariants;

import java.util.concurrent.CompletableFuture;

public final class ZAMWolfVariantProvider extends FabricDynamicRegistryProvider {

    public ZAMWolfVariantProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    public static void bootstrap(BootstrapContext<WolfVariant> context) {
        register(context, ZAMWolfVariants.HONEY, new WolfVariant(assets("honey"), assets("shaya"), SpawnPrioritySelectors.fallback(0)));
    }

    private static WolfVariant.AssetInfo assets(String name) {
        return new WolfVariant.AssetInfo(
                new ClientAsset.ResourceTexture(ZAMMod.id("entity/wolf/" + name)),
                new ClientAsset.ResourceTexture(ZAMMod.id("entity/wolf/" + name + "_tame")),
                new ClientAsset.ResourceTexture(ZAMMod.id("entity/wolf/" + name + "_angry"))
        );
    }

    private static void register(BootstrapContext<WolfVariant> context, ResourceKey<WolfVariant> key, WolfVariant variant) {
        context.register(key, variant);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.addAll(registries.lookupOrThrow(Registries.WOLF_VARIANT));
    }

    @Override
    public String getName() {
        return "ZAM Wolf Variants";
    }
}