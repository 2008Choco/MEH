package wtf.choco.meh.client;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import wtf.choco.meh.client.mnemonic.Mnemonic;

public final class MEHRegistries {

    private static final ResourceKey<Registry<Mnemonic>> KEY_MNEMONIC = key("mnemonic");

    public static final Registry<Mnemonic> MNEMONIC = FabricRegistryBuilder.createSimple(KEY_MNEMONIC).buildAndRegister();

    private MEHRegistries() { }

    static void init() { }

    private static <T> ResourceKey<Registry<T>> key(String key) {
        return ResourceKey.createRegistryKey(ResourceLocation.tryBuild(MEHClient.MOD_ID, key));
    }

}
