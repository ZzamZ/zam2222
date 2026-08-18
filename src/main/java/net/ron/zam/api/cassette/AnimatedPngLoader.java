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

import java.io.InputStream;
import java.util.*;

public final class AnimatedPngLoader {

    private static final Map<Identifier, Loaded> CACHE = new HashMap<>();
    private static final Loaded EMPTY = new Loaded(List.of(), null, 1);

    private AnimatedPngLoader() {}

    public record Loaded(List<Identifier> frames, int @Nullable [] frameOrder, int frametime) {
        public boolean isEmpty() { return frames.isEmpty(); }
    }

    @Nullable
    public static Loaded getOrLoad(Identifier texturePath) {
        Loaded cached = CACHE.get(texturePath);
        if (cached != null) return cached;

        Minecraft mc = Minecraft.getInstance();
        if (mc == null) return EMPTY;
        ResourceManager rm = mc.getResourceManager();

        Optional<Resource> textureOpt = rm.getResource(texturePath);
        if (textureOpt.isEmpty()) {
            ZAMMod.LOGGER.warn("[AnimatedPngLoader] texture not found: {}", texturePath);
            CACHE.put(texturePath, EMPTY);
            return EMPTY;
        }

        McmetaAnimationLoader.Animation anim = McmetaAnimationLoader.getOrLoad(texturePath);
        if (!anim.isAnimated()) {
            ZAMMod.LOGGER.info("[AnimatedPngLoader] non-animated, skipping: {}", texturePath);
            CACHE.put(texturePath, EMPTY);
            return EMPTY;
        }

        try (InputStream in = textureOpt.get().open()) {
            NativeImage strip = NativeImage.read(in);
            int w = strip.getWidth();
            int totalH = strip.getHeight();
            int frameCount = anim.frameCount();
            int frameH = totalH / frameCount;
            if (frameH <= 0) {
                strip.close();
                CACHE.put(texturePath, EMPTY);
                return EMPTY;
            }

            String safe = texturePath.getPath().replace('/', '_').replace(".png", "");
            TextureManager textureManager = mc.getTextureManager();
            List<Identifier> frameIds = new ArrayList<>(frameCount);

            for (int i = 0; i < frameCount; i++) {
                NativeImage frame = new NativeImage(NativeImage.Format.RGBA, w, frameH, false);
                int yOffset = i * frameH;
                for (int y = 0; y < frameH; y++) {
                    for (int x = 0; x < w; x++) {
                        frame.setPixel(x, y, strip.getPixel(x, yOffset + y));
                    }
                }
                Identifier frameId = Identifier.fromNamespaceAndPath(texturePath.getNamespace(),
                        "dynamic_png/" + safe + "/frame_" + i);
                DynamicTexture tex = new DynamicTexture(() -> frameId.toString(), frame);
                textureManager.register(frameId, tex);
                frameIds.add(frameId);
            }
            strip.close();

            Loaded loaded = new Loaded(frameIds, anim.frameOrder(), Math.max(1, anim.frametime()));
            ZAMMod.LOGGER.info("[AnimatedPngLoader] sliced {} -> {} frames at {}x{} each",
                    texturePath, frameCount, w, frameH);
            CACHE.put(texturePath, loaded);
            return loaded;
        } catch (Exception e) {
            ZAMMod.LOGGER.error("Failed to slice animated PNG {}", texturePath, e);
            CACHE.put(texturePath, EMPTY);
            return EMPTY;
        }
    }

    public static Identifier frameAt(Loaded loaded, int playbackTicks) {
        if (loaded == null || loaded.isEmpty()) {
            return Identifier.withDefaultNamespace("textures/misc/unknown_pack.png");
        }
        int totalSlots = loaded.frameOrder != null ? loaded.frameOrder.length : loaded.frames.size();
        int slot = Math.floorDiv(playbackTicks, Math.max(1, loaded.frametime)) % totalSlots;
        int frameIdx = loaded.frameOrder != null ? loaded.frameOrder[slot] : slot;
        if (frameIdx < 0 || frameIdx >= loaded.frames.size()) frameIdx = 0;
        return loaded.frames.get(frameIdx);
    }
}
