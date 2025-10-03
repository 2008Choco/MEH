package wtf.choco.meh.client.event;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.input.KeyEvent;

/**
 * Contains events dispatched closer to the LWJGL level.
 */
public final class LWJGLEvents {

    /**
     * Callback for when a key state changes from LWJGL.
     */
    public static final Event<KeyStateChange> KEY_STATE_CHANGE = EventFactory.createArrayBacked(KeyStateChange.class,
            listeners -> (action, keyEvent) -> {
                for (KeyStateChange event : listeners) {
                    if (!event.onKeyStateChange(action, keyEvent)) {
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
         * @param action the action performed (see {@link InputConstants#PRESS} or {@link InputConstants#RELEASE})
         * @param event the event context
         *
         * @return true if the key state change should continue to be processed, or false to stop processing
         */
        public boolean onKeyStateChange(int action, KeyEvent event);

    }

}
