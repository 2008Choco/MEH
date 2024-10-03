package wtf.choco.meh.client.chat;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.mnemonic.Mnemonics;

public final class ManualGGFeature extends AbstractMnemonicChatFeature {

    public ManualGGFeature(MEHClient mod) {
        super(mod, MEHConfig::isManualGGEnabled, Mnemonics.GG, "gg");
    }

}
