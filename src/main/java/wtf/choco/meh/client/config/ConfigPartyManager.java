package wtf.choco.meh.client.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler.EnumDisplayOption;

import wtf.choco.meh.client.screen.widgets.PartyListHudElement;

public final class ConfigPartyManager {

    private boolean enabled = true;

    @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
    private PartyListHudElement.Position party_list_position = PartyListHudElement.Position.TOP_LEFT;

    ConfigPartyManager() { }

    public boolean isEnabled() {
        return enabled;
    }

    public PartyListHudElement.Position getPartyListPosition() {
        return party_list_position;
    }

}
