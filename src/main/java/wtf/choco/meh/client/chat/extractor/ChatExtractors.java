package wtf.choco.meh.client.chat.extractor;

public final class ChatExtractors {

    public static final ChatExtractor<PrivateMessageData> PRIVATE_MESSAGE = new RegExChatExtractor<>(PrivateMessageData.PATTERN, PrivateMessageData::fromMatcher);

    private ChatExtractors() { }

}
