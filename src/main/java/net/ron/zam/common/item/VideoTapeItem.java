package net.ron.zam.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.ron.zam.common.component.VideoMediaComponent;
import net.ron.zam.registry.ZAMComponents;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.nio.file.Path;
import java.util.function.Consumer;

public class VideoTapeItem extends Item {
    public VideoTapeItem(Properties properties) {
        super(properties);
    }

    @Nullable
    public static VideoMediaComponent getMedia(ItemStack stack) {
        return stack.get(ZAMComponents.VIDEO_MEDIA);
    }

    public static void configure(ItemStack stack, String url, float volume) {
        configure(stack, url, volume, "", "");
    }

    public static void configure(ItemStack stack, String url, float volume, String title, String creator) {
        stack.set(ZAMComponents.VIDEO_MEDIA,
                new VideoMediaComponent(url, volume, title, creator));
    }

    public static void clear(ItemStack stack) {
        stack.remove(ZAMComponents.VIDEO_MEDIA);
    }

    public static boolean hasMedia(ItemStack stack) {
        VideoMediaComponent media = getMedia(stack);
        return media != null && !media.url().isBlank();
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return hasMedia(stack) || super.isFoil(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
                                Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);

        VideoMediaComponent media = getMedia(stack);
        if (media == null || media.url().isBlank()) return;

        String title = media.title().isBlank() ? fileName(media.url()) : media.title();

        if (!title.isBlank())
            tooltip.accept(Component.literal(title).withStyle(ChatFormatting.GRAY));

        if (!media.creator().isBlank())
            tooltip.accept(Component.literal(media.creator()).withStyle(ChatFormatting.DARK_GRAY));

        tooltip.accept(Component.literal("Volume: " + Math.round(media.volume() * 100F) + "%")
                .withStyle(ChatFormatting.DARK_GRAY));
    }

    private static String fileName(String source) {
        String value = source.trim();

        try {
            if (value.startsWith("http://") || value.startsWith("https://")) {
                String path = URI.create(value).getPath();
                if (path != null && !path.isBlank())
                    value = Path.of(path).getFileName().toString();
            } else {
                value = value.replace('\\', '/');
                int slash = value.lastIndexOf('/');
                if (slash >= 0) value = value.substring(slash + 1);
            }

            int dot = value.lastIndexOf('.');
            return dot > 0 ? value.substring(0, dot) : value;
        } catch (Exception ignored) {
            return value;
        }
    }
}