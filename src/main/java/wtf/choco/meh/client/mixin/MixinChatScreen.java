package wtf.choco.meh.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.channel.ChannelSelector;
import wtf.choco.meh.client.channel.ChatChannel;

@Mixin(ChatScreen.class)
public class MixinChatScreen {

    @Shadow
    protected EditBox input;

    @SuppressWarnings("unused")
    @Inject(method = "keyPressed(III)Z", at = @At("HEAD"), cancellable = true)
    private void onKeyPressed(int key, int keycode, int scancode, CallbackInfoReturnable<Boolean> callback) {
        // Don't allow channel switching if not connected to Hypixel
        MEHClient mod = MEHClient.getInstance();
        if (!mod.isConnectedToHypixel()) {
            return;
        }

        // Don't allow channel switching if writing a command
        if (isWritingCommand()) {
            return;
        }

        if (Screen.hasControlDown()) {
            if (key == GLFW.GLFW_KEY_TAB) {
                mod.switchChannel(!Screen.hasShiftDown());
                callback.setReturnValue(true);
            } else if (key == GLFW.GLFW_KEY_MINUS) {
                ChannelSelector channelSelector = mod.getChannelSelector();
                ChatChannel selectedChannel = channelSelector.getSelectedChannel();
                if (!selectedChannel.isRemovable()) {
                    return;
                }

                channelSelector.removeChannel(selectedChannel);
                callback.setReturnValue(true);
            }
        }
    }

    @SuppressWarnings("unused")
    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At("HEAD"))
    private void onRender(GuiGraphics graphics, int screenX, int screenY, float delta, CallbackInfo callback) {
        MEHClient mod = MEHClient.getInstance();
        if (!mod.isConnectedToHypixel() || isWritingCommand()) {
            return;
        }

        ChannelSelector channelSelector = mod.getChannelSelector();
        if (channelSelector == null) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        ChatChannel channel = channelSelector.getSelectedChannel();
        Component displayName = channel.getDisplayName();
        int height = self().height;
        int width = minecraft.font.width(displayName) + 7;
        int backgroundColor = (0x60 << 24) | channel.getColor();

        graphics.fill(2, height - 28, width - 2, height - 16, backgroundColor);
        graphics.drawString(minecraft.font, displayName, 4, height - 26, 0xFFFFFF);
    }

    private boolean isWritingCommand() {
        String text = input.getValue();
        return !text.isEmpty() && text.charAt(0) == '/';
    }

    private ChatScreen self() {
        return ((ChatScreen) ((Object) this));
    }

}
