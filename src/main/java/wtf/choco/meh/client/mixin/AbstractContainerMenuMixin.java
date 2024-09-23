package wtf.choco.meh.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import wtf.choco.meh.client.event.ContainerEvents;
import wtf.choco.meh.client.util.SharedMixinValues;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {

    @Inject(
        method = "doClick(IILnet/minecraft/world/inventory/ClickType;Lnet/minecraft/world/entity/player/Player;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/inventory/Slot;tryRemove(IILnet/minecraft/world/entity/player/Player;)Ljava/util/Optional;",
            shift = At.Shift.BEFORE,
            by = 1
        ),
        cancellable = true
    )
    @SuppressWarnings("unused")
    public void onDoClick(int mouseX, int mouseY, ClickType clickType, Player player, CallbackInfo callback, @Local Slot slot, @Local(ordinal = 0) ItemStack itemStack) {
        if (!ContainerEvents.PICKUP_ITEM.invoker().onPickupItem((AbstractContainerMenu) (Object) this, player, slot, itemStack)) {
            callback.cancel();
            SharedMixinValues.cancelNextMenuClick = true;
        }
    }

}
