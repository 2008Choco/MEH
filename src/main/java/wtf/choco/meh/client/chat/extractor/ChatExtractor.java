package wtf.choco.meh.client.chat.extractor;

import java.util.Optional;

public interface ChatExtractor<R> {

    public Optional<R> extract(String input);

}
