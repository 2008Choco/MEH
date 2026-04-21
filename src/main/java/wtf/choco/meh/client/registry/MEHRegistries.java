package wtf.choco.meh.client.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.chat.ChatEmote;
import wtf.choco.meh.client.feature.Feature;

public final class MEHRegistries {

    public static final ResourceKey<Registry<Feature>> KEY_FEATURE = ResourceKey.createRegistryKey(MEHClient.key("feature"));
    public static final ResourceKey<Registry<ChatEmote>> KEY_CHAT_EMOTES = ResourceKey.createRegistryKey(MEHClient.key("chat_emotes"));

    public static final MappedRegistry<Feature> FEATURE = FabricRegistryBuilder.create(KEY_FEATURE).buildAndRegister();
    public static final MappedRegistry<ChatEmote> CHAT_EMOTES = FabricRegistryBuilder.create(KEY_CHAT_EMOTES).buildAndRegister();

    private MEHRegistries() { }

}
