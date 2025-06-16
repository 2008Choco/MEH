package wtf.choco.meh.client.mixinapi;

import net.minecraft.client.gui.Gui;

public interface GuiExtensions {

    public static GuiExtensions get(Gui gui) {
        return (GuiExtensions) gui;
    }

    public void setHideHealthInformation(boolean hide);

}
