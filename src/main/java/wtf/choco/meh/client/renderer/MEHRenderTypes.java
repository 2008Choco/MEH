package wtf.choco.meh.client.renderer;

import java.util.function.Function;

import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public final class MEHRenderTypes {

    private static final Function<ResourceLocation, RenderType> WORLD_POSITIONED_GUI = Util.memoize(textureLocation -> RenderType.create(
            "world_positioned_gui",
            1536,
            MEHRenderPipelines.WORLD_POSITIONED_GUI,
            RenderType.CompositeState.builder()
                .setTextureState(new RenderStateShard.TextureStateShard(textureLocation, false))
                .createCompositeState(false)
    ));

    private MEHRenderTypes() { }

    public static RenderType worldPositionedGui(ResourceLocation textureLocation) {
        return WORLD_POSITIONED_GUI.apply(textureLocation);
    }

}
