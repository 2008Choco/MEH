package wtf.choco.meh.client.chat;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.mnemonic.Mnemonics;

public final class GGMnemonicFeature extends MnemonicChatFeature {

    public GGMnemonicFeature(MEHClient mod) {
        super(mod, MEHConfig::isGGMnemonicEnabled, Mnemonics.GG, "gg");
    }

}
