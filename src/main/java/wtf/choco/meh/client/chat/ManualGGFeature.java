package wtf.choco.meh.client.chat;

import com.mojang.blaze3d.platform.InputConstants;

import java.util.concurrent.TimeUnit;

import net.minecraft.client.Minecraft;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.event.LWJGLEvents;
import wtf.choco.meh.client.feature.Feature;

public final class ManualGGFeature extends Feature {

    private static final long GG_COOLDOWN = TimeUnit.SECONDS.toMillis(10);

    private long lastReleasedG = 0L;
    private long lastSaidGG = 0L;

    public ManualGGFeature(MEHClient mod) {
        super(mod, MEHConfig::isManualGGEnabled);
    }

    @Override
    protected void registerListeners() {
        LWJGLEvents.KEY_STATE_CHANGE.register(this::onKeyStateChange);
    }

    @SuppressWarnings("unused")
    private boolean onKeyStateChange(int key, int scancode, int action, int mods) {
        if (!isEnabled()) {
            return true;
        }

        // Don't do anything if there's a screen open (including the chat screen)
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen != null) {
            return true;
        }

        // Listen for releases of the G key
        if (key != InputConstants.KEY_G || action != InputConstants.PRESS) {
            return true;
        }

        long now = System.currentTimeMillis();
        long difference = (now - lastReleasedG);

        // Don't spam GGs
        if (difference < 50) {
            return true;
        }

        // Two consecutive G releases should be within 1 second
        if (difference > 1000) {
            this.lastReleasedG = now;
            return true;
        }

        // Don't allow spamming of GG chat messages. Cooldown for a bit
        if ((now - lastSaidGG) < GG_COOLDOWN) {
            return true;
        }

        ChatChannelsFeature.dontSendToChannel = true; // "gg" should always be sent to the global chat channel!
        minecraft.player.connection.sendChat("gg");
        ChatChannelsFeature.dontSendToChannel = false;
        this.lastReleasedG = 0L;
        this.lastSaidGG = now;
        return true;
    }

}
