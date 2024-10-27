package wtf.choco.meh.client.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public final class ConfigMurderMystery {

    @ConfigEntry.Gui.Tooltip
    private boolean retextured_murderer_knives = true;

    ConfigMurderMystery() { }

    public boolean isRetexturedMurdererKnives() {
        return retextured_murderer_knives;
    }

}
