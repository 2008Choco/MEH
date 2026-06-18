package wtf.choco.meh.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.gui.contextualbar.ContextualBar;
import wtf.choco.meh.client.util.RegistrationInvoker;

import java.util.function.Supplier;

public final class GuiEvents {

    public static final Event<RegisterContextualInfo> REGISTER_CONTEXTUAL_INFO = EventFactory.createArrayBacked(RegisterContextualInfo.class,
            listeners -> (registrar) -> {
                for (RegisterContextualInfo event : listeners) {
                    event.onRegisterContextualInfo(registrar);
                }
            }
    );

    public static final Event<ContextualBarOverride> CONTEXTUAL_BAR_OVERRIDE = EventFactory.createArrayBacked(ContextualBarOverride.class,
            listeners -> (vanillaInfo) -> {
                for (ContextualBarOverride event : listeners) {
                    Hud.ContextualInfo result = event.getContextualBarOverride(vanillaInfo);
                    if (result != vanillaInfo) {
                        return result;
                    }
                }

                return vanillaInfo;
            }
    );

    private GuiEvents() { }

    @FunctionalInterface
    public interface RegisterContextualInfo {

        public void onRegisterContextualInfo(RegistrationInvoker<Hud.ContextualInfo, Supplier<ContextualBar>> registrar);

    }

    @FunctionalInterface
    public interface ContextualBarOverride {

        public Hud.ContextualInfo getContextualBarOverride(Hud.ContextualInfo vanillaInfo);

    }

}
