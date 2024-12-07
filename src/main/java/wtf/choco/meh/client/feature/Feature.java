package wtf.choco.meh.client.feature;

import com.google.common.base.Preconditions;

import java.util.function.Function;
import java.util.function.Predicate;

import net.minecraft.resources.ResourceLocation;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.Enableable;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.server.HypixelServerState;

/**
 * A base feature that can be added to the mod and individually enabled or disabled.
 */
public abstract class Feature {

    public static final ResourceLocation AUTO_DISABLE_HOUSING_FLIGHT = ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, "auto_disable_housing_flight");
    public static final ResourceLocation CHAT_CHANNELS = ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, "chat_channels");
    public static final ResourceLocation EMOTE_SELECTOR = ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, "emote_selector");
    public static final ResourceLocation MNEMONIC_GC = ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, "mnemonic_gc");
    public static final ResourceLocation MNEMONIC_GG = ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, "mnemonic_gg");
    public static final ResourceLocation PARTY_MANAGER = ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, "party_manager");
    public static final ResourceLocation RETEXTURED_FISHING_RODS = ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, "retextured_fishing_rods");

    private boolean initialized = false;

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
     * @param mod the client mod instance
     * @param featureEnabled a predicate to check whether or not this feature is enabled. Generally this
     * should be a method reference to a MEHConfig getter
     */
    public Feature(MEHClient mod, Function<MEHConfig, Enableable> featureEnabled) {
        this(mod, (Predicate<MEHConfig>) config -> featureEnabled.apply(config).isEnabled());
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
     * (according to {@link HypixelServerState#isConnectedToHypixel()}).
     *
     * @return true if enabled, false if not enabled or not connected to Hypixel
     */
    public boolean isEnabled() {
        return mod.getHypixelServerState().isConnectedToHypixel() && featureEnabled.test(MEHClient.getConfig());
    }

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
