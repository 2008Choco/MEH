package wtf.choco.meh.client.mnemonic;

import it.unimi.dsi.fastutil.ints.IntList;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import org.spongepowered.include.com.google.common.base.Preconditions;

/**
 * Represents a sequence of keys that may be pressed in order to perform an action.
 */
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

    /**
     * Get the description key of this mnemonic.
     *
     * @return the description key
     */
    public String getDescriptionKey() {
        return getOrCreateDescriptionKey();
    }

    /**
     * Get the display name of this mnemonic as a translatable component using the
     * {@link #getDescriptionKey()}.
     *
     * @return the display name
     */
    public Component getDisplayName() {
        return Component.translatable(getDescriptionKey());
    }

    /**
     * Returns a copy of the array of the mnemonic's key codes (in order).
     *
     * @return the key codes
     */
    public int[] getKeycodes() {
        return Arrays.copyOf(keycodes, keycodes.length);
    }

    /**
     * Get the keycode at the given index.
     *
     * @param index the index of the keycode to get
     *
     * @return the keycode
     *
     * @throws IllegalArgumentException if the index is less than 0 or is greater than or
     * equal to the {@link #size() size} of this mnemonic
     */
    public int keycodeAt(int index) {
        Preconditions.checkArgument(index >= 0 && index < size(), "index out of bounds. Expected value between 0 and %s (exclusive)", size());
        return keycodes[index];
    }

    /**
     * Get the size of this mnemonic, which directly correlates to the amount of keys required to
     * perform this mnemonic.
     *
     * @return the size
     */
    public int size() {
        return keycodes.length;
    }

    /**
     * Check whether or not this mnemonic matches exactly with the given list of keycodes, both in
     * size, order, and actual keycodes.
     *
     * @param keycodes the keycodes to check against
     *
     * @return true if the mnemonic matches exactly with the list of keycodes
     */
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

    /**
     * Create a new {@link Mnemonic}.
     *
     * @param id the id of the mnemonic
     * @param cooldown the (global) cooldown to apply after the mnemonic was successfully performed
     * @param cooldownUnit the unit in which the {@code cooldown} parameter was specified
     * @param keycodes the keycodes that compose this mnemonic
     *
     * @return the mnemonic
     */
    public static Mnemonic of(ResourceLocation id, long cooldown, TimeUnit cooldownUnit, int... keycodes) {
        return new Mnemonic(id, keycodes, cooldown, cooldownUnit);
    }

    /**
     * Create a new {@link Mnemonic} using the default cooldown of {@value #DEFAULT_COOLDOWN} seconds.
     *
     * @param id the id of the mnemonic
     * @param keycodes the keycodes that compose this mnemonic
     *
     * @return the mnemonic
     */
    public static Mnemonic of(ResourceLocation id, int... keycodes) {
        return of(id, DEFAULT_COOLDOWN, DEFAULT_COOLDOWN_UNIT, keycodes);
    }

}
