package wtf.choco.meh.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.Nullable;

public final class ClientTitleEvents {

    /**
     * Callback before a title starts rendering.
     */
    public static final Event<TitleStart> TITLE_START = EventFactory.createArrayBacked(TitleStart.class,
            listeners -> (title, subtitle, fadeInTicks, stayTicks, fadeOutTicks) -> {
                for (TitleStart event : listeners) {
                    if (!event.onTitleStart(title, subtitle, fadeInTicks, stayTicks, fadeOutTicks)) {
                        return false;
                    }
                }

                return true;
            }
    );

    private ClientTitleEvents() { }

    @FunctionalInterface
    public interface TitleStart {

        /**
         * Called when a new title has been sent to the client and has started rendering.
         *
         * @param title the title component
         * @param subtitle the subtitle component
         * @param fadeInTicks the fade in ticks
         * @param stayTicks the stay ticks
         * @param fadeOutTicks the fade out ticks
         *
         * @return false if the title should not be shown, true to show it
         */
        public boolean onTitleStart(Component title, @Nullable Component subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks);

    }

}
