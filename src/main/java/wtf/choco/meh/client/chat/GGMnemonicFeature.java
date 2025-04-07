package wtf.choco.meh.client.chat;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.ConfigMnemonics;
import wtf.choco.meh.client.mnemonic.Mnemonics;

public final class GGMnemonicFeature extends MnemonicChatFeature {

    public GGMnemonicFeature(MEHClient mod) {
        super(mod, Mnemonics.GG, "gg");
    }

    @Override
    protected boolean isMnemonicEnabled(ConfigMnemonics config) {
        return config.isGGEnabled();
    }

}
