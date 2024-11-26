package wtf.choco.meh.client.fishing;

import java.util.Optional;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.item.Items;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.model.CustomModelOverrides;
import wtf.choco.meh.client.server.HypixelServerType;

public final class RetexturedFishingRodsFeature extends Feature implements ModelLoadingPlugin {

    public RetexturedFishingRodsFeature(MEHClient mod) {
        super(mod, MEHConfig::isRetexturedFishingRodsEnabled);
    }

    @Override
    protected void registerListeners() {
        ModelLoadingPlugin.register(this);
    }

    @Override
    public void initialize(Context context) {
        for (FishingRodType type : FishingRodType.values()) {
            context.addModels(type.getModelLocation(), type.getCastModelLocation());
            this.registerFishingRodModelOverride(type);
        }
    }

    private void registerFishingRodModelOverride(FishingRodType type) {
        CustomModelOverrides.register(Items.FISHING_ROD, new FishingRodModelOverride(this, type, false));
        CustomModelOverrides.register(Items.FISHING_ROD, new FishingRodModelOverride(this, type, true));
    }

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
