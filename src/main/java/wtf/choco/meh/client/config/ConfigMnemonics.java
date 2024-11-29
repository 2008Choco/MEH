package wtf.choco.meh.client.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public final class ConfigMnemonics {

    @ConfigEntry.Gui.Tooltip
    private boolean gg = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    private boolean gc = true;

    ConfigMnemonics() { }

    public boolean isGGEnabled() {
        return gg;
    }

    public boolean isGCEnabled() {
        return gc;
    }

}
