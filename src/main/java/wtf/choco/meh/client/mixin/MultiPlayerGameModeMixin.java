package wtf.choco.meh.client.mixin;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import wtf.choco.meh.client.util.SharedMixinValues;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @Inject(
        method = "handleInventoryMouseClick(IIILnet/minecraft/world/inventory/ClickType;Lnet/minecraft/world/entity/player/Player;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;clicked(IILnet/minecraft/world/inventory/ClickType;Lnet/minecraft/world/entity/player/Player;)V"
        ),
        cancellable = true
    )
    @SuppressWarnings("unused")
    public void onHandleInventoryMouseClick(int containerId, int mouseX, int mouseY, ClickType clickType, Player player, CallbackInfo callback) {
        if (SharedMixinValues.cancelNextMenuClick) {
            SharedMixinValues.cancelNextMenuClick = false;
            callback.cancel();
        }
    }

}
