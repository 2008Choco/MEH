package wtf.choco.meh.client.feature;

import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.chat.ChatChannelsFeature;
import wtf.choco.meh.client.chat.EmoteSelectorFeature;
import wtf.choco.meh.client.chat.GCMnemonicFeature;
import wtf.choco.meh.client.chat.GGMnemonicFeature;
import wtf.choco.meh.client.fishing.RetexturedFishingRodsFeature;
import wtf.choco.meh.client.game.housing.HousingAutoDisableFlightFeature;
import wtf.choco.meh.client.game.housing.HousingAutoNightVisionFeature;
import wtf.choco.meh.client.game.murdermystery.RetexturedKnivesFeature;
import wtf.choco.meh.client.game.skyblock.SkyBlockPrettyHudFeature;
import wtf.choco.meh.client.party.PartyManagerFeature;
import wtf.choco.meh.client.registry.MEHRegistries;

public final class Features {

    // Non-SkyBlock
    public static final ChatChannelsFeature CHAT_CHANNELS = register("chat_channels", ChatChannelsFeature::new);
    public static final CustomStatusMenuFeature CUSTOM_STATUS_MENU = register("custom_status_menu", CustomStatusMenuFeature::new);
    public static final EmoteSelectorFeature EMOTE_SELECTOR = register("emote_selector", EmoteSelectorFeature::new);
    public static final HousingAutoDisableFlightFeature HOUSING_AUTO_DISABLE_FLIGHT = register("housing_auto_disable_flight", HousingAutoDisableFlightFeature::new);
    public static final HousingAutoNightVisionFeature HOUSING_AUTO_NIGHT_VISION = register("housing_auto_night_vision", HousingAutoNightVisionFeature::new);
    public static final GCMnemonicFeature MNEMONIC_GC = register("mnemonic_gc", GCMnemonicFeature::new);
    public static final GGMnemonicFeature MNEMONIC_GG = register("mnemonic_gg", GGMnemonicFeature::new);
    public static final PartyManagerFeature PARTY_MANAGER = register("party_manager", PartyManagerFeature::new);
    public static final QuietThunderFeature QUIET_THUNDER = register("quiet_thunder", QuietThunderFeature::new);
    public static final RetexturedFishingRodsFeature RETEXTURED_FISHING_RODS = register("retextured_fishing_rods", RetexturedFishingRodsFeature::new);
    public static final RetexturedKnivesFeature RETEXTURED_KNIVES = register("retextured_knives", RetexturedKnivesFeature::new);

    // SkyBlock
    public static final SkyBlockPrettyHudFeature SKYBLOCK_PRETTY_HUD = register("skyblock/pretty_hud", SkyBlockPrettyHudFeature::new);

    private Features() { }

    public static void bootstrap() { }

    private static <F extends Feature> F register(String id, Function<MEHClient, F> featureConstructor) {
        MEHClient mod = MEHClient.getInstance();
        ResourceKey<Feature> key = ResourceKey.create(MEHRegistries.FEATURE.key(), MEHClient.key(id));
        F feature = featureConstructor.apply(mod);
        Registry.register(MEHRegistries.FEATURE, key, feature);
        return feature;
    }

}
