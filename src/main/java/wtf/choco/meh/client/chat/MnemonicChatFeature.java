package wtf.choco.meh.client.chat;

import java.util.function.Predicate;

import net.minecraft.client.Minecraft;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.ConfigMnemonics;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.event.MEHEvents;
import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.mnemonic.Mnemonic;
import wtf.choco.meh.client.mnemonic.Mnemonics;

public final class MnemonicChatFeature extends Feature {

    private final Mnemonic mnemonic;
    private final String message;
    private final Predicate<ConfigMnemonics> enabled;

    private MnemonicChatFeature(MEHClient mod, Mnemonic mnemonic, String message, Predicate<ConfigMnemonics> enabled) {
        super(mod);
        this.mnemonic = mnemonic;
        this.message = message;
        this.enabled = enabled;
    }

    @Override
    protected final boolean isFeatureEnabled(MEHConfig config) {
        return enabled.test(config.getMnemonicsConfig());
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

    public static MnemonicChatFeature gg(MEHClient mod) {
        return new MnemonicChatFeature(mod, Mnemonics.GG, "gg", ConfigMnemonics::isGGEnabled);
    }

    public static MnemonicChatFeature gc(MEHClient mod) {
        return new MnemonicChatFeature(mod, Mnemonics.GC, "gc", ConfigMnemonics::isGCEnabled);
    }

}
