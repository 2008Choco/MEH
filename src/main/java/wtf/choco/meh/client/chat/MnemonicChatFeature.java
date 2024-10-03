package wtf.choco.meh.client.chat;

import java.util.function.Predicate;

import net.minecraft.client.Minecraft;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.event.MEHEvents;
import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.mnemonic.Mnemonic;

abstract class MnemonicChatFeature extends Feature {

    private final Mnemonic mnemonic;
    private final String message;

    public MnemonicChatFeature(MEHClient mod, Predicate<MEHConfig> featureEnabled, Mnemonic mnemonic, String message) {
        super(mod, featureEnabled);
        this.mnemonic = mnemonic;
        this.message = message;
    }

    @Override
    protected void registerListeners() {
        MEHEvents.MNEMONIC_COMPLETION.register(this::onMnemonic);
    }

    private boolean onMnemonic(Mnemonic mnemonic) {
        if (mnemonic != this.mnemonic || !isEnabled()) {
            return true;
        }

        ChatChannelsFeature.dontSendToChannel = true; // "gg" should always be sent to the global chat channel!
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.player.connection.sendChat(message);
        ChatChannelsFeature.dontSendToChannel = false;
        return true;
    }

}
