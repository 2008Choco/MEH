package wtf.choco.meh.client.fishing;

import java.util.Optional;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Items;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.ConfigFishingStatOverlay;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.gui.FishingStatsHudElement;
import wtf.choco.meh.client.server.HypixelServerType;

public final class FishingStatOverlayFeature extends Feature {

    private final FishingStatsHudElement fishingStatsElement = new FishingStatsHudElement(this);

    public FishingStatOverlayFeature(MEHClient mod) {
        super(mod);
    }

    @Override
    protected boolean isFeatureEnabled(MEHConfig config) {
        // Always render in a development environment
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            return true;
        }

        Optional<HypixelServerType> serverType = getMod().getHypixelServerState().getServerLocationProvider().getServerType();
        return serverType.isPresent() && serverType.get() == HypixelServerType.MAIN_LOBBY;
    }

    public boolean isDisplayConditionMet() {
        ConfigFishingStatOverlay overlayConfig = MEHClient.getConfig().getMainLobbyFishingConfig().getFishingStatOverlay();

        boolean fishing = overlayConfig.isEnabledWhenFishing() == MEHClient.getInstance().getHypixelServerState().getFishingState().isFishing();
        boolean holdingRod = overlayConfig.isEnabledWhenHoldingRod() == isHoldingFishingRod();

        return overlayConfig.getOperator().apply(fishing, holdingRod);
    }

    private boolean isHoldingFishingRod() {
        LocalPlayer player = Minecraft.getInstance().player;
        return player != null && player.getInventory().getSelectedItem().getItem() == Items.FISHING_ROD;
    }

    @Override
    protected void registerListeners() {
        HudElementRegistry.addLast(FishingStatsHudElement.ID, fishingStatsElement);
    }

}
