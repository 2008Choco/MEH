package wtf.choco.meh.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.contextualbar.ContextualBarRenderer;

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

    private GuiEvents() { }

    @FunctionalInterface
    public static interface ContextualBarOverride {

        public ContextualBarRenderer getContextualBarRenderer(Gui.ContextualInfo vanillaInfo, ContextualBarRenderer vanillaRenderer);

    }

}
