package wtf.choco.meh.client.feature;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.keybind.MEHKeyMappings;

public final class BarrierRendererFeature extends Feature {

    private boolean enabled = false;

    public BarrierRendererFeature(MEHClient mod) {
        super(mod);
    }

    @Override
    protected boolean isFeatureEnabled(MEHConfig config) {
        return enabled;
    }

    @Override
    protected void registerListeners() {
        // TODO: Maybe render an icon or text somewhere to let the client know that barriers are being rendered
        // Not that they wouldn't be able to tell... they'll see barrier blocks... but it's a nice QOL thing!
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (MEHKeyMappings.TOGGLE_BARRIER_BLOCK_RENDERING.consumeClick()) {
                this.enabled = !enabled;
                client.player.sendSystemMessage(Component.translatable(
                    enabled ? "meh.barrier_rendering.toggle.on" : "meh.barrier_rendering.toggle.off",
                    MEHKeyMappings.TOGGLE_BARRIER_BLOCK_RENDERING.getTranslatedKeyMessage()
                ).withStyle(enabled ? ChatFormatting.GREEN : ChatFormatting.RED));
            }
        });
    }

}
