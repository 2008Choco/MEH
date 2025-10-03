package wtf.choco.meh.client.mixin;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import wtf.choco.meh.client.event.LWJGLEvents;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {

    @SuppressWarnings("unused") // windowId
    @Inject(method = "keyPress(JILnet/minecraft/client/input/KeyEvent;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/InputConstants;isKeyDown(Lcom/mojang/blaze3d/platform/Window;I)Z", ordinal = 0), cancellable = true)
    private void onKeyPress(long windowId, int action, KeyEvent event, CallbackInfo callback) {
        if (!LWJGLEvents.KEY_STATE_CHANGE.invoker().onKeyStateChange(action, event)) {
            callback.cancel();
        }
    }

}
