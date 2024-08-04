package wtf.choco.meh.client.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import wtf.choco.meh.client.event.ClientTitleEvents;

@Mixin(Gui.class)
public class GuiMixin {

    @Nullable
    @Shadow
    private Component subtitle;
    @Shadow
    private int titleFadeInTime;
    @Shadow
    private int titleStayTime;
    @Shadow
    private int titleFadeOutTime;

    @Inject(method = "setTitle(Lnet/minecraft/network/chat/Component;)V", at = @At("HEAD"), cancellable = true)
    private void onSetTitle(Component title, CallbackInfo callback) {
        if (!ClientTitleEvents.TITLE_START.invoker().onTitleStart(title, subtitle, titleFadeInTime, titleStayTime, titleFadeOutTime)) {
            callback.cancel();
        }
    }

}
