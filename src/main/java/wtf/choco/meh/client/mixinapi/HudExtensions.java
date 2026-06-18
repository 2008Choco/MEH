package wtf.choco.meh.client.mixinapi;

import net.minecraft.client.gui.Hud;
import net.minecraft.client.gui.contextualbar.ContextualBar;

import java.util.function.Supplier;

public interface HudExtensions {

    public static HudExtensions get(Hud hud) {
        return (HudExtensions) hud;
    }

    public void setHideHealthInformation(boolean hide);

}
