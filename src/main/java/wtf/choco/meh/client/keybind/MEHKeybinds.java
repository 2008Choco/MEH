package wtf.choco.meh.client.keybind;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public final class MEHKeybinds {

    public static final String CATEGORY_MEH = "key.categories.meh";

    // Standard key mappings
    public static final KeyMapping OPEN_CUSTOM_STATUS_SCREEN = registerKeybind("open_custom_status_screen", InputConstants.UNKNOWN.getValue());

    private MEHKeybinds() { }

    public static void init() { }

    private static KeyMapping registerKeybind(String keyId, int key) {
        KeyMapping keybind = new KeyMapping(keyId, key, CATEGORY_MEH);
        KeyBindingHelper.registerKeyBinding(keybind);
        return keybind;
    }

}
