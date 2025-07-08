package wtf.choco.meh.client.mixin;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import wtf.choco.meh.client.event.MenuEvents;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {

    @SuppressWarnings("unused") // stateId
    @Inject(method = "setItem(IILnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"))
    private void onSetItem(int slotIndex, int stateId, ItemStack itemStack, CallbackInfo callback) {
        MenuEvents.SLOT_ITEM_STACK_CHANGE.invoker().onSlotItemStackChange((AbstractContainerMenu) (Object) this, getSlot(slotIndex), itemStack);
    }

    @Shadow
    public abstract Slot getSlot(int index);

}
