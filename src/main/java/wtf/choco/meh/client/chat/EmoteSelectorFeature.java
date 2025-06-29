package wtf.choco.meh.client.chat;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.gui.ScreenUtil;
import wtf.choco.meh.client.gui.components.EmoteSelectorWidget;
import wtf.choco.meh.client.mixin.ChatScreenAccessor;
import wtf.choco.meh.client.mixin.ScreenAccessor;

public final class EmoteSelectorFeature extends Feature {

    // Sharing the emote selector between chat screen instances so we don't lose state (last selected emote, etc.)
    private final EmoteSelectorWidget emoteSelector = new EmoteSelectorWidget(this);

    public EmoteSelectorFeature(MEHClient mod) {
        super(mod);
    }

    @Override
    protected boolean isFeatureEnabled(MEHConfig config) {
        return config.getEmoteSelectorConfig().isEnabled();
    }

    @Override
    protected void registerListeners() {
        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!(screen instanceof ChatScreen) || !isEnabled()) {
                return;
            }

            ScreenKeyboardEvents.allowKeyPress(screen).register(this::onKeyInChatScreen);
            ScreenEvents.remove(screen).register(this::onChatScreenClose);
        });

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!(screen instanceof ChatScreen chatScreen) || !isEnabled()) {
                return;
            }

            ((ScreenAccessor) screen).invokeAddRenderableOnly(emoteSelector);
            ((ChatScreenAccessor) screen).getInput().setCanLoseFocus(true);
            this.emoteSelector.setChatScreen(chatScreen);
        });
    }

    private boolean onKeyInChatScreen(ChatScreen screen, int key, int keycode, int modifiers) {
        if (!isEnabled() || ScreenUtil.isWritingCommand(screen)) {
            return true;
        }

        if (Screen.hasControlDown() && key == InputConstants.KEY_E && !emoteSelector.isFocused()) {
            this.emoteSelector.takeFocus(screen);
            return false;
        }

        if (emoteSelector.isFocused()) {
            this.emoteSelector.onKeyPress(key, keycode, modifiers);
            return false;
        }

        return true;
    }

    private boolean onKeyInChatScreen(Screen screen, int key, int keycode, int scancode) { // bridge
        return onKeyInChatScreen((ChatScreen) screen, key, keycode, scancode);
    }

    @SuppressWarnings("unused")
    private void onChatScreenClose(Screen screen) {
        this.emoteSelector.setFocused(false);
        this.emoteSelector.setChatScreen(null);
    }

}
