package wtf.choco.meh.client.feature;

import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.chat.ChatChannelsFeature;
import wtf.choco.meh.client.chat.EmoteSelectorFeature;
import wtf.choco.meh.client.chat.GCMnemonicFeature;
import wtf.choco.meh.client.chat.GGMnemonicFeature;
import wtf.choco.meh.client.fishing.RetexturedFishingRodsFeature;
import wtf.choco.meh.client.party.PartyManagerFeature;
import wtf.choco.meh.client.registry.MEHRegistries;

public final class Features {

    public static final AutoDisableHousingFlightFeature AUTO_DISABLE_HOUSING_FLIGHT = register("auto_disable_housing_flight", AutoDisableHousingFlightFeature::new);
    public static final ChatChannelsFeature CHAT_CHANNELS = register("chat_channels", ChatChannelsFeature::new);
    public static final EmoteSelectorFeature EMOTE_SELECTOR = register("emote_selector", EmoteSelectorFeature::new);
    public static final GCMnemonicFeature MNEMONIC_GC = register("mnemonic_gc", GCMnemonicFeature::new);
    public static final GGMnemonicFeature MNEMONIC_GG = register("mnemonic_gg", GGMnemonicFeature::new);
    public static final PartyManagerFeature PARTY_MANAGER = register("party_manager", PartyManagerFeature::new);
    public static final RetexturedFishingRodsFeature RETEXTURED_FISHING_RODS = register("retextured_fishing_rods", RetexturedFishingRodsFeature::new);

    private Features() { }

    public static void bootstrap() { }

    private static <F extends Feature> F register(String id, Function<MEHClient, F> featureConstructor) {
        MEHClient mod = MEHClient.getInstance();
        ResourceKey<Feature> key = ResourceKey.create(MEHRegistries.FEATURE.key(), ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, id));
        F feature = featureConstructor.apply(mod);
        Registry.register(MEHRegistries.FEATURE, key, feature);
        return feature;
    }

}
