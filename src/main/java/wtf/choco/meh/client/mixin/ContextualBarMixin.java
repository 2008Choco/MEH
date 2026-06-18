package wtf.choco.meh.client.mixin;

import net.minecraft.client.gui.contextualbar.ContextualBar;
import org.spongepowered.asm.mixin.Mixin;
import wtf.choco.meh.client.mixinapi.MEHContextualBar;

@Mixin(ContextualBar.class)
public interface ContextualBarMixin extends MEHContextualBar {

}
