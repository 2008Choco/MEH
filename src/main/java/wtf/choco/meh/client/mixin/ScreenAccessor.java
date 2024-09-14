package wtf.choco.meh.client.mixin;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Screen.class)
public interface ScreenAccessor {

    @Invoker("addRenderableOnly")
    public <T extends Renderable> T invokeAddRenderableOnly(T renderable);

}
