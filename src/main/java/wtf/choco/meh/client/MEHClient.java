package wtf.choco.meh.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import net.fabricmc.api.ClientModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wtf.choco.meh.client.chat.ChatChannelsFeature;
import wtf.choco.meh.client.chat.ManualGGFeature;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.feature.FeatureManager;
import wtf.choco.meh.client.pksim.PKSim3Feature;
import wtf.choco.meh.client.server.HypixelServerState;

public final class MEHClient implements ClientModInitializer {

    public static final String MOD_ID = "meh";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static MEHClient instance;
    private static ConfigHolder<MEHConfig> config;

    private final HypixelServerState hypixelServerState = new HypixelServerState();
    private final FeatureManager featureManager = new FeatureManager(this);

    @Override
    public void onInitializeClient() {
        instance = this;

        MEHKeybinds.init();

        AutoConfig.register(MEHConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(MEHConfig.class);

        this.featureManager.addFeature(ChatChannelsFeature.class, ChatChannelsFeature::new);
        this.featureManager.addFeature(ManualGGFeature.class, ManualGGFeature::new);
        this.featureManager.addFeature(PKSim3Feature.class, PKSim3Feature::new);

        /*
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("testscoreboardlines")
                .executes(context -> {
                    return 1;
                })
            ));
        }
        */

        this.hypixelServerState.initialize();
        this.featureManager.initializeFeatures();
    }

    public HypixelServerState getHypixelServerState() {
        return hypixelServerState;
    }

    public <T extends Feature> T getFeature(Class<T> featureClass) {
        return featureManager.getFeature(featureClass);
    }

    public static MEHClient getInstance() {
        return instance;
    }

    public static MEHConfig getConfig() {
        return config.getConfig();
    }

}
