package wtf.choco.meh.client.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.feature.Feature;

public final class MEHRegistries {

    public static final ResourceKey<Registry<Feature>> KEY_FEATURE = ResourceKey.createRegistryKey(MEHClient.key("feature"));

    public static final MappedRegistry<Feature> FEATURE = FabricRegistryBuilder.createSimple(KEY_FEATURE).buildAndRegister();

    private MEHRegistries() { }

}
