package wtf.choco.meh.client.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler.EnumDisplayOption;

import wtf.choco.meh.client.screen.widgets.PartyListWidget;

public final class ConfigPartyManager {

    private boolean enabled = true;

    @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
    private PartyListWidget.Position party_list_position = PartyListWidget.Position.TOP_LEFT;

    ConfigPartyManager() { }

    public boolean isEnabled() {
        return enabled;
    }

    public PartyListWidget.Position getPartyListPosition() {
        return party_list_position;
    }

}
