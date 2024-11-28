package wtf.choco.meh.client.chat.extractor;

import java.util.Optional;

/**
 * Represents a process by which an input string can be matched and have data extracted from it.
 * <p>
 * This is a specialized type of {@link ChatMatcher} that will extract additional data from the
 * text that it matched against. This object will still work as a chat matcher and will, by default,
 * operate based on whether or not this extractor was successful in extracting data. Implementations
 * may change how {@link #matches(String)} behaves for performance reasons, but should still abide
 * by ChatMatcher's API contract.
 *
 * @param <R> the object type holding the result of the data extraction
 *
 * @see ChatExtractors
 */
public interface ChatExtractor<R> extends ChatMatcher {

    /**
     * Extract data from the given input string.
     *
     * @param input the input string from which to extract data
     *
     * @return an optional containing the result of the extraction, or an empty optional if the
     * extraction failed for any reason unknown to callers
     */
    public Optional<R> extract(String input);

    @Override
    public default boolean matches(String input) {
        return extract(input).isPresent();
    }

}
