package wtf.choco.meh.client.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public final class ConfigSkyBlock {

    @ConfigEntry.Gui.Tooltip
    private boolean pretty_hud = true;

    ConfigSkyBlock() { }

    public boolean isPrettyHud() {
        return pretty_hud;
    }

}
