package wtf.choco.meh.client.mnemonic;

import com.mojang.blaze3d.platform.InputConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import net.minecraft.resources.ResourceLocation;

import wtf.choco.meh.client.MEHClient;

public final class Mnemonics {

    private static final Map<ResourceLocation, Mnemonic> BY_ID = new HashMap<>();

    public static final Mnemonic GG = create("gg", InputConstants.KEY_G, InputConstants.KEY_G);

    private Mnemonics() { }

    private static Mnemonic create(String id, int... keycodes) {
        ResourceLocation key = ResourceLocation.tryBuild(MEHClient.MOD_ID, id);
        Mnemonic mnemonic = Mnemonic.of(key, keycodes);
        BY_ID.put(key, mnemonic);
        return mnemonic;
    }

    public static Stream<Mnemonic> stream() {
        return BY_ID.values().stream();
    }

}
