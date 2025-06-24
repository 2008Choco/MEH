package wtf.choco.meh.client.mixin;

import net.minecraft.client.gui.contextualbar.ContextualBarRenderer;

import org.spongepowered.asm.mixin.Mixin;

import wtf.choco.meh.client.mixinapi.MEHContextualBarRenderer;

@Mixin(ContextualBarRenderer.class)
public interface ContextualBarRendererMixin extends MEHContextualBarRenderer {

}
