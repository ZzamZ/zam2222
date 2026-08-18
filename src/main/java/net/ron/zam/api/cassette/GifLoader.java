package net.ron.zam.api.cassette;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.ron.zam.ZAMMod;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

public final class GifLoader {

    private static final Map<Identifier, Loaded> CACHE = new HashMap<>();
    private static final String GIF_IMAGE_META = "javax_imageio_gif_image_1.0";

    private GifLoader() {}

    @Nullable
    public static Loaded getOrLoad(Identifier cassetteId) {
        Loaded cached = CACHE.get(cassetteId);
        if (cached != null) return cached;

        Identifier resourcePath = Identifier.fromNamespaceAndPath(
                cassetteId.getNamespace(), "textures/" + cassetteId.getPath() + ".gif");

        Minecraft mc = Minecraft.getInstance();
        if (mc == null) return null;
        ResourceManager resourceManager = mc.getResourceManager();
        Optional<Resource> resourceOpt = resourceManager.getResource(resourcePath);
        if (resourceOpt.isEmpty()) {
            CACHE.put(cassetteId, EMPTY);
            return EMPTY;
        }

        try (InputStream stream = resourceOpt.get().open()) {
            Loaded loaded = decode(stream, cassetteId);
            CACHE.put(cassetteId, loaded);
            return loaded;
        } catch (IOException e) {
            ZAMMod.LOGGER.error("Failed to read gif {}", resourcePath, e);
            CACHE.put(cassetteId, EMPTY);
            return EMPTY;
        }
    }

    private static Loaded decode(InputStream stream, Identifier cassetteId) throws IOException {
        try (ImageInputStream iis = ImageIO.createImageInputStream(stream)) {
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("gif");
            if (!readers.hasNext()) {
                ZAMMod.LOGGER.warn("No JDK ImageReader available for gif format");
                return EMPTY;
            }
            ImageReader reader = readers.next();
            reader.setInput(iis, false);

            int frameCount = reader.getNumImages(true);
            if (frameCount <= 0) return EMPTY;

            int canvasW = reader.getWidth(0);
            int canvasH = reader.getHeight(0);
            BufferedImage canvas = new BufferedImage(canvasW, canvasH, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = canvas.createGraphics();
            g.setComposite(AlphaComposite.Src);

            BufferedImage snapshot = new BufferedImage(canvasW, canvasH, BufferedImage.TYPE_INT_ARGB);

            List<Identifier> frameIds = new ArrayList<>(frameCount);
            List<Integer> frameDelaysCs = new ArrayList<>(frameCount);

            TextureManager textureManager = Minecraft.getInstance().getTextureManager();
            String safeName = cassetteId.getPath().replace('/', '_');

            String prevDisposal = "none";
            int prevX = 0, prevY = 0, prevW = canvasW, prevH = canvasH;

            for (int i = 0; i < frameCount; i++) {
                IIOMetadataNode meta = (IIOMetadataNode) reader.getImageMetadata(i).getAsTree(GIF_IMAGE_META);

                if (i > 0) {
                    switch (prevDisposal) {
                        case "restoreToBackgroundColor" -> {
                            g.setComposite(AlphaComposite.Clear);
                            g.fillRect(prevX, prevY, prevW, prevH);
                            g.setComposite(AlphaComposite.Src);
                        }
                        case "restoreToPrevious" -> {
                            g.setComposite(AlphaComposite.Src);
                            g.drawImage(snapshot, 0, 0, null);
                        }
                        default -> { }
                    }
                }

                String thisDisposal = readDisposalMethod(meta);
                if ("restoreToPrevious".equals(thisDisposal)) {
                    Graphics2D sg = snapshot.createGraphics();
                    sg.setComposite(AlphaComposite.Src);
                    sg.drawImage(canvas, 0, 0, null);
                    sg.dispose();
                }

                BufferedImage frame = reader.read(i);
                int[] off = readImageOffset(meta);
                g.setComposite(AlphaComposite.SrcOver);
                g.drawImage(frame, off[0], off[1], null);
                g.setComposite(AlphaComposite.Src);

                NativeImage native_ = bufferedToNative(canvas);
                Identifier frameId = Identifier.fromNamespaceAndPath(cassetteId.getNamespace(),
                        "dynamic_gif/" + safeName + "/frame_" + i);
                DynamicTexture tex = new DynamicTexture(() -> frameId.toString(), native_);
                textureManager.register(frameId, tex);
                frameIds.add(frameId);

                int delayCs = readFrameDelayCs(meta);
                frameDelaysCs.add(delayCs <= 0 ? 10 : delayCs);

                prevDisposal = thisDisposal;
                prevX = off[0];
                prevY = off[1];
                prevW = frame.getWidth();
                prevH = frame.getHeight();
            }
            g.dispose();
            reader.dispose();

            return new Loaded(frameIds, frameDelaysCs);
        }
    }

    private static String readDisposalMethod(IIOMetadataNode root) {
        if (root == null) return "none";
        for (int i = 0; i < root.getLength(); i++) {
            IIOMetadataNode node = (IIOMetadataNode) root.item(i);
            if ("GraphicControlExtension".equals(node.getNodeName())) {
                String dm = node.getAttribute("disposalMethod");
                return dm == null || dm.isEmpty() ? "none" : dm;
            }
        }
        return "none";
    }

    private static int[] readImageOffset(IIOMetadataNode root) {
        if (root == null) return new int[]{0, 0};
        for (int i = 0; i < root.getLength(); i++) {
            IIOMetadataNode node = (IIOMetadataNode) root.item(i);
            if ("ImageDescriptor".equals(node.getNodeName())) {
                try {
                    int x = Integer.parseInt(node.getAttribute("imageLeftPosition"));
                    int y = Integer.parseInt(node.getAttribute("imageTopPosition"));
                    return new int[]{x, y};
                } catch (NumberFormatException ignored) {
                    return new int[]{0, 0};
                }
            }
        }
        return new int[]{0, 0};
    }

    private static int readFrameDelayCs(IIOMetadataNode root) {
        if (root == null) return 0;
        try {
            for (int i = 0; i < root.getLength(); i++) {
                IIOMetadataNode node = (IIOMetadataNode) root.item(i);
                if ("GraphicControlExtension".equals(node.getNodeName())) {
                    String delay = node.getAttribute("delayTime");
                    return Integer.parseInt(delay);
                }
            }
        } catch (Exception ignored) {}
        return 0;
    }

    private static NativeImage bufferedToNative(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        NativeImage out = new NativeImage(NativeImage.Format.RGBA, w, h, false);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int argb = img.getRGB(x, y);
                int a = (argb >> 24) & 0xFF;
                int r = (argb >> 16) & 0xFF;
                int gC = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;
                int abgr = (a << 24) | (b << 16) | (gC << 8) | r;
                out.setPixelABGR(x, y, abgr);
            }
        }
        return out;
    }

    public static Identifier frameAt(Loaded loaded, int playbackTicks) {
        if (loaded == null || loaded.frames().isEmpty()) {
            return MISSING;
        }
        int totalCs = 0;
        for (int d : loaded.delaysCs()) totalCs += d;
        if (totalCs <= 0) totalCs = loaded.frames().size();
        int cs = (playbackTicks * 5) % totalCs;
        int acc = 0;
        for (int i = 0; i < loaded.frames().size(); i++) {
            acc += loaded.delaysCs().get(i);
            if (cs < acc) return loaded.frames().get(i);
        }
        return loaded.frames().get(loaded.frames().size() - 1);
    }

    private static final Identifier MISSING = Identifier.fromNamespaceAndPath("minecraft", "textures/misc/unknown_pack.png");
    private static final Loaded EMPTY = new Loaded(List.of(), List.of());

    public record Loaded(List<Identifier> frames, List<Integer> delaysCs) {
        public boolean isEmpty() { return frames.isEmpty(); }
    }
}
