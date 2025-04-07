package wtf.choco.meh.client.feature;

import com.google.common.base.Preconditions;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.server.HypixelServerState;

/**
 * A base feature that can be added to the mod and individually enabled or disabled.
 */
public abstract class Feature {

    private boolean initialized = false;

    private final MEHClient mod;

    /**
     * @param mod the client mod instance
     */
    public Feature(MEHClient mod) {
        this.mod = mod;
    }

    /**
     * Get the {@link MEHClient} mod instance.
     *
     * @return the mod instance
     */
    protected final MEHClient getMod() {
        return mod;
    }

    /**
     * Check whether or not this feature is enabled and that the client is currently connected to Hypixel
     * (according to {@link HypixelServerState#isConnectedToHypixel()}).
     *
     * @return true if enabled, false if not enabled or not connected to Hypixel
     */
    public final boolean isEnabled() {
        return mod.getHypixelServerState().isConnectedToHypixel() && isFeatureEnabled(MEHClient.getConfig());
    }

    /**
     * A feature-specific check for the {@link #isEnabled()} check.
     *
     * @param config the MEH config instance
     *
     * @return true if this feature is enabled, false otherwise
     */
    protected abstract boolean isFeatureEnabled(MEHConfig config);

    /**
     * Register all event handlers required by this feature.
     */
    public final void initialize() {
        Preconditions.checkState(!initialized, "Already initialized. Cannot initialize again!");

        this.registerListeners();
        this.initialized = true;
    }

    /**
     * Register this feature's event handlers in this method.
     */
    protected abstract void registerListeners();

}
