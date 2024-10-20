package wtf.choco.meh.client.chat.filter;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.ChatFormatting;
import net.minecraft.client.GuiMessage;

final class RegExChatMessageFilter implements ChatMessageFilter {

    private final Pattern pattern;
    private final boolean exact;

    RegExChatMessageFilter(Pattern pattern, boolean exact) {
        this.pattern = pattern;
        this.exact = exact;
    }

    @Override
    public boolean test(GuiMessage message) {
        Matcher matcher = pattern.matcher(ChatFormatting.stripFormatting(message.content().getString()));
        if (exact) {
            return matcher.matches();
        } else {
            return matcher.find();
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(pattern, exact);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof RegExChatMessageFilter other && pattern.equals(other.pattern) && exact == other.exact);
    }

}
