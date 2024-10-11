package wtf.choco.meh.client.chat.filter;

import java.util.function.Predicate;

import net.minecraft.client.GuiMessage;

@FunctionalInterface
public interface ChatMessageFilter extends Predicate<GuiMessage> {

    public static ChatMessageFilter privateMessage(String username) {
        return new PrivateMessageChatMessageFilter(username);
    }

}
