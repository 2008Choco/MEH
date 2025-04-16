package wtf.choco.meh.client.feature;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.MEHConfig;

public final class QuietThunderFeature extends Feature {

    public QuietThunderFeature(MEHClient mod) {
        super(mod);
    }

    @Override
    protected boolean isFeatureEnabled(MEHConfig config) {
        return config.isQuietThunder();
    }

    @Override
    protected void registerListeners() { }

}
