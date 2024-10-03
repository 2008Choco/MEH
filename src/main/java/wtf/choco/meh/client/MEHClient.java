package wtf.choco.meh.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wtf.choco.meh.client.chat.ChatChannelsFeature;
import wtf.choco.meh.client.chat.EmoteSelectorFeature;
import wtf.choco.meh.client.chat.ManualGGFeature;
import wtf.choco.meh.client.command.ClientTestCommand;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.event.impl.ChatListener;
import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.feature.FeatureManager;
import wtf.choco.meh.client.fishing.RetexturedFishingRodsFeature;
import wtf.choco.meh.client.mnemonic.MnemonicHandler;
import wtf.choco.meh.client.server.HypixelServerState;

public final class MEHClient implements ClientModInitializer {

    public static final String MOD_ID = "meh";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static MEHClient instance;
    private static ConfigHolder<MEHConfig> config;

    private MnemonicHandler mnemonicHandler;

    private final HypixelServerState hypixelServerState = new HypixelServerState();
    private final FeatureManager featureManager = new FeatureManager(this);

    @Override
    public void onInitializeClient() {
        instance = this;

        MEHKeybinds.init();

        AutoConfig.register(MEHConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(MEHConfig.class);

        this.mnemonicHandler = new MnemonicHandler();

        ChatListener.initialize();

        this.featureManager.addFeature(ChatChannelsFeature.class, ChatChannelsFeature::new);
        this.featureManager.addFeature(EmoteSelectorFeature.class, EmoteSelectorFeature::new);
        this.featureManager.addFeature(ManualGGFeature.class, ManualGGFeature::new);
        this.featureManager.addFeature(RetexturedFishingRodsFeature.class, RetexturedFishingRodsFeature::new);

        this.hypixelServerState.initialize();
        this.featureManager.initializeFeatures();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> ClientTestCommand.register(dispatcher));
    }

    public MnemonicHandler getMnemonicHandler() {
        return mnemonicHandler;
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
