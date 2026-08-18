package net.ron.zam.api.television;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.ron.zam.mixin.RenderTypeAccessor;
import net.ron.zam.registry.ZAMRenderPipelines;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public final class TelevisionRenderTypes {
    private TelevisionRenderTypes() {}

    public static RenderType cassetteFrame(Identifier textureLocation) {
        return RenderTypes.entityCutoutCull(textureLocation);
    }

    private static final Map<Identifier, RenderType> ZAMZ_CRT_CACHE = new HashMap<>();
    private static final Map<Identifier, RenderType> CRT_FALLBACK_CACHE = new HashMap<>();

    private static final boolean IRIS_OR_OCULUS_LOADED =
            FabricLoader.getInstance().isModLoaded("iris")
                    || FabricLoader.getInstance().isModLoaded("oculus");

    private static boolean shaderApiInitialized = false;
    private static Object irisApiInstance;
    private static Method isShaderPackInUseMethod;

    private static synchronized void initShaderApi() {
        if (shaderApiInitialized) return;
        shaderApiInitialized = true;
        try {
            Class<?> irisApi = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
            irisApiInstance = irisApi.getMethod("getInstance").invoke(null);
            isShaderPackInUseMethod = irisApi.getMethod("isShaderPackInUse");
        } catch (Throwable ignored) {
        }
    }

    public static boolean isShaderPackInUse() {
        if (!IRIS_OR_OCULUS_LOADED) return false;
        initShaderApi();
        if (isShaderPackInUseMethod == null || irisApiInstance == null) return false;
        try {
            return (boolean) isShaderPackInUseMethod.invoke(irisApiInstance);
        } catch (Throwable e) {
            return false;
        }
    }

    public static RenderType zamCrt(Identifier texture) {
        if (isShaderPackInUse()) {
            RenderType cached = CRT_FALLBACK_CACHE.get(texture);
            if (cached != null) return cached;
            RenderType fallback = RenderTypes.entityCutoutCull(texture);
            CRT_FALLBACK_CACHE.put(texture, fallback);
            return fallback;
        }
        RenderType cached = ZAMZ_CRT_CACHE.get(texture);
        if (cached != null) return cached;
        RenderSetup setup = RenderSetup.builder(ZAMRenderPipelines.ZAM_CRT)
                .withTexture("Sampler0", texture)
                .useLightmap()
                .useOverlay()
                .affectsCrumbling()
                .createRenderSetup();
        RenderType created = RenderTypeAccessor.zam$create("zam_crt:" + texture, setup);
        ZAMZ_CRT_CACHE.put(texture, created);
        return created;
    }

    public static Identifier resolveCassetteTexture(Identifier frameId) {
        String path = frameId.getPath();
        if (path.startsWith("textures/") && path.endsWith(".png")) return frameId;
        return Identifier.fromNamespaceAndPath(frameId.getNamespace(), "textures/" + path + ".png");
    }
}
