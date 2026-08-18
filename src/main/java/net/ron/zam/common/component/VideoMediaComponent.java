package net.ron.zam.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record VideoMediaComponent(String url, float volume) {
    public static final Codec<VideoMediaComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("url").forGetter(VideoMediaComponent::url),
            Codec.FLOAT.optionalFieldOf("volume", 1.0F).forGetter(VideoMediaComponent::volume)
    ).apply(instance, VideoMediaComponent::new));

    public static final StreamCodec<ByteBuf, VideoMediaComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, VideoMediaComponent::url,
            ByteBufCodecs.FLOAT, VideoMediaComponent::volume,
            VideoMediaComponent::new
    );

    public VideoMediaComponent {
        url = url.trim();
        volume = Math.clamp(volume, 0.0F, 1.0F);
    }
}