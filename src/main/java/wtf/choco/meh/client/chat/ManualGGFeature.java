package wtf.choco.meh.client.chat;

import net.minecraft.client.Minecraft;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.event.MEHEvents;
import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.mnemonic.Mnemonic;
import wtf.choco.meh.client.mnemonic.Mnemonics;

public final class ManualGGFeature extends Feature {

    public ManualGGFeature(MEHClient mod) {
        super(mod, MEHConfig::isManualGGEnabled);
    }

    @Override
    protected void registerListeners() {
        MEHEvents.MNEMONIC_COMPLETION.register(this::onMnemonic);
    }

    private boolean onMnemonic(Mnemonic mnemonic) {
        if (mnemonic != Mnemonics.GG || !isEnabled()) {
            return true;
        }

        ChatChannelsFeature.dontSendToChannel = true; // "gg" should always be sent to the global chat channel!
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.player.connection.sendChat("gg");
        ChatChannelsFeature.dontSendToChannel = false;
        return true;
    }

}
