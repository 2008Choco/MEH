package wtf.choco.meh.client.keybind;

import com.mojang.blaze3d.platform.InputConstants;

import java.util.function.BooleanSupplier;

import net.minecraft.resources.ResourceLocation;

import de.siphalor.amecs.api.AmecsKeyBinding;
import de.siphalor.amecs.api.KeyModifiers;
import de.siphalor.amecs.api.PriorityKeyBinding;

public final class AmecsPriorityKeyBinding extends AmecsKeyBinding implements PriorityKeyBinding {

    private final BooleanSupplier onPress;

    public AmecsPriorityKeyBinding(ResourceLocation id, InputConstants.Type type, int code, String category, KeyModifiers defaultModifiers, BooleanSupplier onPress) {
        super(id, type, code, category, defaultModifiers);
        this.onPress = onPress;
    }

    @Override
    public boolean onPressedPriority() {
        return onPress.getAsBoolean();
    }

}
