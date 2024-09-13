package wtf.choco.meh.client;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

import org.lwjgl.glfw.GLFW;

import wtf.choco.meh.client.mixin.KeyMappingAccessor;

public final class MEHKeybinds {

    public static final String CATEGORY_MEH = "key.categories.meh";

    public static final KeyMapping KEY_SWITCH_CHAT_CHANNEL = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.meh.switch_chat_channel",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_TAB,
            CATEGORY_MEH
    ));

    public static final KeyMapping KEY_DELETE_CURRENT_CHAT_CHANNEL = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.meh.delete_current_chat_channel",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_MINUS,
            CATEGORY_MEH
    ));

    private MEHKeybinds() { }

    public static void init() { }

    public static int getRawKey(KeyMapping key) {
        return ((KeyMappingAccessor) key).getKey().getValue();
    }

}
