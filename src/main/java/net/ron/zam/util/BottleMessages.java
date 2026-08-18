package net.ron.zam.util;

import net.minecraft.network.chat.Component;

import java.util.List;

public class BottleMessages {

    private static final List<Component> MESSAGES = List.of(
            Component.translatable("message.zam.1"),
            Component.translatable("message.zam.2"),
            Component.translatable("message.zam.3"),
            Component.translatable("message.zam.4"),
            Component.translatable("message.zam.5")
    );

    public static Component get(int message) {
        if (message < 1 || message > MESSAGES.size()) {
            return Component.translatable("message.zam.unknown");
        }

        return MESSAGES.get(message - 1);
    }

    public static int count() {
        return MESSAGES.size();
    }
}