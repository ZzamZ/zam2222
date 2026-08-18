package net.ron.zam.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.RenderPipelines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderPipelines.class)
public interface RenderPipelinesAccessor {
    @Invoker("register")
    static RenderPipeline zam$register(RenderPipeline pipeline) {
        throw new AssertionError();
    }

    @Accessor("ENTITY_SNIPPET")
    static RenderPipeline.Snippet zam$entitySnippet() {
        throw new AssertionError();
    }
}
