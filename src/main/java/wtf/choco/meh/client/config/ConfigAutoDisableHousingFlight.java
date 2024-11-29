package wtf.choco.meh.client.config;

public final class ConfigAutoDisableHousingFlight implements Enableable {

    private boolean enabled = false;

    ConfigAutoDisableHousingFlight() { }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
