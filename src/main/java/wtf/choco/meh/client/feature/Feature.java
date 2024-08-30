package wtf.choco.meh.client.feature;

import java.util.function.Predicate;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.MEHConfig;

public abstract class Feature {

    private final MEHClient mod;
    private final Predicate<MEHConfig> featureEnabled;

    public Feature(MEHClient mod, Predicate<MEHConfig> featureEnabled) {
        this.mod = mod;
        this.featureEnabled = featureEnabled;
    }

    protected final MEHClient getMod() {
        return mod;
    }

    public final boolean isEnabled() {
        return mod.isConnectedToHypixel() && featureEnabled.test(MEHClient.getConfig());
    }

}
