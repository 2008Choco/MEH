package wtf.choco.meh.client.chat.extractor;

import java.util.regex.Pattern;

/**
 * A {@link ChatMatcher} that will match text based on a RegEx pattern.
 */
class RegExChatMatcher implements ChatMatcher {

    protected final Pattern pattern;

    /**
     * Construct a new {@link RegExChatExtractor}.
     *
     * @param pattern the pattern to match against
     */
    RegExChatMatcher(Pattern pattern) {
        this.pattern = pattern;
    }

    /**
     * Construct a new {@link RegExChatExtractor}.
     *
     * @param pattern the pattern to match against. Must be RegEx compliant
     */
    RegExChatMatcher(String regexPattern) {
        this(Pattern.compile(regexPattern));
    }

    @Override
    public boolean matches(String input) {
        return pattern.matcher(input).matches();
    }

}
