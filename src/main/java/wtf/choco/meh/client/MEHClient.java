package wtf.choco.meh.client;

import java.nio.file.Path;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
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
import wtf.choco.meh.client.screen.CustomStatusScreen;
import wtf.choco.meh.client.server.CustomStatusStorage;
import wtf.choco.meh.client.server.HypixelServerState;

public final class MEHClient implements ClientModInitializer {

    public static final String MOD_ID = "meh";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final Path STATUS_STORAGE_PATH = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + "_custom_statuses.txt");

    private static MEHClient instance;
    private static ConfigHolder<MEHConfig> config;

    private final MnemonicHandler mnemonicHandler = new MnemonicHandler();
    private final HypixelServerState hypixelServerState = new HypixelServerState();
    private final CustomStatusStorage statusStorage = new CustomStatusStorage(STATUS_STORAGE_PATH);

    @SuppressWarnings("deprecation") // ItemModelProperty implementations
    @Override
    public void onInitializeClient() {
        instance = this;

        AutoConfig.register(MEHConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(MEHConfig.class);

        ChatListener.initialize();

        Features.bootstrap();
        MEHRegistries.FEATURE.forEach(Feature::initialize);

        this.mnemonicHandler.initialize();
        this.hypixelServerState.initialize();

        MEHKeybinds.init();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> ClientTestCommand.register(dispatcher));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.screen == null && MEHKeybinds.OPEN_CUSTOM_STATUS_SCREEN.isDown()) {
                client.setScreen(new CustomStatusScreen(statusStorage));
            }
        });

        this.statusStorage.readFromFile();

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
