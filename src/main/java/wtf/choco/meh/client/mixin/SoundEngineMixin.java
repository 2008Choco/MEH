package wtf.choco.meh.client.mixin;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import wtf.choco.meh.client.event.SoundEvents;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {

    @Inject(
            method = "play(Lnet/minecraft/client/resources/sounds/SoundInstance;)Lnet/minecraft/client/sounds/SoundEngine$PlayResult;",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/resources/sounds/SoundInstance;resolve(Lnet/minecraft/client/sounds/SoundManager;)Lnet/minecraft/client/sounds/WeighedSoundEvents;"
            )
    )
    @SuppressWarnings("unused") // callback
    public void onPlay(SoundInstance soundInstance, CallbackInfoReturnable<SoundEngine.PlayResult> callback) {
        SoundEvents.PLAY.invoker().onSoundPlay(soundInstance);
    }

}
