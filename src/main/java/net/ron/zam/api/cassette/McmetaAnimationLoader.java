package net.ron.zam.api.cassette;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.ron.zam.ZAMMod;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class McmetaAnimationLoader {

    private static final Map<Identifier, Animation> CACHE = new HashMap<>();
    private static final Animation NONE = new Animation(1, 1, null);

    private McmetaAnimationLoader() {}

    public record Animation(int frameCount, int frametime, int @Nullable [] frameOrder) {
        public boolean isAnimated() {
            return frameCount > 1;
        }

        public float[] vRangeFor(int playbackTicks) {
            int totalFrames = frameOrder != null ? frameOrder.length : frameCount;
            int slot = Math.floorDiv(playbackTicks, Math.max(1, frametime)) % totalFrames;
            int frame = frameOrder != null ? frameOrder[slot] : slot;
            float h = 1.0F / (float) frameCount;
            return new float[]{frame * h, (frame + 1) * h};
        }
    }

    public static Animation getOrLoad(Identifier texturePath) {
        Animation cached = CACHE.get(texturePath);
        if (cached != null) return cached;

        Identifier mcmetaPath = Identifier.fromNamespaceAndPath(texturePath.getNamespace(),
                texturePath.getPath() + ".mcmeta");

        Minecraft mc = Minecraft.getInstance();
        if (mc == null) {
            CACHE.put(texturePath, NONE);
            return NONE;
        }
        Optional<Resource> resourceOpt = mc.getResourceManager().getResource(mcmetaPath);
        if (resourceOpt.isEmpty()) {
            CACHE.put(texturePath, NONE);
            return NONE;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resourceOpt.get().open(), StandardCharsets.UTF_8))) {
            JsonElement root = JsonParser.parseReader(reader);
            if (!root.isJsonObject()) {
                CACHE.put(texturePath, NONE);
                return NONE;
            }
            JsonObject obj = root.getAsJsonObject();
            if (!obj.has("animation") || !obj.get("animation").isJsonObject()) {
                CACHE.put(texturePath, NONE);
                return NONE;
            }
            JsonObject anim = obj.getAsJsonObject("animation");
            int frametime = anim.has("frametime") ? anim.get("frametime").getAsInt() : 1;

            int[] frameOrder = null;
            int frameCount;
            if (anim.has("frames") && anim.get("frames").isJsonArray()) {
                var arr = anim.getAsJsonArray("frames");
                frameOrder = new int[arr.size()];
                int max = 0;
                for (int i = 0; i < arr.size(); i++) {
                    int f = arr.get(i).isJsonObject()
                            ? arr.get(i).getAsJsonObject().get("index").getAsInt()
                            : arr.get(i).getAsInt();
                    frameOrder[i] = f;
                    if (f > max) max = f;
                }
                frameCount = max + 1;
            } else {
                frameCount = inferFrameCount(texturePath);
            }

            Animation result = new Animation(Math.max(1, frameCount), Math.max(1, frametime), frameOrder);
            CACHE.put(texturePath, result);
            return result;
        } catch (Exception e) {
            ZAMMod.LOGGER.error("Failed to parse mcmeta {}", mcmetaPath, e);
            CACHE.put(texturePath, NONE);
            return NONE;
        }
    }

    private static int inferFrameCount(Identifier texturePath) {
        Minecraft mc = Minecraft.getInstance();
        Optional<Resource> resourceOpt = mc.getResourceManager().getResource(texturePath);
        if (resourceOpt.isEmpty()) return 1;
        try (var stream = resourceOpt.get().open()) {
            var image = com.mojang.blaze3d.platform.NativeImage.read(stream);
            int w = image.getWidth();
            int h = image.getHeight();
            image.close();
            if (w <= 0) return 1;
            return Math.max(1, h / w);
        } catch (Exception e) {
            return 1;
        }
    }
}
