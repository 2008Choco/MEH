package wtf.choco.meh.client.chat.filter;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import net.minecraft.client.GuiMessage;

/**
 * A functional interface capable of testing whether or not a {@link GuiMessage} is allowed to
 * be rendered in the client's chat history.
 */
@FunctionalInterface
public interface ChatMessageFilter extends Predicate<GuiMessage> {

    /**
     * Test the given {@link GuiMessage} through this filter.
     *
     * @return true if the message passes this filter and is allowed to be rendered in the client's
     * chat history, or false if the message should be hidden
     */
    @Override
    public boolean test(GuiMessage message);

    /**
     * Create a {@link ChatMessageFilter} that will filter out any message that are not Hypixel
     * private messages between the client and the provided (case-sensitive) username.
     *
     * @param username the username of the user whose private messages to filter for
     *
     * @return the filter
     */
    public static ChatMessageFilter privateMessage(String username) {
        return new PrivateMessageChatMessageFilter(username);
    }

    /**
     * Create a {@link ChatMessageFilter} that will filter out any message that does not match the
     * given RegEx {@link Pattern}.
     *
     * @param pattern the pattern to match against
     * @param exact if the message should match exactly and whole-ly against the regular expression.
     * If false, there must only be at least one match anywhere in the message
     *
     * @return the filter
     */
    public static ChatMessageFilter regex(Pattern pattern, boolean exact) {
        return new RegExChatMessageFilter(pattern, exact);
    }

    /**
     * Create a {@link ChatMessageFilter} that will filter out any message that does not match the
     * given RegEx pattern.
     *
     * @param pattern the RegEx-compliant pattern string to compile and match against
     * @param exact if the message should match exactly and whole-ly against the regular expression.
     * If false, there must only be at least one match anywhere in the message
     *
     * @return the filter
     */
    public static ChatMessageFilter regex(String pattern, boolean exact) {
        return regex(Pattern.compile(pattern), exact);
    }

    /**
     * Create a {@link ChatMessageFilter} that will filter out any message that does not start with
     * the given string of text.
     *
     * @param text the text that the string must start with
     *
     * @return the filter
     */
    public static ChatMessageFilter startsWith(String text) {
        return new StartsWithChatMessageFilter(text);
    }

}
