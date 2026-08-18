package net.ron.zam.util.tooltips;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.ron.zam.api.musicbox.PlayableRecord;
import net.ron.zam.api.musicbox.SoundTracker;
import net.ron.zam.common.item.RecordSleeveItem;

import java.util.List;
import java.util.stream.IntStream;

public class ClientMusicBoxTooltip implements ClientTooltipComponent {

    private static final int ROW_HEIGHT = 12;
    private static final int TEXT_OFFSET_X = 20;
    private static final int RIGHT_PADDING = 4;

    private final ItemStack record;
    private final boolean paused;
    private final boolean looping;

    public ClientMusicBoxTooltip(MusicBoxTooltip data) {
        this.record = data.disc();
        this.paused = data.paused();
        this.looping = data.looping();
    }

    @Override
    public int getWidth(Font font) {
        int width = 0;

        for (ItemStack track : getTracks()) {
            Component name = getDiscDescriptionOrName(track);

            width = Math.max(width, TEXT_OFFSET_X + font.width(name) + RIGHT_PADDING);
        }

        if (paused) {
            width = Math.max(width, TEXT_OFFSET_X + font.width("Paused") + RIGHT_PADDING);
        }

        if (looping) {
            String loopingText = isSleeve() ? "Looping Playlist" : "Looping";

            width = Math.max(width, TEXT_OFFSET_X + font.width(loopingText) + RIGHT_PADDING);
        }

        return width;
    }

    @Override
    public int getHeight(Font font) {
        int height =
                Math.max(1, getTracks().size())
                        * ROW_HEIGHT
                        + 1;

        if (paused) {
            height += 9;
        }

        if (looping) {
            height += 9;
        }

        return height;
    }

    @Override
    public boolean showTooltipWithItemInHand() {
        return true;
    }

    @Override
    public void extractImage(
            Font font,
            int x,
            int y,
            int width,
            int height,
            GuiGraphicsExtractor graphics
    ) {
        List<ItemStack> tracks = getTracks();
        int currentTrack = getCurrentTrack();

        for (int i = 0; i < tracks.size(); i++) {
            ItemStack track = tracks.get(i);
            int rowY = y + i * ROW_HEIGHT;

            graphics.item(
                    track,
                    x,
                    rowY
            );

            Component name =
                    getDiscDescriptionOrName(track);

            if (isSleeve()
                    && i == currentTrack
                    && !paused) {

                drawFlowingTrackName(
                        graphics,
                        font,
                        name.getString(),
                        x + TEXT_OFFSET_X,
                        rowY + 2
                );
            } else {
                graphics.text(
                        font,
                        name,
                        x + TEXT_OFFSET_X,
                        rowY + 2,
                        0xFFFFFFFF,
                        false
                );
            }
        }

        int statusY =
                y + Math.max(1, tracks.size())
                        * ROW_HEIGHT
                        + 1;

        if (paused) {
            graphics.text(
                    font,
                    Component.literal("Paused")
                            .withStyle(
                                    ChatFormatting.GRAY
                            ),
                    x + TEXT_OFFSET_X,
                    statusY,
                    0xFFAAAAAA,
                    false
            );

            statusY += 9;
        }

        if (looping) {
            Component loopingText = isSleeve()
                    ? Component.literal(
                    "Looping Playlist"
            )
                    : Component.literal("Looping");

            graphics.text(
                    font,
                    loopingText.copy().withStyle(ChatFormatting.AQUA),
                    x + TEXT_OFFSET_X,
                    statusY - 1,
                    0xFF55FFFF,
                    false
            );
        }
    }

    /**
     * Draws an animated green-to-cyan wave across the active
     * track's full name.
     */
    private static void drawFlowingTrackName(
            GuiGraphicsExtractor graphics,
            Font font,
            String text,
            int x,
            int y
    ) {
        long time = Util.getMillis();
        int characterX = x;

        for (int i = 0; i < text.length(); i++) {
            String character =
                    String.valueOf(text.charAt(i));

            double phase =
                    time / 250.0 - i * 0.65;

            double wave =
                    (Math.sin(phase) + 1.0) / 2.0;

            int red = 40;
            int green =
                    210 + (int) (45 * wave);
            int blue =
                    80 + (int) (175 * wave);

            int color =
                    0xFF000000
                            | red << 16
                            | green << 8
                            | blue;

            graphics.text(
                    font,
                    Component.literal(character),
                    characterX,
                    y,
                    color,
                    false
            );

            characterX += font.width(character);
        }
    }

    private boolean isSleeve() {
        return record.getItem()
                instanceof RecordSleeveItem;
    }

    /**
     * Returns every disc in the Record Sleeve or the single
     * normal record.
     */
    private List<ItemStack> getTracks() {
        int trackCount =
                PlayableRecord.getTrackCount(record);

        if (trackCount <= 0) {
            return List.of(record);
        }

        return IntStream.range(0, trackCount)
                .mapToObj(index ->
                        PlayableRecord.getTrack(
                                record,
                                index
                        ))
                .filter(track -> !track.isEmpty())
                .toList();
    }

    private int getCurrentTrack() {
        Minecraft minecraft =
                Minecraft.getInstance();

        if (minecraft.player == null) {
            return -1;
        }

        return SoundTracker.getCurrentTrack(
                minecraft.player.getId(),
                record
        );
    }

    private static Component getDiscDescriptionOrName(
            ItemStack stack
    ) {
        if (stack.has(DataComponents.JUKEBOX_PLAYABLE)) {
            Minecraft minecraft =
                    Minecraft.getInstance();

            Item.TooltipContext context =
                    minecraft.level != null
                            ? Item.TooltipContext.of(
                            minecraft.level
                    )
                            : Item.TooltipContext.EMPTY;

            TooltipFlag flag =
                    minecraft.options
                            .advancedItemTooltips
                            ? TooltipFlag.ADVANCED
                            : TooltipFlag.NORMAL;

            List<Component> lines =
                    stack.getTooltipLines(
                            context,
                            minecraft.player,
                            flag
                    );

            Component discLine = null;

            if (lines.size() >= 3) {
                discLine = lines.get(2);
            } else if (lines.size() >= 2) {
                discLine = lines.get(1);
            }

            if (discLine != null
                    && !discLine.getString().isEmpty()) {

                return discLine.copy().withStyle(
                        stack.getRarity().color()
                );
            }
        }

        return stack.getHoverName()
                .copy()
                .withStyle(stack.getRarity().color());
    }
}