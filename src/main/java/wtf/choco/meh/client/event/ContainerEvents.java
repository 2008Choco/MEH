package wtf.choco.meh.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class ContainerEvents {

    /**
     * A callback for when an item is picked up in an inventory with an empty item on the cursor.
     */
    public static final Event<PickupItem> PICKUP_ITEM = EventFactory.createArrayBacked(PickupItem.class,
            listeners -> (menu, player, slot, itemStack) -> {
                for (PickupItem event : listeners) {
                    if (!event.onPickupItem(menu, player, slot, itemStack)) {
                        return false;
                    }
                }

                return true;
            }
    );

    private ContainerEvents() { }

    @FunctionalInterface
    public static interface PickupItem {

        /**
         * Called when the client tries to pickup an item in an inventory with an empty item on its cursor.
         *
         * @param menu the menu in which the click occurred
         * @param player the player
         * @param slot the slot that was clicked
         * @param itemStack the item stack in the slot
         *
         * @return true to allow the event to proceed, false to cancel, not pickup the item, and not inform
         * the server of the pickup
         */
        public boolean onPickupItem(AbstractContainerMenu menu, Player player, Slot slot, ItemStack itemStack);

    }

}
