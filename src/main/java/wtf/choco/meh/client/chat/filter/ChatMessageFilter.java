package wtf.choco.meh.client.chat.filter;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import net.minecraft.client.GuiMessage;

@FunctionalInterface
public interface ChatMessageFilter extends Predicate<GuiMessage> {

    public static ChatMessageFilter privateMessage(String username) {
        return new PrivateMessageChatMessageFilter(username);
    }

    public static ChatMessageFilter regex(Pattern pattern, boolean exact) {
        return new RegExChatMessageFilter(pattern, exact);
    }

    public static ChatMessageFilter regex(String pattern, boolean exact) {
        return regex(Pattern.compile(pattern), exact);
    }

    public static ChatMessageFilter startsWith(String text) {
        return new StartsWithChatMessageFilter(text);
    }

}
