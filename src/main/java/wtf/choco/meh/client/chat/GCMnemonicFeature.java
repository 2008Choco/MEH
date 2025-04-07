package wtf.choco.meh.client.chat;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.ConfigMnemonics;
import wtf.choco.meh.client.mnemonic.Mnemonics;

public final class GCMnemonicFeature extends MnemonicChatFeature {

    public GCMnemonicFeature(MEHClient mod) {
        super(mod, Mnemonics.GC, "gc");
    }

    @Override
    protected boolean isMnemonicEnabled(ConfigMnemonics config) {
        return config.isGCEnabled();
    }

}
