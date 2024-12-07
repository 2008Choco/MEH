package wtf.choco.meh.client.mnemonic;

import com.mojang.blaze3d.platform.InputConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;

import wtf.choco.meh.client.event.LWJGLEvents;
import wtf.choco.meh.client.event.MEHEvents;

/**
 * Represents a handler capable of managing and keeping track of active mnemonics and performing
 * them if the client is successful in typing one.
 */
public final class MnemonicHandler {

    private static final long MNEMONIC_KEY_PRESS_WAIT_PERIOD_MILLIS = 50;
    private static final long MNEMONIC_TIMEOUT_MILLIS = 1000;

    private long lastPressedKeyTimestamp = 0L;
    private Mnemonic lastSuccessfulMnemonic = null;
    private long lastSuccessfulMnemonicTimestamp = 0L;

    private MnemonicStack stack;

    public void initialize() {
        LWJGLEvents.KEY_STATE_CHANGE.register(this::onKeyStateChange);
    }

    @SuppressWarnings("unused")
    private boolean onKeyStateChange(int keycode, int scancode, int action, int mods) {
        // Don't process anything if there's a screen open or if the action was not a press
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen != null || action != InputConstants.PRESS) {
            return true;
        }

        long now = System.currentTimeMillis();
        long difference = (now - lastPressedKeyTimestamp);

        // Don't allow mnemonic keys to be spam pressed
        if (difference < MNEMONIC_KEY_PRESS_WAIT_PERIOD_MILLIS) {
            return true;
        }

        // Apply a cooldown if necessary
        if (lastSuccessfulMnemonic != null && lastSuccessfulMnemonicTimestamp + lastSuccessfulMnemonic.getCooldown() > now) {
            return true;
        }

        // If there's no stack yet, or if the last attempt at a mnemonic was too long ago, start from the beginning
        if (stack == null || difference > MNEMONIC_TIMEOUT_MILLIS) {
            List<Mnemonic> possibleMnemonics = Mnemonics.stream()
                .filter(mnemonic -> mnemonic.size() > 0)
                .filter(mnemonic -> mnemonic.keycodeAt(0) == keycode)
                .collect(Collectors.toCollection(ArrayList::new));

            if (possibleMnemonics.isEmpty()) {
                return true;
            }

            this.stack = new MnemonicStack(possibleMnemonics);
        }

        // Push the pressed key to the stack and don't continue if there are no more possible mnemonics
        if (!stack.pushKey(keycode)) {
            this.stack = null;
            return true;
        }

        this.lastPressedKeyTimestamp = now;

        // Check if there's a single mnemonic that matches all the pressed keys so far
        Mnemonic mnemonic = stack.get();
        if (mnemonic == null) {
            return true;
        }

        // Process an event that a mnemonic succeeded! :)
        boolean result = MEHEvents.MNEMONIC_COMPLETION.invoker().onMnemonic(mnemonic);
        this.lastSuccessfulMnemonic = mnemonic;
        this.lastSuccessfulMnemonicTimestamp = now;
        return !result;
    }

}
