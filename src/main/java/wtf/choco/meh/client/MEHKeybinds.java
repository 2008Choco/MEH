package wtf.choco.meh.client;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

import org.lwjgl.glfw.GLFW;

public final class MEHKeybinds {

    public static final KeyMapping KEY_SWITCH_CHANNEL = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.meh.switch_chat_channel",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_0,
            KeyMapping.CATEGORY_MULTIPLAYER
    ));

    private MEHKeybinds() { }

    public static void init() {}

}
