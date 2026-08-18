package net.ron.zam.common.packet;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.world.item.JukeboxSong;
import net.ron.zam.ZAMMod;
import net.ron.zam.api.casesystem.BaseLootBoxMenu;
import net.ron.zam.api.rarity.Rarity;
import net.ron.zam.common.data.ZAMSavedData;
import net.ron.zam.registry.ZAMCriteriaTriggers;

public record ClaimRewardPacket(ItemStack reward, Rarity rarity, Identifier caseId) implements CustomPacketPayload {
    public static final Type<ClaimRewardPacket> TYPE = new Type<>(ZAMMod.id("claim_reward"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClaimRewardPacket> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, ClaimRewardPacket::reward,
            ByteBufCodecs.STRING_UTF8.map(Rarity::valueOf, Rarity::name), ClaimRewardPacket::rarity,
            Identifier.STREAM_CODEC, ClaimRewardPacket::caseId,
            ClaimRewardPacket::new
    );

    public ClaimRewardPacket {
        reward = reward.copy();
    }

    @Override
    public Type<ClaimRewardPacket> type() {
        return TYPE;
    }

    public static void handle(ClaimRewardPacket packet, ServerPlayNetworking.Context ctx) {
        ServerPlayer player = ctx.player();

        if (!(player.containerMenu instanceof BaseLootBoxMenu<?> menu)) {
            return;
        }

        if (!menu.isValidReward(packet.reward, packet.rarity, packet.caseId)) {
            return;
        }

        if (!menu.consumeRewardAuthorization()) {
            return;
        }

        ItemStack reward = packet.reward.copy();

        if (reward.isEmpty()) {
            return;
        }

        Component caseTitle = menu.getCaseEntry().title();

        player.closeContainer();
        giveReward(player, reward);

        ZAMSavedData.setCollected(
                player.level().getServer(),
                player,
                reward.getItem()
        );

        Component announcement = createAnnouncement(
                player,
                reward,
                packet.rarity,
                caseTitle,
                player.registryAccess()
        );

        player.level().getServer()
                .getPlayerList()
                .broadcastSystemMessage(announcement, false);

        ZAMCriteriaTriggers.CASE_REWARD.trigger(
                player,
                reward,
                packet.caseId
        );

        ZAMCriteriaTriggers.CASE_RARITY_REWARD.trigger(
                player,
                reward,
                packet.caseId,
                packet.rarity
        );
    }

    private static void giveReward(ServerPlayer player, ItemStack reward) {
        ItemStack remaining = reward.copy();
        boolean added = player.getInventory().add(remaining);

        if (added && remaining.isEmpty()) {
            player.inventoryMenu.broadcastChanges();
            return;
        }

        ItemEntity dropped = player.drop(remaining, false);

        if (dropped != null) {
            dropped.setNoPickUpDelay();
            dropped.setTarget(player.getUUID());
        }
    }

    private static Component createAnnouncement(ServerPlayer player, ItemStack reward, Rarity rarity, Component caseTitle, RegistryAccess registryAccess) {
        Component rewardName = getRewardName(reward);
        Component playerName = player.getDisplayName().copy().withStyle(style -> style.withColor(0x55FF55));
        Component itemName = rewardName.copy().withStyle(style -> style.withColor(rarity.getColor()));
        Component caseName = caseTitle.copy().withStyle(style -> style.withColor(0x90EE90));

        return Component.empty()
                .append(playerName)
                .append(Component.literal(" opened a ").withStyle(style -> style.withColor(0xADD8E6)))
                .append(Component.literal("[").withStyle(style -> style.withColor(0xADD8E6)))
                .append(caseName)
                .append(Component.literal("]").withStyle(style -> style.withColor(0xADD8E6)))
                .append(Component.literal(" and received ").withStyle(style -> style.withColor(0xADD8E6)))
                .append(Component.literal("[").withStyle(style -> style.withColor(rarity.getColor())))
                .append(itemName)
                .append(Component.literal("]").withStyle(style -> style.withColor(rarity.getColor())));
    }

    private static Component getRewardName(ItemStack stack) {
        JukeboxPlayable playable = stack.get(DataComponents.JUKEBOX_PLAYABLE);

        if (playable != null) {
            Holder<JukeboxSong> song = playable.song();
            return song.value().description();
        }

        return stack.getHoverName();
    }
}