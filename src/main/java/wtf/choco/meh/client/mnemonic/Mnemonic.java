package wtf.choco.meh.client.mnemonic;

import it.unimi.dsi.fastutil.ints.IntList;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import net.minecraft.network.chat.Component;

import org.spongepowered.include.com.google.common.base.Preconditions;

public record Mnemonic(Component name, long cooldown, TimeUnit cooldownUnit, int[] keycodes) {

    private static final long DEFAULT_COOLDOWN = 10;
    private static final TimeUnit DEFAULT_COOLDOWN_UNIT = TimeUnit.SECONDS;

    /**
     * Returns a copy of the array of the mnemonic's key codes.
     *
     * @return the key codes
     */
    @Override
    public int[] keycodes() {
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

    public long cooldownMillis() {
        return cooldownUnit.toMillis(cooldown);
    }

    public static Mnemonic of(Component name, long cooldown, TimeUnit cooldownUnit, int... keycodes) {
        return new Mnemonic(name, cooldown, cooldownUnit, keycodes);
    }

    public static Mnemonic of(Component name, int... keycodes) {
        return of(name, DEFAULT_COOLDOWN, DEFAULT_COOLDOWN_UNIT, keycodes);
    }

}
