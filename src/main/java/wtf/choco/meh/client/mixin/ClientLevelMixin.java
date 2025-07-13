package wtf.choco.meh.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import wtf.choco.meh.client.event.MEHClientEntityEvents;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {

    @SuppressWarnings("unused") // callback
    @Inject(
        method = "addEntity(Lnet/minecraft/world/entity/Entity;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/entity/TransientEntitySectionManager;addEntity(Lnet/minecraft/world/level/entity/EntityAccess;)V"
        )
    )
    private void onAddEntity(Entity entity, CallbackInfo callback) {
        MEHClientEntityEvents.ENTITY_ADD.invoker().onEntityAdd(entity);
    }

    @SuppressWarnings("unused") // entityId, callback
    @Inject(
        method = "removeEntity(ILnet/minecraft/world/entity/Entity$RemovalReason;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;setRemoved(Lnet/minecraft/world/entity/Entity$RemovalReason;)V"
        )
    )
    private void onEntityRemove(int entityId, Entity.RemovalReason reason, CallbackInfo callback, @Local Entity entity) {
        MEHClientEntityEvents.ENTITY_REMOVE.invoker().onEntityRemove(entity, reason);
    }

}
