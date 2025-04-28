package wtf.choco.meh.client.feature;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.keybind.MEHKeybinds;
import wtf.choco.meh.client.screen.CustomStatusScreen;

public final class CustomStatusMenuFeature extends Feature {

    public CustomStatusMenuFeature(MEHClient mod) {
        super(mod);
    }

    @Override
    protected boolean isFeatureEnabled(MEHConfig config) {
        return isInLobby();
    }

    @Override
    protected void registerListeners() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!isEnabled()) {
                if (!isInLobby()) {
                    client.player.displayClientMessage(Component.translatable("gui.meh.custom_status.not_in_lobby").withStyle(ChatFormatting.RED), false);
                }

                return;
            }

            if (client.screen == null && MEHKeybinds.OPEN_CUSTOM_STATUS_SCREEN.consumeClick()) {
                client.setScreen(new CustomStatusScreen(getMod().getStatusStorage()));
            }
        });
    }

    private boolean isInLobby() {
        return getMod().getHypixelServerState().getServerLocationProvider().isLobby();
    }

}
