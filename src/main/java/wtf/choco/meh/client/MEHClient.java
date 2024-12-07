package wtf.choco.meh.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.resources.ResourceLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wtf.choco.meh.client.command.ClientTestCommand;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.event.impl.ChatListener;
import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.feature.Features;
import wtf.choco.meh.client.keybind.MEHKeybinds;
import wtf.choco.meh.client.mnemonic.MnemonicHandler;
import wtf.choco.meh.client.model.property.ItemModelPropertyHypixelServerType;
import wtf.choco.meh.client.model.property.ItemModelPropertyMEHFeatureEnabled;
import wtf.choco.meh.client.registry.MEHRegistries;
import wtf.choco.meh.client.server.HypixelServerState;

public final class MEHClient implements ClientModInitializer {

    public static final String MOD_ID = "meh";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static MEHClient instance;
    private static ConfigHolder<MEHConfig> config;

    private MnemonicHandler mnemonicHandler;

    private final HypixelServerState hypixelServerState = new HypixelServerState();

    @SuppressWarnings("deprecation") // ItemModelProperty implementations
    @Override
    public void onInitializeClient() {
        instance = this;

        AutoConfig.register(MEHConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(MEHConfig.class);

        this.mnemonicHandler = new MnemonicHandler();

        ChatListener.initialize();

        Features.bootstrap();
        MEHRegistries.FEATURE.forEach(Feature::initialize);

        this.hypixelServerState.initialize();

        MEHKeybinds.init();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> ClientTestCommand.register(dispatcher));

        ConditionalItemModelProperties.ID_MAPPER.put(ResourceLocation.fromNamespaceAndPath(MOD_ID, "hypixel_server_type"), ItemModelPropertyHypixelServerType.MAP_CODEC);
        ConditionalItemModelProperties.ID_MAPPER.put(ResourceLocation.fromNamespaceAndPath(MOD_ID, "meh_feature_enabled"), ItemModelPropertyMEHFeatureEnabled.MAP_CODEC);
    }

    public MnemonicHandler getMnemonicHandler() {
        return mnemonicHandler;
    }

    public HypixelServerState getHypixelServerState() {
        return hypixelServerState;
    }

    public static MEHClient getInstance() {
        return instance;
    }

    public static MEHConfig getConfig() {
        return config.getConfig();
    }

}
