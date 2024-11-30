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

    /**
     * Match against a Hypixel party disband due to the party being empty.
     */
    public static final ChatMatcher PARTY_DISBAND_EMPTY = new StringChatMatcher("The party was disbanded because all invites expired and the party was empty.");

    /**
     * Match against a Hypixel party disband due to the party leader leaving.
     */
    public static final ChatMatcher PARTY_DISBAND_LEADER_DISCONNECTED = new StringChatMatcher("The party was disbanded because the party leader disconnected.");

    /**
     * Match against a Hypixel party disband due to user action.
     *
     * @see UserData
     */
    public static final ChatExtractor<UserData> PARTY_DISBAND = new RegExChatExtractor<>(UserData.PATTERN_PARTY_DISBAND, UserData::fromMatcher);

    /**
     * Match against and extract data from a Hypixel party invitation.
     *
     * @see BiUserData
     */
    public static final ChatExtractor<BiUserData> PARTY_INVITE = new RegExChatExtractor<>(BiUserData.PATTERN_PARTY_INVITE, BiUserData::fromMatcher);

    /**
     * Match against and extract data from when you join another another player's Hypixel party.
     *
     * @see UserData
     */
    public static final ChatExtractor<UserData> PARTY_JOIN_SELF = new RegExChatExtractor<>(UserData.PATTERN_PARTY_JOIN_SELF, UserData::fromMatcher);

    /**
     * Match against and extract data from when another player joins a Hypixel party that you are in.
     *
     * @see UserData
     */
    public static final ChatExtractor<UserData> PARTY_JOIN_OTHER = new RegExChatExtractor<>(UserData.PATTERN_PARTY_JOIN_OTHER, UserData::fromMatcher);

    /**
     * Match against and extract data from when another member is kicked from the party.
     *
     * @see UserData
     */
    public static final ChatExtractor<UserData> PARTY_KICK_OTHER = new RegExChatExtractor<>(UserData.PATTERN_PARTY_KICKED_OTHER, UserData::fromMatcher);

    /**
     * Match against and extract data from when the client is kicked from the party.
     *
     * @see UserData
     */
    public static final ChatExtractor<UserData> PARTY_KICKED_SELF = new RegExChatExtractor<>(UserData.PATTERN_PARTY_KICKED_SELF, UserData::fromMatcher);

    /**
     * Match against and extract data from a Hypixel party leave.
     *
     * @see UserData
     */
    public static final ChatExtractor<UserData> PARTY_LEAVE = new RegExChatExtractor<>(UserData.PATTERN_PARTY_LEAVE, UserData::fromMatcher);

    /**
     * Match against and extract data from a Hypixel party role promotion or demotion.
     *
     * @see PartyRoleChangeData
     */
    public static final ChatExtractor<PartyRoleChangeData> PARTY_ROLE_CHANGE = new RegExChatExtractor<>(PartyRoleChangeData.PATTERN, PartyRoleChangeData::fromMatcher);

    /**
     * Match against and extract data from a Hypixel party transfer.
     *
     * @see BiUserData
     */
    public static final ChatExtractor<BiUserData> PARTY_TRANSFER = new RegExChatExtractor<>(BiUserData.PATTERN_PARTY_TRANSFER, BiUserData::fromMatcher);

    private ChatExtractors() { }

}
