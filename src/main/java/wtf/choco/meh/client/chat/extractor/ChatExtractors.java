package wtf.choco.meh.client.chat.extractor;

/**
 * An object holding constants of common {@link ChatExtractor} implementations used by MEH.
 */
public final class ChatExtractors {

    /**
     * Match against and extract data from a Hypixel private message.
     *
     * @see PrivateMessageData
     */
    public static final ChatExtractor<PrivateMessageData> PRIVATE_MESSAGE = new RegExChatExtractor<>(PrivateMessageData.PATTERN, PrivateMessageData::fromMatcher);

    private ChatExtractors() { }

}
