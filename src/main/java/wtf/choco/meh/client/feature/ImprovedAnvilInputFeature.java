package wtf.choco.meh.client.feature;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.MEHConfig;

public final class ImprovedAnvilInputFeature extends Feature {

    public ImprovedAnvilInputFeature(MEHClient mod) {
        super(mod);
    }

    @Override
    protected boolean isFeatureEnabled(MEHConfig config) {
        return config.isImprovedAnvilInput();
    }

    @Override
    protected void registerListeners() {
        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!isEnabled() || !(screen instanceof AnvilScreen anvilScreen)) {
                return;
            }

            ScreenKeyboardEvents.afterKeyPress(screen).register((screen_, context) -> {
                if (context.key() != InputConstants.KEY_RETURN && context.key() != InputConstants.KEY_NUMPADENTER) {
                    return;
                }

                AnvilMenu menu = anvilScreen.getMenu();
                Slot inputSlot = menu.getSlot(0);
                Slot resultSlot = menu.getSlot(menu.getResultSlot());
                if (!inputSlot.hasItem() || !resultSlot.hasItem()) {
                    return;
                }

                client.gameMode.handleInventoryMouseClick(menu.containerId, resultSlot.index, InputConstants.MOUSE_BUTTON_LEFT, ClickType.PICKUP, client.player);
            });
        });
    }

}
