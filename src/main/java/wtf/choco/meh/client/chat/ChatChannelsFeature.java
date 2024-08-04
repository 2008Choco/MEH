package wtf.choco.meh.client.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;

import org.lwjgl.glfw.GLFW;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.MEHKeybinds;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.event.ChatScreenEvents;
import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.mixin.ChatScreenAccessor;

public class ChatChannelsFeature extends Feature {

    /*
     * From [ADMIN] 2008Choco:
     * To [MVP++] Player:
     * From UnrankedPlayer:
     */
    private static final Pattern PATTERN_MESSAGE = Pattern.compile("^(?:From|To)(?:\\s\\[.+\\])?\\s(?<name>\\w+):");

    private final ChannelSelector channelSelector = new ChannelSelector();
    private final MEHClient mod;

    public ChatChannelsFeature(MEHClient mod) {
        this.mod = mod;

        // Register configured known channels
        for (MEHConfig.KnownChannel channel : MEHClient.getConfig().getKnownChannels()) {
            ChatChannel chatChannel = new ChatChannel(channel.getId(), Component.literal(channel.getName()), channel.getColor(), channel.getCommandPrefix(), false);
            this.channelSelector.addChannel(chatChannel);
        }

        ClientSendMessageEvents.ALLOW_CHAT.register(this::onAllowOutgoingChat);
        ClientReceiveMessageEvents.GAME.register(this::onReceiveChatMessage);
        ClientTickEvents.END_CLIENT_TICK.register(this::onRenderClientTick);
        ChatScreenEvents.KEY_PRESS.register(this::onKeyInChatScreen);
        ChatScreenEvents.RENDER.register(this::onRenderChatScreen);
    }

    private boolean onAllowOutgoingChat(String message) {
        if (!mod.isConnectedToHypixel()) {
            return true;
        }

        ChatChannel selectedChannel = channelSelector.getSelectedChannel();
        if (!selectedChannel.hasCommandPrefix()) {
            return true;
        }

        Minecraft minecraft = Minecraft.getInstance();
        minecraft.player.connection.sendCommand(selectedChannel.getCommandPrefix() + " " + message);
        return false;
    }

    private void onReceiveChatMessage(Component message, boolean actionBar) {
        if (actionBar || !mod.isConnectedToHypixel()) {
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
        if (MEHClient.getConfig().isAutoSwitchOnNewMessage() && !(minecraft.screen instanceof ChatScreen)) {
            this.channelSelector.selectChannel(newChannelIndex);
        }
    }

    @SuppressWarnings("unused")
    private boolean onKeyInChatScreen(ChatScreen screen, int key, int keycode, int scancode) {
        // Don't allow channel switching if not connected to Hypixel
        if (!mod.isConnectedToHypixel()) {
            return true;
        }

        // Don't allow channel switching if writing a command
        if (isWritingCommand(screen)) {
            return true;
        }

        if (Screen.hasControlDown()) {
            if (key == GLFW.GLFW_KEY_TAB) {
                this.switchChannel(!Screen.hasShiftDown());
                return false;
            } else if (key == GLFW.GLFW_KEY_MINUS) {
                ChannelSelector channelSelector = getChannelSelector();
                ChatChannel selectedChannel = channelSelector.getSelectedChannel();
                if (!selectedChannel.isRemovable()) {
                    return false;
                }

                channelSelector.removeChannel(selectedChannel);
                return false;
            }
        }

        return true;
    }

    @SuppressWarnings("unused")
    private void onRenderChatScreen(ChatScreen screen, GuiGraphics graphics, int screenX, int screenY, float delta) {
        if (!mod.isConnectedToHypixel() || isWritingCommand(screen)) {
            return;
        }

        ChannelSelector channelSelector = getChannelSelector();
        if (channelSelector == null) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        ChatChannel channel = channelSelector.getSelectedChannel();
        Component displayName = channel.getDisplayName();
        int height = screen.height;
        int width = minecraft.font.width(displayName) + 7;
        int backgroundColor = (0x60 << 24) | channel.getColor();

        graphics.fill(2, height - 28, width - 2, height - 16, backgroundColor);
        graphics.drawString(minecraft.font, displayName, 4, height - 26, 0xFFFFFF);
    }

    private void onRenderClientTick(Minecraft client) {
        if (!mod.isConnectedToHypixel()) {
            return;
        }

        while (MEHKeybinds.KEY_SWITCH_CHANNEL.consumeClick()) {
            ChatChannel channel = switchChannel(!Screen.hasShiftDown());
            Component message = Component.translatable("meh.channel.switch", channel.getDisplayName(true));
            client.player.sendSystemMessage(message);
        }
    }

    private boolean isWritingCommand(ChatScreen screen) {
        String text = ((ChatScreenAccessor) screen).getInput().getValue();
        return !text.isEmpty() && text.charAt(0) == '/';
    }

    public ChannelSelector getChannelSelector() {
        return channelSelector;
    }

    public ChatChannel switchChannel(boolean next) {
        return next ? channelSelector.nextChannel() : channelSelector.previousChannel();
    }

}
