package wtf.choco.meh.client.mixin;

import net.minecraft.client.GuiMessage;
import net.minecraft.client.gui.components.ChatComponent;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.chat.filter.ChatFilterable;
import wtf.choco.meh.client.chat.filter.ChatMessageFilter;

@Mixin(ChatComponent.class)
public abstract class ChatComponentMixin implements ChatFilterable {

    @Unique
    @Nullable
    private ChatMessageFilter messageFilter = null;

    @Inject(method = "addMessageToDisplayQueue(Lnet/minecraft/client/GuiMessage;)V", at = @At("HEAD"), cancellable = true)
    private void onAddMessageToDisplayQueue(GuiMessage message, CallbackInfo callback) {
        ChatMessageFilter filter = messageFilter;
        if (filter != null && !filter.test(message)) {
            callback.cancel();
        }
    }

    @SuppressWarnings("unused")
    @ModifyConstant(method = "addMessageToQueue(Lnet/minecraft/client/GuiMessage;)V", constant = @Constant(intValue = 100))
    private int modifyMaxChatHistory(int value) {
        return getMaxInMemoryChatHistory();
    }

    @Shadow
    public abstract void rescaleChat();

    @Override
    public void setChatMessageFilter(@Nullable ChatMessageFilter filter) {
        this.messageFilter = filter;
        this.rescaleChat();
    }

    @Override
    public ChatMessageFilter getChatMessageFilter() {
        return messageFilter;
    }

    /*
     * By default, 100 messages are rendered in the chat screen, but also 100 messages
     * are kept in memory. We don't want to change how far back the player can scroll or
     * how many messages can be rendered, but because we're allowing for filters, we
     * should really increase the maximum amount of messages that the client remembers.
     */
    private static int getMaxInMemoryChatHistory() {
        return MEHClient.getConfig().getMaxRememberedChatHistory();
    }

}
