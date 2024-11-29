package wtf.choco.meh.client.config;

public final class ConfigEmoteSelector implements Enableable {

    private boolean enabled = true;

    ConfigEmoteSelector() { }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
