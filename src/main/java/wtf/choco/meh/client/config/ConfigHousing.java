package wtf.choco.meh.client.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public final class ConfigHousing {

    @ConfigEntry.Gui.Tooltip
    private boolean auto_disable_flight = false;

    @ConfigEntry.Gui.Tooltip(count = 3)
    private boolean auto_night_vision = false;

    ConfigHousing() { }

    public boolean isAutoDisableFlight() {
        return auto_disable_flight;
    }

    public boolean isAutoNightVision() {
        return auto_night_vision;
    }

}
