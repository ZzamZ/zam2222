package net.ron.zam.common.packet;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.ron.zam.ZAMMod;
import net.ron.zam.common.item.VideoTapeItem;
import net.ron.zam.registry.ZAMItems;

public record ConfigureVideoTapePacket(
        InteractionHand hand, String url, float volume, String title, String creator
) implements CustomPacketPayload {
    public static final Type<ConfigureVideoTapePacket> TYPE =
            new Type<>(ZAMMod.id("configure_video_tape"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ConfigureVideoTapePacket> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public ConfigureVideoTapePacket decode(RegistryFriendlyByteBuf buf) {
                    return new ConfigureVideoTapePacket(
                            buf.readBoolean() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND,
                            buf.readUtf(2048), buf.readFloat(), buf.readUtf(512), buf.readUtf(256)
                    );
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, ConfigureVideoTapePacket packet) {
                    buf.writeBoolean(packet.hand() == InteractionHand.OFF_HAND);
                    buf.writeUtf(packet.url(), 2048);
                    buf.writeFloat(packet.volume());
                    buf.writeUtf(packet.title(), 512);
                    buf.writeUtf(packet.creator(), 256);
                }
            };

    public static void handle(ConfigureVideoTapePacket packet, ServerPlayNetworking.Context context) {
        ItemStack tape = context.player().getItemInHand(packet.hand());
        if (!tape.is(ZAMItems.VIDEO_TAPE)) return;

        String url = clean(packet.url());

        if (url.isBlank()) {
            VideoTapeItem.clear(tape);
            return;
        }

        VideoTapeItem.configure(tape, url, packet.volume(), packet.title(), packet.creator());
    }

    private static String clean(String value) {
        value = value.trim();

        if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\""))
            value = value.substring(1, value.length() - 1);

        return value.trim();
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}