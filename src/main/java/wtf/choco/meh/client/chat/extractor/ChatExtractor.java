package wtf.choco.meh.client.chat.extractor;

import java.util.Optional;

/**
 * Represents a process by which an input string can be matched and have data extracted from it.
 *
 * @param <R> the object type holding the result of the data extraction
 *
 * @see ChatExtractors
 */
public interface ChatExtractor<R> {

    /**
     * Extract data from the given input string.
     *
     * @param input the input string from which to extract data
     *
     * @return an optional containing the result of the extraction, or an empty optional if the
     * extraction failed for any reason unknown to callers
     */
    public Optional<R> extract(String input);

}
