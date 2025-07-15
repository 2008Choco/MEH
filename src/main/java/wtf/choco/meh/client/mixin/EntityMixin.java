package wtf.choco.meh.client.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import wtf.choco.meh.client.event.MEHClientEntityEvents;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @SuppressWarnings("unused") // callback
    @Inject(method = "setCustomName(Lnet/minecraft/network/chat/Component;)V", at = @At("HEAD"))
    private void onSetCustomName(@Nullable Component customName, CallbackInfo callback) {
        Entity entity = (Entity) (Object) this;
        if (entity.level().isClientSide()) {
            MEHClientEntityEvents.SET_CUSTOM_NAME.invoker().onSetCustomName(entity, customName);
        }
    }

    @SuppressWarnings("unused") // callback
    @Inject(target = @Desc(value = "onSyncedDataUpdated", args = EntityDataAccessor.class), at = @At("HEAD"))
    private void onOnSyncedDataUpdate(EntityDataAccessor<?> data, CallbackInfo callback) {
        Entity entity = (Entity) (Object) this;
        if (entity.level().isClientSide() && Entity.DATA_CUSTOM_NAME.equals(data)) {
            MEHClientEntityEvents.SET_CUSTOM_NAME.invoker().onSetCustomName(entity, getCustomName());
        }
    }

    @Shadow
    public abstract Component getCustomName();

}
