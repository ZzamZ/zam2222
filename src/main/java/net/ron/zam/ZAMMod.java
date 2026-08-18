package net.ron.zam;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.Identifier;
import net.ron.zam.api.cassette.CassetteReloadListener;
import net.ron.zam.api.casesystem.cases.CaseRewards;
import net.ron.zam.common.packet.ClaimRewardPacket;
import net.ron.zam.common.packet.ConfigureVideoTapePacket;
import net.ron.zam.common.packet.ConsumeLootBoxItemsPacket;
import net.ron.zam.registry.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZAMMod implements ModInitializer {
    public static final String MOD_ID = "zam";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final CassetteReloadListener CASSETTES = new CassetteReloadListener();

    @Override
    public void onInitialize() {
        ZAMComponents.registerDataComponents();
        ZAMItems.registerItems();
        ZAMBlocks.registerBlocks();
        ZAMBlockEntities.registerBlockEntities();
        ZAMCriteriaTriggers.registerCriteriaTriggers();
        ZAMEnchantments.registerEnchantments();
        ZAMItemGroups.registerItemGroups();
        ZAMLoot.registerLoot();
        ZAMLootTables.registerLootTables();
        ZAMMenuTypes.registerMenuTypes();
        ZAMStats.registerStats();
        ZAMSounds.registerSounds();

        CaseRewards.init();

        registerPayloadHandlers();
    }

    private static void registerPayloadHandlers() {
        PayloadTypeRegistry.serverboundPlay().register(ClaimRewardPacket.TYPE, ClaimRewardPacket.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ClaimRewardPacket.TYPE, ClaimRewardPacket::handle);

        PayloadTypeRegistry.serverboundPlay().register(ConsumeLootBoxItemsPacket.TYPE, ConsumeLootBoxItemsPacket.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ConsumeLootBoxItemsPacket.TYPE, ConsumeLootBoxItemsPacket::handle);

        PayloadTypeRegistry.serverboundPlay().register(ConfigureVideoTapePacket.TYPE, ConfigureVideoTapePacket.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ConfigureVideoTapePacket.TYPE, ConfigureVideoTapePacket::handle);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
