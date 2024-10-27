package wtf.choco.meh.client.game.murdermystery;

import java.util.Optional;
import java.util.function.Predicate;

import net.fabricmc.loader.api.FabricLoader;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.model.DynamicModelOverrides;
import wtf.choco.meh.client.server.HypixelServerType;

public final class RetexturedKnivesFeature extends Feature {

    public RetexturedKnivesFeature(MEHClient mod) {
        super(mod, (Predicate<MEHConfig>) config -> config.getMurderMysteryConfig().isRetexturedMurdererKnives());

        for (KnifeType type : KnifeType.values()) {
            if (!type.shouldRetexture()) {
                continue;
            }

            DynamicModelOverrides.register(type.getItem(), new KnifeModelOverride(type));
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

        // Otherwise, only render in Murder Mystery
        Optional<HypixelServerType> serverType = getMod().getHypixelServerState().getServerLocationProvider().getServerType();
        return serverType.isPresent() && serverType.get() == HypixelServerType.MURDER_MYSTERY;
    }

}
