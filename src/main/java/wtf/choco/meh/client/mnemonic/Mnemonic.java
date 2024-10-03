package wtf.choco.meh.client.mnemonic;

import it.unimi.dsi.fastutil.ints.IntList;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import org.spongepowered.include.com.google.common.base.Preconditions;

public final class Mnemonic {

    private static final long DEFAULT_COOLDOWN = 10;
    private static final TimeUnit DEFAULT_COOLDOWN_UNIT = TimeUnit.SECONDS;

    private String descriptionKey;

    private final ResourceLocation id;
    private final int[] keycodes;
    private final long cooldownMillis;

    private Mnemonic(ResourceLocation id, int[] keycodes, long cooldown, TimeUnit cooldownUnit) {
        this.id = id;
        this.keycodes = keycodes;
        this.cooldownMillis = cooldownUnit.toMillis(cooldown);
    }

    private String getOrCreateDescriptionKey() {
        if (descriptionKey == null) {
            this.descriptionKey = Util.makeDescriptionId("mnemonic", id);
        }

        return descriptionKey;
    }

    public String getDescriptionKey() {
        return getOrCreateDescriptionKey();
    }

    public Component getDisplayName() {
        return Component.translatable(getDescriptionKey());
    }

    /**
     * Returns a copy of the array of the mnemonic's key codes.
     *
     * @return the key codes
     */
    public int[] getKeycodes() {
        return Arrays.copyOf(keycodes, keycodes.length);
    }

    public int keycodeAt(int index) {
        Preconditions.checkArgument(index >= 0 && index < size(), "index out of bounds. Expected value between 0 and %s (exclusive)", size());
        return keycodes[index];
    }

    public int size() {
        return keycodes.length;
    }

    public boolean matchesExactly(IntList keycodes) {
        if (keycodes.size() != size()) {
            return false;
        }

        for (int i = 0; i < this.keycodes.length; i++) {
            if (this.keycodes[i] != keycodes.getInt(i)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Get the cooldown for this mnemonic (in milliseconds).
     *
     * @return the cooldown in milliseconds
     */
    public long getCooldown() {
        return cooldownMillis;
    }

    public static Mnemonic of(ResourceLocation id, long cooldown, TimeUnit cooldownUnit, int... keycodes) {
        return new Mnemonic(id, keycodes, cooldown, cooldownUnit);
    }

    public static Mnemonic of(ResourceLocation id, int... keycodes) {
        return of(id, DEFAULT_COOLDOWN, DEFAULT_COOLDOWN_UNIT, keycodes);
    }

}
