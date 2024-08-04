package wtf.choco.meh.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;

/**
 * Contains client-sided events triggered by interacting with the chat screen window.
 */
public final class ChatScreenEvents {

    public static final Event<KeyPress> KEY_PRESS = EventFactory.createArrayBacked(KeyPress.class,
            listeners -> (screen, key, keycode, scancode) -> {
                for (KeyPress event : listeners) {
                    if (!event.onKeyPress(screen, key, keycode, scancode)) {
                        return false;
                    }
                }

                return true;
            }
    );

    public static final Event<Render> RENDER = EventFactory.createArrayBacked(Render.class,
            listeners -> (screen, graphics, screenX, screenY, delta) -> {
                for (Render event : listeners) {
                    event.onRender(screen, graphics, screenX, screenY, delta);
                }
            }
    );

    private ChatScreenEvents() { }

    @FunctionalInterface
    public interface KeyPress {

        /**
         * Called before a key is pressed while the chat screen is opened.
         *
         * @param screen the active chat screen
         * @param key the key
         * @param keycode the keycode
         * @param scancode the scancode
         *
         * @return false to intercept the key press and not forward it to the chat box, or true
         * to let the key press proceed and type a key in the chat box
         */
        public boolean onKeyPress(ChatScreen screen, int key, int keycode, int scancode);

    }

    @FunctionalInterface
    public interface Render {

        /**
         * Called when the chat screen is opened and being rendered.
         *
         * @param screen the active chat screen
         * @param graphics the graphics context
         * @param screenX the screen x position
         * @param screenY the screen y position
         * @param delta the delta time
         */
        public void onRender(ChatScreen screen, GuiGraphics graphics, int screenX, int screenY, float delta);

    }

}
