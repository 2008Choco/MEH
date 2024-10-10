package wtf.choco.meh.client.chat;

import java.util.OptionalLong;

import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;

import org.lwjgl.glfw.GLFW;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.event.ChatChannelEvents;
import wtf.choco.meh.client.event.HypixelServerEvents;
import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.keybind.MEHKeybinds;
import wtf.choco.meh.client.mixin.ChatScreenAccessor;
import wtf.choco.meh.client.util.SharedMixinValues;

public final class ChatChannelsFeature extends Feature {

    static boolean dontSendToChannel = false;

    private static final int DEFAULT_CHAT_BOX_MAX_LENGTH = 256;

    private final ChannelSelector channelSelector = new ChannelSelector();

    public ChatChannelsFeature(MEHClient mod) {
        super(mod, MEHConfig::areChatChannelsEnabled);

        // Register configured known channels
        for (MEHConfig.KnownChannel channel : MEHClient.getConfig().getKnownChannels()) {
            ChatChannel chatChannel = new ChatChannel(channel.getId(), Component.literal(channel.getName()), channel.getColor(), channel.getCommandPrefix(), false);
            this.channelSelector.addChannel(chatChannel);
        }
    }

    @Override
    protected void registerListeners() {
        ClientSendMessageEvents.ALLOW_CHAT.register(this::onAllowOutgoingChat);
        HypixelServerEvents.PRIVATE_MESSAGE_RECEIVED.register(this::onPrivateMessageReceived);
        HypixelServerEvents.PRIVATE_MESSAGE_SENT.register(this::onPrivateMessageSent);
        ChatChannelEvents.SWITCH.register((from, to, reason) -> {
            Minecraft client = Minecraft.getInstance();
            this.ensureChatEditBoxMaxLength(client.screen, to);
            return true;
        });

        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!(screen instanceof ChatScreen) || !isEnabled()) {
                return;
            }

            if (!MEHKeybinds.isAmecsLoaded()) {
                ScreenKeyboardEvents.allowKeyPress(screen).register(this::onKeyInChatScreen);
            }

            ScreenEvents.afterRender(screen).register(this::onRenderChatScreen);
        });

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!(screen instanceof ChatScreen chatScreen) || !isEnabled()) {
                return;
            }

            this.ensureChatEditBoxMaxLength(chatScreen, channelSelector.getSelectedChannel());
        });
    }

    private boolean onAllowOutgoingChat(String message) {
        if (!isEnabled() || dontSendToChannel) {
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

    @SuppressWarnings("unused") // rank, message, lastCommunicated
    private void onPrivateMessage(String username, String rank, String message, OptionalLong lastCommunicated, ChatChannelEvents.Switch.Reason switchReason) {
        if (!isEnabled()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (username.equals(minecraft.player.getName().getString()) || channelSelector.exists(username)) {
            return;
        }

        RandomSource random = minecraft.player.getRandom();
        int r = (int) (((random.nextFloat() / 2.0F) + 0.5F) * 0xFF);
        int g = (int) (((random.nextFloat() / 2.0F) + 0.5F) * 0xFF);
        int b = (int) (((random.nextFloat() / 2.0F) + 0.5F) * 0xFF);
        int randomColor = ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF) << 0);

        Component channelDisplayName = Component.literal(username);
        ChatChannel channel = new ChatChannel(username, channelDisplayName, randomColor, "msg " + username, true);

        if (!ChatChannelEvents.CREATE.invoker().onCreateChatChannel(channel)) {
            return;
        }

        int newChannelIndex = channelSelector.addChannel(channel);

        minecraft.player.sendSystemMessage(Component.translatable("meh.channel.new.msg", channel.getDisplayName(true)));

        // If the chat window isn't open, we'll automatically switch to the newly created channel
        if (MEHClient.getConfig().isAutoSwitchOnNewMessage() && !(minecraft.screen instanceof ChatScreen)) {
            if (!ChatChannelEvents.SWITCH.invoker().onSwitchChatChannel(channelSelector.getSelectedChannel(), channel, switchReason)) {
                return;
            }

            this.channelSelector.selectChannel(newChannelIndex);
        }
    }

    private void onPrivateMessageReceived(String username, String rank, String message, OptionalLong lastCommunicated) {
        this.onPrivateMessage(username, rank, message, lastCommunicated, ChatChannelEvents.Switch.Reason.AUTOMATIC_INCOMING);
    }

    private void onPrivateMessageSent(String username, String rank, String message, OptionalLong lastCommunicated) {
        this.onPrivateMessage(username, rank, message, lastCommunicated, ChatChannelEvents.Switch.Reason.AUTOMATIC_OUTGOING);
    }

    @SuppressWarnings("unused")
    private boolean onKeyInChatScreen(ChatScreen screen, int key, int keycode, int modifiers) {
        if (Screen.hasControlDown()) {
            if (key == MEHKeybinds.KEY_SWITCH_CHANNEL) {
                boolean next = (modifiers & GLFW.GLFW_MOD_SHIFT) == 0;
                return !(next ? keybindSwitchChannelNext() : keybindSwitchChannelPrevious());
            } else if (key == MEHKeybinds.KEY_DELETE_CHANNEL) {
                return !keybindDeleteChannel();
            }
        }

        return true;
    }

    private boolean onKeyInChatScreen(Screen screen, int key, int keycode, int scancode) { // Exists only as a way to target with method reference
        return onKeyInChatScreen((ChatScreen) screen, key, keycode, scancode);
    }

    public boolean keybindSwitchChannelNext() {
        if (shouldProcessKeybind()) {
            this.switchChannel(true);
            return true;
        }

        return false;
    }

    public boolean keybindSwitchChannelPrevious() {
        if (shouldProcessKeybind()) {
            this.switchChannel(false);
            return true;
        }

        return false;
    }

    public boolean keybindDeleteChannel() {
        if (!shouldProcessKeybind()) {
            return false;
        }

        ChannelSelector channelSelector = getChannelSelector();
        ChatChannel selectedChannel = channelSelector.getSelectedChannel();
        if (!selectedChannel.isRemovable()) {
            return true;
        }

        if (ChatChannelEvents.DELETE.invoker().onDeleteChatChannel(selectedChannel)) {
            channelSelector.removeChannel(selectedChannel);
            return true;
        }

        return false;
    }

    private boolean shouldProcessKeybind() {
        if (!isEnabled()) {
            return false;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (!(minecraft.screen instanceof ChatScreen) || SharedMixinValues.isWritingCommand((ChatScreen) minecraft.screen)) {
            return false;
        }

        return true;
    }

    @SuppressWarnings("unused")
    private void onRenderChatScreen(ChatScreen screen, GuiGraphics graphics, int screenX, int screenY, float delta) {
        if (!isEnabled() || SharedMixinValues.isWritingCommand(screen)) {
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

    private void onRenderChatScreen(Screen screen, GuiGraphics graphics, int screenX, int screenY, float delta) { // Exists only as a way to target with method reference
        this.onRenderChatScreen((ChatScreen) screen, graphics, screenX, screenY, delta);
    }

    private ChatChannel switchChannel(boolean next) {
        ChatChannel from = channelSelector.getSelectedChannel();
        ChatChannel to;
        ChatChannelEvents.Switch.Reason reason;
        if (next) {
            to = getChannelSelector().getNextChannel();
            reason = ChatChannelEvents.Switch.Reason.MANUAL_NEXT;
        } else {
            to = getChannelSelector().getPreviousChannel();
            reason = ChatChannelEvents.Switch.Reason.MANUAL_PREVIOUS;
        }

        if (!ChatChannelEvents.SWITCH.invoker().onSwitchChatChannel(from, to, reason)) {
            return null;
        }

        return next ? channelSelector.nextChannel() : channelSelector.previousChannel();
    }

    private void ensureChatEditBoxMaxLength(Screen screen, ChatChannel channel) {
        if (!(screen instanceof ChatScreen chatScreen)) {
            return;
        }

        int prefixLength = channel.getCommandPrefixLength();
        if (prefixLength > 0) {
            /*
             * Prefixes, if they exist, don't contain a slash or the extra space that we send...
             * but the length should account for this because that's what's sent to the server.
             */
            prefixLength += 2;
        }

        EditBox input = ((ChatScreenAccessor) chatScreen).getInput();
        int newMaxLength = (DEFAULT_CHAT_BOX_MAX_LENGTH - prefixLength);
        if (input.getValue().length() > newMaxLength) {
            input.setValue(input.getValue().substring(0, newMaxLength));
        }

        input.setMaxLength(newMaxLength);
    }

    public ChannelSelector getChannelSelector() {
        return channelSelector;
    }

}
