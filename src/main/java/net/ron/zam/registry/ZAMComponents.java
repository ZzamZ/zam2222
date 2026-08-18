package net.ron.zam.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.ron.zam.ZAMMod;
import net.ron.zam.common.component.*;

import java.util.function.UnaryOperator;

public class ZAMComponents {
    public static final DataComponentType<PausedComponent> PAUSED = register(
            "paused",
            builder -> builder
                    .persistent(PausedComponent.CODEC)
                    .networkSynchronized(PausedComponent.STREAM_CODEC)
                    .cacheEncoding()
    );
    public static final DataComponentType<PlayingRecordComponent> PLAYING_RECORD = register(
            "playing_record",
            builder -> builder
                    .persistent(PlayingRecordComponent.CODEC)
                    .networkSynchronized(PlayingRecordComponent.STREAM_CODEC)
                    .cacheEncoding()
    );
    public static final DataComponentType<MusicTrackComponent> MUSIC = register(
            "music",
            builder -> builder
                    .persistent(MusicTrackComponent.CODEC)
                    .networkSynchronized(MusicTrackComponent.STREAM_CODEC)
                    .cacheEncoding()
    );
    public static final DataComponentType<LoopingComponent> LOOPING = register(
            "looping",
            builder -> builder
                    .persistent(LoopingComponent.CODEC)
                    .networkSynchronized(LoopingComponent.STREAM_CODEC)
                    .cacheEncoding()
    );
    public static final DataComponentType<Integer> SECRET_MESSAGE = register(
            "secret_message",
            builder -> builder
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT)
    );
    public static final DataComponentType<Identifier> CASSETTE_ID = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ZAMMod.id("cassette_id"),
            DataComponentType.<Identifier>builder()
                    .persistent(Identifier.CODEC)
                    .networkSynchronized(Identifier.STREAM_CODEC.cast())
                    .build()
    );
    public static final DataComponentType<Identifier> CASE_ID = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ZAMMod.id("case_id"),
            DataComponentType.<Identifier>builder()
                    .persistent(Identifier.CODEC)
                    .networkSynchronized(Identifier.STREAM_CODEC)
                    .build()
    );
    public static final DataComponentType<VideoMediaComponent> VIDEO_MEDIA = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ZAMMod.id("video_media"),
            DataComponentType.<VideoMediaComponent>builder()
                    .persistent(VideoMediaComponent.CODEC)
                    .networkSynchronized(VideoMediaComponent.STREAM_CODEC)
                    .build()
    );

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Identifier.fromNamespaceAndPath(ZAMMod.MOD_ID, name),
                builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void registerDataComponents() {
        ZAMMod.LOGGER.info("Registering Data Components for " + ZAMMod.MOD_ID);
    }
}

