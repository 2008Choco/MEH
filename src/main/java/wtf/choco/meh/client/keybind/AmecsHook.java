package wtf.choco.meh.client.keybind;

import com.mojang.blaze3d.platform.InputConstants;

import java.util.function.BooleanSupplier;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;

import de.siphalor.amecs.api.KeyModifiers;

// This has to be in a separate class to avoid class loading exceptions with Fabric
final class AmecsHook {

    static KeyMapping createAmecsKeybinding(ResourceLocation id, InputConstants.Type type, int key, String category, boolean control, boolean shift, BooleanSupplier onPress) {
        KeyModifiers defaultModifiers = new KeyModifiers().setControl(control).setShift(shift);
        return new AmecsPriorityKeyBinding(id, type, key, category, defaultModifiers, onPress);
    }

}
