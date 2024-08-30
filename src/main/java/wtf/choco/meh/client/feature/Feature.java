package wtf.choco.meh.client.feature;

import com.google.common.base.Preconditions;

import java.util.function.Predicate;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.MEHConfig;

/**
 * A base feature that can be added to the mod and individually enabled or disabled.
 */
public abstract class Feature {

    private boolean registeredEventHandlers = false;

    private final MEHClient mod;
    private final Predicate<MEHConfig> featureEnabled;

    /**
     * @param mod the client mod instance
     * @param featureEnabled a predicate to check whether or not this feature is enabled. Generally this
     * should be a method reference to a MEHConfig getter
     */
    public Feature(MEHClient mod, Predicate<MEHConfig> featureEnabled) {
        this.mod = mod;
        this.featureEnabled = featureEnabled;
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
     * Check whether or not this feature enabled and that the client is currently connected to Hypixel
     * (according to {@link MEHClient#isConnectedToHypixel()}).
     *
     * @return true if enabled, false if not enabled or not connected to Hypixel
     */
    public final boolean isEnabled() {
        return mod.isConnectedToHypixel() && featureEnabled.test(MEHClient.getConfig());
    }

    /**
     * Register all event handlers required by this feature.
     */
    public final void registerEventHandlers() {
        Preconditions.checkState(!registeredEventHandlers, "Already registered event handlers. Cannot register again!");

        this.registerListeners();
        this.registeredEventHandlers = true;
    }

    /**
     * Register this feature's event handlers in this method.
     */
    protected abstract void registerListeners();

}
