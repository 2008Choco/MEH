package wtf.choco.meh.client.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import wtf.choco.meh.client.feature.Features;

@Mixin(ClientLevel.class)
public final class ClientLevelMixin {

    @Inject(method = "getMarkerParticleTarget()Lnet/minecraft/world/level/block/Block", at = @At("HEAD"), cancellable = true)
    private void getMarkerParticleTarget(CallbackInfoReturnable<Block> callback) {
        if (Features.BARRIER_RENDERER.isEnabled()) {
            callback.setReturnValue(Blocks.BARRIER);
        }
    }

}
