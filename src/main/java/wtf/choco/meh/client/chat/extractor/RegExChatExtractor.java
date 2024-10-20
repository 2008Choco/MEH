package wtf.choco.meh.client.chat.extractor;

import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RegExChatExtractor<R> implements ChatExtractor<R> {

    private final Pattern pattern;
    private final Function<Matcher, R> resultExtractor;

    RegExChatExtractor(Pattern pattern, Function<Matcher, R> resultExtractor) {
        this.pattern = pattern;
        this.resultExtractor = resultExtractor;
    }

    @Override
    public Optional<R> extract(String input) {
        Matcher matcher = pattern.matcher(input);
        return matcher.matches() ? Optional.of(resultExtractor.apply(matcher)) : Optional.empty();
    }

}
