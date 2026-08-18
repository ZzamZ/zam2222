package net.ron.zam.common.packet;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.ron.zam.ZAMMod;
import net.ron.zam.api.casesystem.BaseLootBoxMenu;
import net.ron.zam.registry.ZAMStats;

public record ConsumeLootBoxItemsPacket() implements CustomPacketPayload {
    public static final Type<ConsumeLootBoxItemsPacket> TYPE = new Type<>(ZAMMod.id("consume_lootbox_items"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ConsumeLootBoxItemsPacket> STREAM_CODEC =
            StreamCodec.unit(new ConsumeLootBoxItemsPacket());

    @Override
    public Type<ConsumeLootBoxItemsPacket> type() {
        return TYPE;
    }

    public static void handle(ConsumeLootBoxItemsPacket packet, ServerPlayNetworking.Context ctx) {
        ServerPlayer player = ctx.player();

        if (!(player.containerMenu instanceof BaseLootBoxMenu<?> menu)) {
            return;
        }

        if (!menu.consumeRequiredItems(player)) {
            return;
        }

        player.awardStat(ZAMStats.CASES_OPENED, 1);
        player.inventoryMenu.broadcastChanges();
    }
}