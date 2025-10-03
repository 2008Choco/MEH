package wtf.choco.meh.client.keybind;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

import wtf.choco.meh.client.MEHClient;

public final class MEHKeybinds {

    public static final KeyMapping.Category CATEGORY_MEH = KeyMapping.Category.register(MEHClient.key("meh"));

    public static final KeyMapping OPEN_CUSTOM_STATUS_SCREEN = registerKeybind("open_custom_status_screen", InputConstants.UNKNOWN.getValue());
    public static final KeyMapping TOGGLE_BARRIER_BLOCK_RENDERING = registerKeybind("toggle_barrier_block_rendering", InputConstants.UNKNOWN.getValue());

    private MEHKeybinds() { }

    public static void init() { }

    private static KeyMapping registerKeybind(String keyId, int key) {
        KeyMapping keybind = new KeyMapping(keyId, key, CATEGORY_MEH);
        KeyBindingHelper.registerKeyBinding(keybind);
        return keybind;
    }

}
