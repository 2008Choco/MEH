package wtf.choco.meh.client.chat;

import net.minecraft.client.Minecraft;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.ConfigMnemonics;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.event.MEHEvents;
import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.mnemonic.Mnemonic;

abstract class MnemonicChatFeature extends Feature {

    private final Mnemonic mnemonic;
    private final String message;

    public MnemonicChatFeature(MEHClient mod, Mnemonic mnemonic, String message) {
        super(mod);
        this.mnemonic = mnemonic;
        this.message = message;
    }

    @Override
    protected final boolean isFeatureEnabled(MEHConfig config) {
        return isMnemonicEnabled(config.getMnemonicsConfig());
    }

    protected abstract boolean isMnemonicEnabled(ConfigMnemonics config);

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
