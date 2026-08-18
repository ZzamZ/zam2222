package net.ron.zam.common.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.ron.zam.ZAMMod;
import net.ron.zam.util.BottleMessages;

import java.util.List;

public class SecretMessageScreen extends Screen {

    private static final Identifier TEXTURE = ZAMMod.id("textures/gui/secret_message.png");

    private static final int TEXTURE_SIZE = 256;
    private static final int IMAGE_WIDTH = 175;
    private static final int IMAGE_HEIGHT = 191;
    private static final int TEXT_WIDTH = 140;

    private static final int TITLE_COLOR = 0xFF4A402F;
    private static final int MESSAGE_COLOR = 0xFF5B503B;

    private final int message;

    public SecretMessageScreen(int message) {
        super(Component.empty());
        this.message = message;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor gui, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(gui, mouseX, mouseY, partialTick);

        int left = (this.width - IMAGE_WIDTH) / 2;
        int top = (this.height - IMAGE_HEIGHT) / 2;
        int centerX = left + IMAGE_WIDTH / 2;

        Component title = Component.translatable("menu.zam.secret_message", this.message);
        gui.text(this.font, title, centerX - this.font.width(title) / 2, top + 20, TITLE_COLOR, false);

        List<FormattedCharSequence> lines = this.font.split(BottleMessages.get(this.message), TEXT_WIDTH);
        int y = top + 44;
        for (FormattedCharSequence line : lines) {
            if (y + this.font.lineHeight > top + IMAGE_HEIGHT - 18) {
                break;
            }

            gui.text(this.font, line, centerX - this.font.width(line) / 2, y, MESSAGE_COLOR, false);

            y += this.font.lineHeight + 3;
        }
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (this.minecraft.options.keyInventory.matches(event)) {
            this.onClose();
            return true;
        }

        return super.keyPressed(event);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor gui, int mouseX, int mouseY, float partialTick) {
        this.extractTransparentBackground(gui);

        int left = (this.width - IMAGE_WIDTH) / 2;
        int top = (this.height - IMAGE_HEIGHT) / 2;
        gui.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, left, top, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, TEXTURE_SIZE, TEXTURE_SIZE);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}