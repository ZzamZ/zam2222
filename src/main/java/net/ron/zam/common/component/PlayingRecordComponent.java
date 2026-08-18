package net.ron.zam.common.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.ron.zam.ZAMMod;

import java.util.function.Consumer;

public record PlayingRecordComponent(ItemStack stack) implements TooltipProvider {
    public static final Codec<PlayingRecordComponent> CODEC = ItemStack.CODEC
        .xmap(PlayingRecordComponent::new, PlayingRecordComponent::stack);
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayingRecordComponent> STREAM_CODEC = ItemStack.STREAM_CODEC
        .map(PlayingRecordComponent::new, PlayingRecordComponent::stack);

    private static final Component RECORDS = Component.translatable("item." + ZAMMod.MOD_ID + ".music_box.records");

    @Override
    public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
        if (dataComponentGetter.get(DataComponents.JUKEBOX_PLAYABLE) != null) {
            consumer.accept(Component.empty());
            consumer.accept(RECORDS);
        }
    }
}