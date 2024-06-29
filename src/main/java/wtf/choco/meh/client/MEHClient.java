package wtf.choco.meh.client;

import com.mojang.blaze3d.platform.InputConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wtf.choco.meh.client.channel.ChannelSelector;
import wtf.choco.meh.client.channel.ChatChannel;
import wtf.choco.meh.client.config.MEHConfig;

public final class MEHClient implements ClientModInitializer {

    public static final String MOD_ID = "meh";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final KeyMapping KEY_SWITCH_CHANNEL = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.meh.switch_chat_channel",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_0,
            KeyMapping.CATEGORY_MULTIPLAYER
    ));

    // [*.]hypixel.net
    private static final Pattern PATTERN_HYPIXEL_IP = Pattern.compile("^(?:\\w+\\.)?hypixel\\.net$", Pattern.CASE_INSENSITIVE);
    /*
     * From [ADMIN] 2008Choco:
     * To [MVP++] Player:
     * From UnrankedPlayer:
     */
    private static final Pattern PATTERN_MESSAGE = Pattern.compile("^(?:From|To)(?:\\s\\[.+\\])?\\s(?<name>\\w+):");

    private final ChannelSelector channelSelector = new ChannelSelector();
    private boolean connectedToHypixel = false;

    private static MEHClient instance;
    private static ConfigHolder<MEHConfig> config;

    @Override
    public void onInitializeClient() {
        instance = this;

        AutoConfig.register(MEHConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(MEHConfig.class);

        // Register configured known channels
        for (MEHConfig.KnownChannel channel : getConfig().getKnownChannels()) {
            ChatChannel chatChannel = new ChatChannel(channel.getId(), Component.literal(channel.getName()), channel.getColor(), channel.getCommandPrefix(), false);
            this.channelSelector.addChannel(chatChannel);
        }

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> connectedToHypixel = false);
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            ServerData server = client.getCurrentServer();
            this.connectedToHypixel = (server != null) && PATTERN_HYPIXEL_IP.matcher(server.ip).matches();
        });

        ClientSendMessageEvents.ALLOW_CHAT.register(message -> {
            if (!isConnectedToHypixel()) {
                return true;
            }

            ChatChannel selectedChannel = channelSelector.getSelectedChannel();
            if (!selectedChannel.hasCommandPrefix()) {
                return true;
            }

            Minecraft minecraft = Minecraft.getInstance();
            minecraft.player.connection.sendCommand(selectedChannel.getCommandPrefix() + " " + message);
            return false;
        });

        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            if (overlay || !isConnectedToHypixel()) {
                return;
            }

            String stringMessage = ChatFormatting.stripFormatting(message.getString());
            Matcher matcher = PATTERN_MESSAGE.matcher(stringMessage);
            if (!matcher.find()) {
                return;
            }

            Minecraft minecraft = Minecraft.getInstance();
            String playerName = matcher.group("name");
            if (playerName.equals(minecraft.player.getName().getString())) {
                return;
            }

            if (channelSelector.exists(playerName)) {
                return;
            }

            RandomSource random = minecraft.player.getRandom();
            int r = (int) (((random.nextFloat() / 2.0F) + 0.5F) * 0xFF);
            int g = (int) (((random.nextFloat() / 2.0F) + 0.5F) * 0xFF);
            int b = (int) (((random.nextFloat() / 2.0F) + 0.5F) * 0xFF);
            int randomColor = ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF) << 0);

            Component channelDisplayName = Component.literal(playerName);
            ChatChannel channel = new ChatChannel(playerName, channelDisplayName, randomColor, "msg " + playerName, true);
            int newChannelIndex = channelSelector.addChannel(channel);

            minecraft.player.sendSystemMessage(Component.translatable("meh.channel.new.msg", channel.getDisplayName(true)));

            // If the chat window isn't open, we'll automatically switch to the newly created channel
            if (!(minecraft.screen instanceof ChatScreen)) {
                this.channelSelector.selectChannel(newChannelIndex);
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!isConnectedToHypixel()) {
                return;
            }

            while (KEY_SWITCH_CHANNEL.consumeClick()) {
                ChatChannel channel = switchChannel(!Screen.hasShiftDown());
                Component message = Component.translatable("meh.channel.switch", channel.getDisplayName(true));
                client.player.sendSystemMessage(message);
            }
        });
    }

    public boolean isConnectedToHypixel() {
        return connectedToHypixel || FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public ChannelSelector getChannelSelector() {
        return channelSelector;
    }

    public ChatChannel switchChannel(boolean next) {
        return next ? channelSelector.nextChannel() : channelSelector.previousChannel();
    }

    public static MEHClient getInstance() {
        return instance;
    }

    public static MEHConfig getConfig() {
        return config.getConfig();
    }

}
