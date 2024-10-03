package wtf.choco.meh.client.mnemonic;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.Nullable;

final class MnemonicStack {

    private final List<Mnemonic> possibleMnemonics;
    private final IntList pushedKeys;

    MnemonicStack(Collection<Mnemonic> possibleMnemonics) {
        this.possibleMnemonics = new ArrayList<>(possibleMnemonics);

        int maxPushableKeys = 0;
        for (Mnemonic mnemonic : possibleMnemonics) {
            maxPushableKeys = Math.max(maxPushableKeys, mnemonic.size());
        }

        this.pushedKeys = new IntArrayList(maxPushableKeys);
    }

    /**
     * @param keycode
     * @return true if the keycode push resulted in one or more possible mnemonics, or false if
     * no more mnemonics are possible
     */
    boolean pushKey(int keycode) {
        if (possibleMnemonics.isEmpty()) {
            return false;
        }

        int index = pushedKeys.size();
        this.possibleMnemonics.removeIf(mnemonic -> index >= mnemonic.size() || mnemonic.keycodeAt(index) != keycode);

        if (possibleMnemonics.isEmpty()) {
            return false;
        }

        this.pushedKeys.add(keycode);
        return true;
    }

    @Nullable
    Mnemonic get() {
        if (possibleMnemonics.size() != 1) {
            return null;
        }

        Mnemonic mnemonic = possibleMnemonics.get(0);
        return mnemonic.matchesExactly(pushedKeys) ? mnemonic : null;
    }

}
