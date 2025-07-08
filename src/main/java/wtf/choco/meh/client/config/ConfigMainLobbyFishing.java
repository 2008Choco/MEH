package wtf.choco.meh.client.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public final class ConfigMainLobbyFishing {

    @ConfigEntry.Gui.Tooltip
    private boolean retextured_fishing_rods = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    private ConfigFishingStatOverlay fishing_stat_overlay = new ConfigFishingStatOverlay();

    ConfigMainLobbyFishing() { }

    public boolean isRetexturedFishingRodsEnabled() {
        return retextured_fishing_rods;
    }

    public ConfigFishingStatOverlay getFishingStatOverlay() {
        return fishing_stat_overlay;
    }

}
