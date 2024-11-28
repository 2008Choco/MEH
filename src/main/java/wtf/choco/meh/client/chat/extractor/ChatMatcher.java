package wtf.choco.meh.client.chat.extractor;

/**
 * Represents a process by which an input string can be matched against.
 *
 * @see ChatExtractor
 */
public interface ChatMatcher {

    /**
     * Check whether or not the given string matches the parameters of this {@link ChatMatcher}.
     *
     * @param input the input text to test
     *
     * @return true if the input matches, false otherwise
     */
    public boolean matches(String input);

}
