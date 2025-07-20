package wtf.choco.meh.client.renderer;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.RenderPipelines;

import wtf.choco.meh.client.MEHClient;

public final class MEHRenderPipelines {

    public static final RenderPipeline WORLD_POSITIONED_GUI = RenderPipelines.register(RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
            .withLocation(MEHClient.key("pipeline/world_positioned_gui"))
            .withVertexShader(MEHClient.key("core/rendertype_world_positioned_gui"))
            .withFragmentShader(MEHClient.key("core/rendertype_world_positioned_gui"))
            .withSampler("Sampler0")
            .withCull(true)
            .withBlend(BlendFunction.TRANSLUCENT)
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
            .build());

    private MEHRenderPipelines() { }

}
