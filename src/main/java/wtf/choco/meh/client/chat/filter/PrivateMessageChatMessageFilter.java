package wtf.choco.meh.client.chat.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.GuiMessage;

final class PrivateMessageChatMessageFilter implements ChatMessageFilter {

    private static final String FROM = "From", TO = "To";

    /*
     * (direction) (?rank) (name): (message)
     *
     * From [ADMIN] 2008Choco: message
     * To [MVP++] Player: message
     * From UnrankedPlayer: message
     */
    private static final Pattern PATTERN_MESSAGE = Pattern.compile("^(?<direction>" + FROM + "|" + TO + ")(?:\\s\\[(?<rank>.+)\\])?\\s(?<username>\\w+):\\s*(?<message>.+)$");

    private final String username;

    PrivateMessageChatMessageFilter(String username) {
        this.username = username;
    }

    @Override
    public boolean test(GuiMessage message) {
        Matcher matcher = PATTERN_MESSAGE.matcher(message.content().getString());
        return matcher.matches() && matcher.group("username").equals(username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof PrivateMessageChatMessageFilter other && username.equals(other.username));
    }

}
