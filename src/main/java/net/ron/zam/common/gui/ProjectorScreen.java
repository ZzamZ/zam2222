package net.ron.zam.common.gui;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.ron.zam.ZAMMod;
import net.ron.zam.api.projector.MediaResolver;
import net.ron.zam.common.packet.ConfigureVideoTapePacket;

public class ProjectorScreen extends Screen {
    private static final Component TITLE = Component.translatable("menu.zam.projector");

    private final InteractionHand hand;
    private final String initialUrl;
    private final float initialVolume;
    private EditBox urlBox;
    private VolumeSlider volumeSlider;
    private Button doneButton;
    private boolean submitting, submitted;

    public ProjectorScreen(InteractionHand hand, String url, float volume) {
        super(TITLE);
        this.hand = hand;
        this.initialUrl = url;
        this.initialVolume = volume;
    }

    public static void open(InteractionHand hand, String url, float volume) {
        Minecraft.getInstance().gui.setScreen(new ProjectorScreen(hand, url, volume));
    }

    @Override
    protected void init() {
        int left = width / 2 - 180, top = height / 4 + 10;

        urlBox = new EditBox(font, left, top, 360, 20, title);
        urlBox.setMaxLength(2048);
        urlBox.setHint(Component.literal("Paste media URL or file path"));
        urlBox.setValue(initialUrl);
        addRenderableWidget(urlBox);

        volumeSlider = addRenderableWidget(new VolumeSlider(left, top + 30, 360, initialVolume));

        addRenderableWidget(Button.builder(Component.literal("Clear Tape"), b -> clearTape())
                .bounds(left, top + 80, 176, 20).build());

        doneButton = addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> done())
                .bounds(left + 184, top + 80, 176, 20).build());

        setInitialFocus(urlBox);
        urlBox.setFocused(true);
    }

    private void done() {
        if (submitting) return;

        String url = urlBox.getValue().trim();
        float volume = volumeSlider.getVolume();

        if (url.isBlank()) {
            send("", volume, "", "");
            return;
        }

        submitting = true;
        doneButton.active = false;
        doneButton.setMessage(Component.literal("Resolving..."));

        Thread thread = new Thread(() -> {
            String title = "", creator = "";

            try {
                var media = MediaResolver.resolve(url);
                title = media.title();
                creator = media.creator();
            } catch (Exception e) {
                ZAMMod.LOGGER.warn("Could not resolve media metadata for {}", url);
            }

            String finalTitle = title, finalCreator = creator;
            minecraft.execute(() -> send(url, volume, finalTitle, finalCreator));
        }, "zam-media-metadata");

        thread.setDaemon(true);
        thread.start();
    }

    private void clearTape() {
        if (!submitting) send("", 1F, "", "");
    }

    private void send(String url, float volume, String title, String creator) {
        submitted = true;

        ClientPlayNetworking.send(
                new ConfigureVideoTapePacket(hand, url, volume, title, creator)
        );

        minecraft.gui.setScreen(null);
    }

    @Override
    public void onClose() {
        if (!submitted && !submitting) done();
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (super.keyPressed(event)) return true;

        if (event.key() == InputConstants.KEY_RETURN
                || event.key() == InputConstants.KEY_NUMPADENTER) {
            done();
            return true;
        }

        return false;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor gui, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(gui, mouseX, mouseY, partialTick);
        gui.centeredText(font, title, width / 2, 40, 0xFFFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static class VolumeSlider extends AbstractSliderButton {
        VolumeSlider(int x, int y, int width, float volume) {
            super(x, y, width, 20, Component.empty(), Math.clamp(volume, 0F, 1F));
            updateMessage();
        }

        float getVolume() { return (float) value; }

        @Override
        protected void updateMessage() {
            setMessage(Component.literal("Volume: " + Math.round(value * 100D) + "%"));
        }

        @Override protected void applyValue() {}
    }
}