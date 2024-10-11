package wtf.choco.meh.client.mixin;

import net.minecraft.client.GuiMessage;
import net.minecraft.client.gui.components.ChatComponent;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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

    @Shadow
    public abstract void rescaleChat();

    @Override
    public void setChatMessageFilter(@Nullable ChatMessageFilter filter) {
        MEHClient.LOGGER.info("Updated chat filter to " + filter);
        this.messageFilter = filter;
        this.rescaleChat();
    }

    @Override
    public ChatMessageFilter getChatMessageFilter() {
        return messageFilter;
    }

}
