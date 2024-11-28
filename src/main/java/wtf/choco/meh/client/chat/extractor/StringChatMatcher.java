package wtf.choco.meh.client.chat.extractor;

/**
 * A simple {@link ChatMatcher} that will match against a String using
 * {@link String#equals(Object)} or {@link String#equalsIgnoreCase(String)}.
 */
final class StringChatMatcher implements ChatMatcher {

    private final String string;
    private final boolean ignoreCase;

    /**
     * Construct a {@link StringChatMatcher}.
     *
     * @param string the string to match
     * @param ignoreCase whether or not the input string's casing should be ignored
     */
    StringChatMatcher(String string, boolean ignoreCase) {
        this.string = string;
        this.ignoreCase = ignoreCase;
    }

    /**
     * Construct a case-sensitive {@link StringChatMatcher}.
     *
     * @param string the string to match
     */
    public StringChatMatcher(String string) {
        this(string, false);
    }

    @Override
    public boolean matches(String input) {
        return ignoreCase ? string.equalsIgnoreCase(input) : string.equals(input);
    }

}
