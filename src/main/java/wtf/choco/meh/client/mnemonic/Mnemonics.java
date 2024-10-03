package wtf.choco.meh.client.mnemonic;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.MEHRegistries;

public final class Mnemonics {

    public static final Mnemonic GG = register("gg", InputConstants.KEY_G, InputConstants.KEY_G);

    private Mnemonics() { }

    public static void bootstrap() { }

    private static Mnemonic register(String id, int... keycodes) {
        ResourceKey<Mnemonic> key = ResourceKey.create(MEHRegistries.MNEMONIC.key(), ResourceLocation.tryBuild(MEHClient.MOD_ID, id));
        Mnemonic mnemonic = Mnemonic.of(Component.translatable("mnemonic.meh." + id), keycodes);
        Registry.register(MEHRegistries.MNEMONIC, key, mnemonic);
        return mnemonic;
    }

}
