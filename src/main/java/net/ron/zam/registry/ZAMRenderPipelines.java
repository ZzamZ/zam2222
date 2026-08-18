package net.ron.zam.registry;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.ron.zam.ZAMMod;
import net.ron.zam.mixin.RenderPipelinesAccessor;

public final class ZAMRenderPipelines {

    public static RenderPipeline ZAM_CRT;

    private ZAMRenderPipelines() {}

    public static void register() {
        ZAM_CRT = RenderPipelinesAccessor.zam$register(
                RenderPipeline.builder(RenderPipelinesAccessor.zam$entitySnippet())
                        .withLocation(ZAMMod.id("pipeline/zam_crt"))
                        .withVertexShader(ZAMMod.id("core/zam_crt"))
                        .withFragmentShader(ZAMMod.id("core/zam_crt"))
                        .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
                        .withDepthStencilState(DepthStencilState.DEFAULT)
                        .build());
    }
}