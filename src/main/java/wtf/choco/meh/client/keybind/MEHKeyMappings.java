package wtf.choco.meh.client.keybind;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;

import wtf.choco.meh.client.MEHClient;

public final class MEHKeyMappings {

    public static final KeyMapping.Category CATEGORY_MEH = KeyMapping.Category.register(MEHClient.key("meh"));

    public static final KeyMapping OPEN_CUSTOM_STATUS_SCREEN = register("open_custom_status_screen", InputConstants.UNKNOWN.getValue());
    public static final KeyMapping TOGGLE_BARRIER_BLOCK_RENDERING = register("toggle_barrier_block_rendering", InputConstants.UNKNOWN.getValue());

    private MEHKeyMappings() { }

    public static void init() { }

    private static KeyMapping register(String keyId, int key) {
        KeyMapping mapping = new KeyMapping(keyId, key, CATEGORY_MEH);
        KeyMappingHelper.registerKeyMapping(mapping);
        return mapping;
    }

}
