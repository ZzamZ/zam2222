package net.ron.zam.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.ron.zam.ZAMMod;

import java.util.function.Consumer;

public enum PausedComponent implements TooltipProvider {
    INSTANCE;

    public static final MapCodec<PausedComponent> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final Codec<PausedComponent> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<RegistryFriendlyByteBuf, PausedComponent> STREAM_CODEC = StreamCodec.of((buf, value) -> {}, buf -> INSTANCE);
    private static final Component TOOLTIP = Component.translatable("item." + ZAMMod.MOD_ID + ".music_box.paused").withStyle(ChatFormatting.YELLOW);

    @Override
    public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
        consumer.accept(TOOLTIP);
    }
}
