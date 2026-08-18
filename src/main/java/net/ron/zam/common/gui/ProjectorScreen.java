package net.ron.zam.common.gui;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.ron.zam.common.packet.ConfigureVideoTapePacket;

public class ProjectorScreen extends Screen {
    private static final Component TITLE = Component.translatable("menu.zam.projector");

    private final InteractionHand hand;
    private final String initialUrl;
    private final float initialVolume;
    private EditBox urlBox;
    private VolumeSlider volumeSlider;

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
        int left = width / 2 - 180;
        int top = height / 4 + 10;

        urlBox = new EditBox(font, left, top, 360, 20, title);
        urlBox.setMaxLength(2048);
        urlBox.setHint(Component.literal("Paste media URL or file path"));
        urlBox.setValue(initialUrl);
        addRenderableWidget(urlBox);

        volumeSlider = addRenderableWidget(
                new VolumeSlider(left, top + 30, 360, initialVolume)
        );

        addRenderableWidget(Button.builder(
                Component.literal("Clear Tape"),
                b -> clearTape()
        ).bounds(left, top + 80, 176, 20).build());

        addRenderableWidget(Button.builder(
                CommonComponents.GUI_DONE,
                b -> done()
        ).bounds(left + 184, top + 80, 176, 20).build());

        setInitialFocus(urlBox);
        urlBox.setFocused(true);
    }

    private void done() {
        ClientPlayNetworking.send(new ConfigureVideoTapePacket(
                hand,
                urlBox.getValue().trim(),
                volumeSlider.getVolume()
        ));

        minecraft.gui.setScreen(null);
    }

    private void clearTape() {
        ClientPlayNetworking.send(new ConfigureVideoTapePacket(
                hand,
                "",
                1.0F
        ));

        minecraft.gui.setScreen(null);
    }

    @Override
    public void onClose() {
        done();
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
        public VolumeSlider(int x, int y, int width, float volume) {
            super(x, y, width, 20, Component.empty(), Math.clamp(volume, 0.0F, 1.0F));
            updateMessage();
        }

        public float getVolume() {
            return (float) value;
        }

        @Override
        protected void updateMessage() {
            setMessage(Component.literal(
                    "Volume: " + Math.round(value * 100.0D) + "%"
            ));
        }

        @Override
        protected void applyValue() {}
    }
}