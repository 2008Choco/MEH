package wtf.choco.meh.client.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler.EnumDisplayOption;

public class ConfigFishingStatOverlay {

    @ConfigEntry.Gui.Tooltip
    private boolean when_fishing = true;

    @ConfigEntry.Gui.Tooltip
    private boolean when_holding_rod = true;

    @ConfigEntry.Gui.Tooltip(count = 3)
    @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
    private BooleanOperator condition_operator = BooleanOperator.OR;

    ConfigFishingStatOverlay() { }

    public boolean isEnabledWhenFishing() {
        return when_fishing;
    }

    public boolean isEnabledWhenHoldingRod() {
        return when_holding_rod;
    }

    public BooleanOperator getOperator() {
        return condition_operator;
    }

}
