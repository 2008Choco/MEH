package wtf.choco.meh.client.event;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Contains events dispatched closer to the LWJGL level.
 */
public final class LWJGLEvents {

    /**
     * Callback for when a key state changes from LWJGL.
     */
    public static final Event<KeyStateChange> KEY_STATE_CHANGE = EventFactory.createArrayBacked(KeyStateChange.class,
            listeners -> (key, scancode, action, mods) -> {
                for (KeyStateChange event : listeners) {
                    if (!event.onKeyStateChange(key, scancode, action, mods)) {
                        return false;
                    }
                }

                return true;
            }
    );

    @FunctionalInterface
    public interface KeyStateChange {

        /**
         * Called when a key state changes while Minecraft is in focus.
         *
         * @param key the key that was changed (see {@link InputConstants}#KEY_{@literal<key>})
         * @param scancode the scancode of the key that was changed
         * @param action the action performed (see {@link InputConstants#PRESS} or {@link InputConstants#RELEASE})
         * @param mods a bitmask of the active key mods (e.g. shift, control, alt, etc.)
         *
         * @return true if the key state change should continue to be processed, or false to stop processing
         */
        public boolean onKeyStateChange(int key, int scancode, int action, int mods);

    }

}
