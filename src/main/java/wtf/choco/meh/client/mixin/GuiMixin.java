package wtf.choco.meh.client.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import wtf.choco.meh.client.event.ClientTitleEvents;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Shadow
    private Component title;
    @Shadow
    private Component subtitle;
    @Shadow
    private int titleFadeInTime;
    @Shadow
    private int titleStayTime;
    @Shadow
    private int titleFadeOutTime;
    @Shadow
    private int titleTime;

    @SuppressWarnings("unused")
    @Inject(method = "renderTitle(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V", cancellable = true, at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/gui/Gui;getFont()Lnet/minecraft/client/gui/Font;",
        shift = At.Shift.BEFORE
    ))
    private void onRenderTitle(GuiGraphics graphics, DeltaTracker delta, CallbackInfo callback) {
        if (!ClientTitleEvents.TITLE_RENDER.invoker().onTitleRender(title, subtitle, titleFadeInTime, titleStayTime, titleFadeOutTime, titleTime)) {
            this.clear();
            callback.cancel();
        }
    }

    @Shadow
    public abstract void clear();

}
