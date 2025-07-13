package wtf.choco.meh.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.contextualbar.ContextualBarRenderer;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.Nullable;

public final class GuiEvents {

    public static final Event<ContextualBarOverride> CONTEXTUAL_BAR_OVERRIDE = EventFactory.createArrayBacked(ContextualBarOverride.class,
            listeners -> (vanillaInfo, vanillaRenderer) -> {
                for (ContextualBarOverride event : listeners) {
                    ContextualBarRenderer result = event.getContextualBarRenderer(vanillaInfo, vanillaRenderer);
                    if (result != vanillaRenderer) {
                        return result;
                    }
                }

                return vanillaRenderer;
            }
    );

    /**
     * Callback for when a title (and subtitle) are being rendered on the client.
     */
    public static final Event<TitleRender> TITLE_RENDER = EventFactory.createArrayBacked(TitleRender.class,
            listeners -> (title, subtitle, fadeInTicks, stayTicks, fadeOutTicks, titleTicks) -> {
                for (TitleRender event : listeners) {
                    if (!event.onTitleRender(title, subtitle, fadeInTicks, stayTicks, fadeOutTicks, titleTicks)) {
                        return false;
                    }
                }

                return true;
            }
    );

    private GuiEvents() { }

    @FunctionalInterface
    public static interface ContextualBarOverride {

        public ContextualBarRenderer getContextualBarRenderer(Gui.ContextualInfo vanillaInfo, ContextualBarRenderer vanillaRenderer);

    }

    @FunctionalInterface
    public interface TitleRender {

        /**
         * Called when a title (and subtitle) are being rendered on the client.
         *
         * @param title the title component
         * @param subtitle the subtitle component
         * @param fadeInTicks the fade in ticks
         * @param stayTicks the stay ticks
         * @param fadeOutTicks the fade out ticks
         * @param titleTicks the remaining title ticks
         *
         * @return false if the title render should be cancelled and not shown, true to show it. If
         * this method returns false, the title will be cancelled entirely and not continue to render
         * after this method has returned false
         */
        public boolean onTitleRender(Component title, @Nullable Component subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks, int titleTicks);

    }

}
