package wtf.choco.meh.client.game.murdermystery;

import java.util.Optional;

import net.fabricmc.loader.api.FabricLoader;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.model.DynamicModelOverrides;
import wtf.choco.meh.client.server.HypixelServerType;

public final class RetexturedKnivesFeature extends Feature {

    public RetexturedKnivesFeature(MEHClient mod) {
        super(mod);

        for (KnifeType type : KnifeType.values()) {
            if (!type.shouldRetexture()) {
                continue;
            }

            DynamicModelOverrides.register(type.getItem(), new KnifeModelOverride(type));
        }
    }

    @Override
    public boolean isFeatureEnabled(MEHConfig config) {
        // Always render in a development environment
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            return true;
        }

        if (!config.getMurderMysteryConfig().isRetexturedMurdererKnives()) {
            return false;
        }

        // Otherwise, only render in Murder Mystery
        Optional<HypixelServerType> serverType = getMod().getHypixelServerState().getServerLocationProvider().getServerType();
        return serverType.isPresent() && serverType.get() == HypixelServerType.MURDER_MYSTERY;
    }

    @Override
    protected void registerListeners() { }

}
