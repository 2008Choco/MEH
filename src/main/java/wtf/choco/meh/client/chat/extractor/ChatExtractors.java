package wtf.choco.meh.client.chat.extractor;

/**
 * An object holding constants of common {@link ChatExtractor} implementations used by MEH.
 */
public final class ChatExtractors {

    // PrivateMessageData
    /**
     * @see PrivateMessageData
     */
    public static final ChatExtractor<PrivateMessageData> PRIVATE_MESSAGE = new RegExChatExtractor<>(Patterns.PATTERN_PRIVATE_MESSAGE, PrivateMessageData::fromMatcher);

    // PartyRoleChangeData
    /**
     * @see PartyRoleChangeData
     */
    public static final ChatExtractor<PartyRoleChangeData> PARTY_ROLE_CHANGE = new RegExChatExtractor<>(Patterns.PATTERN_PARTY_ROLE_CHANGE, PartyRoleChangeData::fromMatcher);

    // UserData
    /**
     * Extracts the rank and username of the party leader.
     *
     * @see UserData
     */
    public static final ChatExtractor<UserData> PARTY_DISBAND_LEADER_DISBANDED = new RegExChatExtractor<>(Patterns.PATTERN_PARTY_DISBAND_LEADER_DISBANDED, UserData::fromMatcher);
    /**
     * Extracts the rank and username of the player that sent the invitation.
     *
     * @see UserData
     */
    public static final ChatExtractor<UserData> PARTY_INVITE = new RegExChatExtractor<>(Patterns.PATTERN_PARTY_INVITE, UserData::fromMatcher);
    /**
     * Extracts the rank and username of the party leader.
     *
     * @see UserData
     */
    public static final ChatExtractor<UserData> PARTY_JOIN = new RegExChatExtractor<>(Patterns.PATTERN_PARTY_JOIN, UserData::fromMatcher);
    /**
     * Extracts the rank and username of the party member that issued the kick.
     *
     * @see UserData
     */
    public static final ChatExtractor<UserData> PARTY_KICK = new RegExChatExtractor<>(Patterns.PATTERN_PARTY_KICK, UserData::fromMatcher);
    /**
     * Extracts the rank and username of the member that was kicked.
     *
     * @see UserData
     */
    public static final ChatExtractor<UserData> PARTY_MEMBER_KICK = new RegExChatExtractor<>(Patterns.PATTERN_PARTY_MEMBER_KICK, UserData::fromMatcher);
    /**
     * Extracts the rank and username of the member that left.
     *
     * @see UserData
     */
    public static final ChatExtractor<UserData> PARTY_MEMBER_LEAVE = new RegExChatExtractor<>(Patterns.PATTERN_PARTY_MEMBER_LEAVE, UserData::fromMatcher);
    /**
     * Extracts the rank and username of the member that joined.
     *
     * @see UserData
     */
    public static final ChatExtractor<UserData> PARTY_USER_JOIN = new RegExChatExtractor<>(Patterns.PATTERN_PARTY_MEMBER_JOIN, UserData::fromMatcher);

    // BiUserData
    /**
     * Extracts the rank and username of the member that was yoinked ({@link BiUserData#targetUser()})
     * and the user that performed the yoink ({@link BiUserData#user()}).
     *
     * @see BiUserData
     */
    public static final ChatExtractor<BiUserData> PARTY_MEMBER_YOINK = new RegExChatExtractor<>(Patterns.PATTERN_PARTY_MEMBER_YOINK, BiUserData::fromMatcher);
    /**
     * Extracts the rank and username of the member to which the party was transfered ({@link BiUserData#targetUser()})
     * and the user that transfered the party ({@link BiUserData#user()}).
     *
     * @see BiUserData
     */
    public static final ChatExtractor<BiUserData> PARTY_TRANSFER = new RegExChatExtractor<>(Patterns.PATTERN_PARTY_TRANSFER, BiUserData::fromMatcher);
    /**
     * Extracts the rank and username of the member that was invited ({@link BiUserData#targetUser()})
     * and the user that sent the invitation ({@link BiUserData#user()}).
     *
     * @see BiUserData
     */
    public static final ChatExtractor<BiUserData> PARTY_USER_INVITE = new RegExChatExtractor<>(Patterns.PATTERN_PARTY_MEMBER_INVITE, BiUserData::fromMatcher);

    // Matchers
    public static final ChatMatcher PARTY_DISBAND_EMPTY = new StringChatMatcher(Patterns.STRING_PARTY_DISBAND_EMPTY);
    public static final ChatMatcher PARTY_DISBAND_LEADER_DISCONNECTED = new StringChatMatcher(Patterns.STRING_PARTY_DISBAND_LEADER_DISCONNECTED);
    public static final ChatMatcher PARTY_LEAVE = new StringChatMatcher(Patterns.STRING_PARTY_LEAVE);

    private ChatExtractors() { }

}
