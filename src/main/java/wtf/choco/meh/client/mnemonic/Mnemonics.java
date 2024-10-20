package wtf.choco.meh.client.mnemonic;

import com.mojang.blaze3d.platform.InputConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import net.minecraft.resources.ResourceLocation;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.chat.GCMnemonicFeature;
import wtf.choco.meh.client.chat.GGMnemonicFeature;

/**
 * An object holding constants of all the default mnemonics used by MEH.
 */
public final class Mnemonics {

    private static final Map<ResourceLocation, Mnemonic> BY_ID = new HashMap<>();

    /**
     * "gg", which writes "gg" into the chat.
     *
     * @see GGMnemonicFeature
     */
    public static final Mnemonic GG = create("gg", InputConstants.KEY_G, InputConstants.KEY_G);

    /**
     * "gc", which writes "gc" into the chat.
     *
     * @see GCMnemonicFeature
     */
    public static final Mnemonic GC = create("gc", InputConstants.KEY_G, InputConstants.KEY_C);

    private Mnemonics() { }

    private static Mnemonic create(String id, int... keycodes) {
        ResourceLocation key = ResourceLocation.tryBuild(MEHClient.MOD_ID, id);
        Mnemonic mnemonic = Mnemonic.of(key, keycodes);
        BY_ID.put(key, mnemonic);
        return mnemonic;
    }

    /**
     * Get all known {@link Mnemonic Mnemonics} created by MEH as a Stream.
     *
     * @return the mnemonics as a stream
     */
    static Stream<Mnemonic> stream() {
        return BY_ID.values().stream();
    }

}
