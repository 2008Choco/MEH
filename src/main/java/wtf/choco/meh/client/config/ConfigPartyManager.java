package wtf.choco.meh.client.config;

import wtf.choco.meh.client.screen.widgets.PartyListWidget;

public final class ConfigPartyManager implements Enableable {

    private boolean enabled = true;

    private PartyListWidget.Position party_list_position = PartyListWidget.Position.TOP_LEFT;

    ConfigPartyManager() { }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public PartyListWidget.Position getPartyListPosition() {
        return party_list_position;
    }

}
