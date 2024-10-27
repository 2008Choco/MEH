package wtf.choco.meh.client.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

public final class Components {

    private static final Style STYLE_NON_ITALIC = Style.EMPTY.withItalic(false);

    private Components() { }

    public static MutableComponent emptyNonItalic() {
        return Component.empty().setStyle(STYLE_NON_ITALIC);
    }

    public static boolean anyMatch(Component[] components, Component input) {
        boolean dev = FabricLoader.getInstance().isDevelopmentEnvironment();

        for (Component component : components) {
            /*
             * It's difficult to get coloured items in single player "vanilla", so we're going
             * to just ignore colour codes when in a development environment as a quick band-aid patch.
             */
            if ((dev && input.getString().equals(component.getString())) || component.equals(input)) {
                return true;
            }
        }

        return false;
    }

}
