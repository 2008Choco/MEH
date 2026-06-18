package wtf.choco.meh.client.mixin;

import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Hud.ContextualInfo.class)
public enum ContextualInfoMixin {

    MEH_SKYBLOCK_SKILL;

}
