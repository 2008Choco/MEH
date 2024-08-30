package wtf.choco.meh.client.mixin;

import net.minecraft.client.KeyboardHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import wtf.choco.meh.client.event.LWJGLEvents;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {

    @SuppressWarnings("unused") // windowId
    @Inject(method = "keyPress(JIIII)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/InputConstants;isKeyDown(JI)Z"), cancellable = true)
    private void onKeyPress(long windowId, int key, int scancode, int action, int mods, CallbackInfo callback) {
        if (!LWJGLEvents.KEY_STATE_CHANGE.invoker().onKeyStateChange(key, scancode, action, mods)) {
            callback.cancel();
        }
    }

}
