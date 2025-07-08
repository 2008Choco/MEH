package wtf.choco.meh.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class MenuEvents {

    public static final Event<SlotItemStackChange> SLOT_ITEM_STACK_CHANGE = EventFactory.createArrayBacked(SlotItemStackChange.class,
            listeners -> (menu, slot, itemStack) -> {
                for (SlotItemStackChange event : listeners) {
                    event.onSlotItemStackChange(menu, slot, itemStack);
                }
            }
    );

    private MenuEvents() { }

    @FunctionalInterface
    public static interface SlotItemStackChange {

        public void onSlotItemStackChange(AbstractContainerMenu menu, Slot slot, ItemStack itemStack);

    }

}
