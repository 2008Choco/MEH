package wtf.choco.meh.client.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public final class ConfigMainLobbyFishing {

    @ConfigEntry.Gui.Tooltip
    private boolean retextured_fishing_rods = true;

    ConfigMainLobbyFishing() { }

    public boolean isRetexturedFishingRodsEnabled() {
        return retextured_fishing_rods;
    }

}
