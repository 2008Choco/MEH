package wtf.choco.meh.client.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import wtf.choco.meh.client.event.ChatScreenEvents;

@Mixin(ChatScreen.class)
public class MixinChatScreen {

    @Inject(method = "keyPressed(III)Z", at = @At("HEAD"), cancellable = true)
    private void onKeyPressed(int key, int keycode, int scancode, CallbackInfoReturnable<Boolean> callback) {
        if (!ChatScreenEvents.KEY_PRESS.invoker().onKeyPress(self(), key, keycode, scancode)) {
            callback.setReturnValue(true);
        }
    }

    @SuppressWarnings("unused")
    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At("HEAD"))
    private void onRender(GuiGraphics graphics, int screenX, int screenY, float delta, CallbackInfo callback) {
        ChatScreenEvents.RENDER.invoker().onRender(self(), graphics, screenX, screenY, delta);
    }

    private ChatScreen self() {
        return ((ChatScreen) ((Object) this));
    }

}
