package wtf.choco.meh.client.fishing;

import java.util.Optional;
import java.util.function.Predicate;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.item.Items;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.model.DynamicModelOverrides;
import wtf.choco.meh.client.server.HypixelServerType;

public final class RetexturedFishingRodsFeature extends Feature {

    public RetexturedFishingRodsFeature(MEHClient mod) {
        super(mod, (Predicate<MEHConfig>) config -> config.getMainLobbyFishingConfig().isRetexturedFishingRodsEnabled());

        for (FishingRodType type : FishingRodType.values()) {
            DynamicModelOverrides.register(Items.FISHING_ROD, new FishingRodModelOverride(this, type));
        }
    }

    @Override
    protected void registerListeners() { }

    @Override
    public boolean isEnabled() {
        boolean enabled = super.isEnabled();
        if (!enabled) {
            return false;
        }

        // Always render in a development environment
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            return true;
        }

        // Otherwise, only render in the main lobby
        Optional<HypixelServerType> serverType = getMod().getHypixelServerState().getServerLocationProvider().getServerType();
        return serverType.isPresent() && serverType.get() == HypixelServerType.MAIN_LOBBY;
    }

}
