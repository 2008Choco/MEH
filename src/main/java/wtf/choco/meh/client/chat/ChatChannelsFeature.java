package wtf.choco.meh.client.chat;

import com.mojang.blaze3d.platform.InputConstants;

import java.util.Objects;
import java.util.OptionalLong;

import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import org.lwjgl.glfw.GLFW;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.chat.extractor.UserData;
import wtf.choco.meh.client.chat.filter.ChatMessageFilter;
import wtf.choco.meh.client.config.KnownChannel;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.event.ChatChannelEvents;
import wtf.choco.meh.client.event.HypixelServerEvents;
import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.mixin.ChatScreenAccessor;
import wtf.choco.meh.client.util.SharedMixinValues;

public final class ChatChannelsFeature extends Feature {

    static boolean dontSendToChannel = false;

    private static final ResourceLocation TEXTURE_FOCUS = ResourceLocation.tryBuild(MEHClient.MOD_ID, "textures/gui/screen/focus.png");

    private static final int DEFAULT_CHAT_BOX_MAX_LENGTH = 256;

    private static final int CHANNEL_TAG_ALPHA = (0x60 << 24);
    private static final int CHANNEL_TAG_X = 2;
    private static final int CHANNEL_TAG_Y_OFFSET = 28;
    private static final int CHANNEL_NAME_X = CHANNEL_TAG_X + 2;
    private static final int CHANNEL_NAME_Y_OFFSET = CHANNEL_TAG_Y_OFFSET - 2;
    private static final int FOCUS_ICON_PADDING = 6;
    private static final int FOCUS_ICON_SIZE = 12;

    private boolean focused = false;

    private final ChannelSelector channelSelector = new ChannelSelector();

    public ChatChannelsFeature(MEHClient mod) {
        super(mod);

        // Register configured known channels
        for (KnownChannel channel : MEHClient.getConfig().getChatChannelsConfig().getKnownChannels()) {
            ChatChannel chatChannel = new ChatChannel(channel.getId(), Component.literal(channel.getName()), channel.getColor(), channel.getCommandPrefix(), ChatChannelType.BUILT_IN, channel.getFocusFilter().toChatMessageFilter());
            this.channelSelector.addChannel(chatChannel);
        }
    }

    @Override
    protected boolean isFeatureEnabled(MEHConfig config) {
        return config.getChatChannelsConfig().isEnabled();
    }

    @Override
    protected void registerListeners() {
        ClientSendMessageEvents.ALLOW_CHAT.register(this::onAllowOutgoingChat);
        HypixelServerEvents.PRIVATE_MESSAGE_RECEIVED.register(this::onPrivateMessageReceived);
        HypixelServerEvents.PRIVATE_MESSAGE_SENT.register(this::onPrivateMessageSent);
        ChatChannelEvents.SWITCH.register(this::onChatChannelSwitch);

        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!(screen instanceof ChatScreen) || !isEnabled()) {
                return;
            }

            ScreenKeyboardEvents.allowKeyPress(screen).register(this::onKeyInChatScreen);
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
    private void onPrivateMessage(UserData user, String message, OptionalLong lastCommunicated, ChatChannelEvents.Switch.Reason switchReason) {
        if (!isEnabled()) {
            return;
        }

        String username = user.username();
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
        ChatChannel channel = new ChatChannel(username, channelDisplayName, randomColor, "w " + username, ChatChannelType.PRIVATE_MESSAGE, ChatMessageFilter.privateMessage(username));

        if (!ChatChannelEvents.CREATE.invoker().onCreateChatChannel(channel)) {
            return;
        }

        int newChannelIndex = channelSelector.addChannel(channel);

        minecraft.player.displayClientMessage(Component.translatable("meh.channel.new.msg", channel.getDisplayName(true)), false);

        // If the chat window isn't open, we'll automatically switch to the newly created channel
        if (MEHClient.getConfig().getChatChannelsConfig().isAutoSwitchOnNewMessage() && !(minecraft.screen instanceof ChatScreen)) {
            if (!ChatChannelEvents.SWITCH.invoker().onSwitchChatChannel(channelSelector.getSelectedChannel(), channel, switchReason)) {
                return;
            }

            this.channelSelector.selectChannel(newChannelIndex);
        }
    }

    private void onPrivateMessageReceived(UserData user, String message, OptionalLong lastCommunicated) {
        this.onPrivateMessage(user, message, lastCommunicated, ChatChannelEvents.Switch.Reason.AUTOMATIC_INCOMING);
    }

    private void onPrivateMessageSent(UserData user, String message, OptionalLong lastCommunicated) {
        this.onPrivateMessage(user, message, lastCommunicated, ChatChannelEvents.Switch.Reason.AUTOMATIC_OUTGOING);
    }

    @SuppressWarnings("unused")
    private boolean onChatChannelSwitch(ChatChannel from, ChatChannel to, ChatChannelEvents.Switch.Reason reason) {
        Minecraft client = Minecraft.getInstance();
        this.ensureChatEditBoxMaxLength(client.screen, to);

        ChatComponent chat = client.gui.getChat();
        if (focused && !Objects.equals(from.getMessageFilter(), to.getMessageFilter())) {
            chat.setChatMessageFilter(to.getMessageFilter());
        } else if (!focused && chat.hasChatMessageFilter()) {
            chat.setChatMessageFilter(null);
        }

        return true;
    }

    @SuppressWarnings("unused")
    private boolean onKeyInChatScreen(ChatScreen screen, int key, int keycode, int modifiers) {
        if (Screen.hasControlDown()) {
            if (key == InputConstants.KEY_TAB) {
                boolean next = (modifiers & GLFW.GLFW_MOD_SHIFT) == 0;
                return !handleSwitchChannelKeybind(next);
            } else if (key == InputConstants.KEY_MINUS) {
                return !handleDeleteChannelKeybind();
            } else if (key == InputConstants.KEY_F) {
                return !handleToggleFocusModeKeybind();
            }
        }

        return true;
    }

    private boolean onKeyInChatScreen(Screen screen, int key, int keycode, int scancode) { // Exists only as a way to target with method reference
        return onKeyInChatScreen((ChatScreen) screen, key, keycode, scancode);
    }

    private boolean handleSwitchChannelKeybind(boolean next) {
        if (shouldProcessKeybind()) {
            this.switchChannel(next);
            return true;
        }

        return false;
    }

    private boolean handleDeleteChannelKeybind() {
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

    private boolean handleToggleFocusModeKeybind() {
        if (!shouldProcessKeybind()) {
            return false;
        }

        this.focused = !focused;

        Minecraft minecraft = Minecraft.getInstance();
        ChatChannel selectedChannel = getChannelSelector().getSelectedChannel();
        minecraft.gui.getChat().setChatMessageFilter(focused ? selectedChannel.getMessageFilter() : null);
        return true;
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
    private void onRenderChatScreen(ChatScreen screen, GuiGraphics graphics, int mouseX, int mouseY, float delta) {
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

        final int channelTagY = screen.height - CHANNEL_TAG_Y_OFFSET;
        final int channelTagHeight = minecraft.font.lineHeight + 3;

        final int textWidth = minecraft.font.width(displayName);
        final int channelNameY = screen.height - CHANNEL_NAME_Y_OFFSET;
        final int backgroundColor = channel.getColor() | CHANNEL_TAG_ALPHA;

        graphics.fill(CHANNEL_TAG_X, channelTagY, CHANNEL_TAG_X + textWidth + 3, channelTagY + channelTagHeight, backgroundColor);
        graphics.drawString(minecraft.font, displayName, CHANNEL_NAME_X, channelNameY, 0xFFFFFFFF);

        if (focused) {
            final int x = CHANNEL_TAG_X + textWidth + FOCUS_ICON_PADDING;
            final int y = channelTagY;

            // (renderType, texture, x, y, u, v, width, height, textureWidth, textureHeight)
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE_FOCUS, x, y, 0, 0, FOCUS_ICON_SIZE, FOCUS_ICON_SIZE, FOCUS_ICON_SIZE, FOCUS_ICON_SIZE);

            if (mouseX >= x && mouseX <= x + FOCUS_ICON_SIZE && mouseY >= y && mouseY <= y + FOCUS_ICON_SIZE) {
                MutableComponent tooltipComponent = Component.translatable("meh.channel.focus.tooltip", Component.literal("Ctrl + F"));
                graphics.setTooltipForNextFrame(Tooltip.splitTooltip(minecraft, tooltipComponent), mouseX, mouseY);
            }
        }
    }

    private void onRenderChatScreen(Screen screen, GuiGraphics graphics, int mouseX, int mouseY, float delta) { // Exists only as a way to target with method reference
        this.onRenderChatScreen((ChatScreen) screen, graphics, mouseX, mouseY, delta);
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
