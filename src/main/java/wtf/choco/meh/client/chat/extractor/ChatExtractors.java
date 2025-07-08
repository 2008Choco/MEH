package wtf.choco.meh.client.chat.extractor;

import java.util.function.Function;
import java.util.regex.Matcher;

/**
 * An object holding constants of common {@link ChatExtractor} implementations used by MEH.
 */
public final class ChatExtractors {

    private static final Function<Matcher, String> STRING_EXTRACTOR = matcher -> matcher.group("string");

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

    // String
    /**
     * Extracts the name of the fish that was caught.
     */
    public static final ChatExtractor<String> FISHING_CAUGHT_GENERIC = new RegExChatExtractor<>(Patterns.PATTERN_FISHING_CAUGHT_GENERIC, STRING_EXTRACTOR);
    /**
     * Extracts the name of the junk that was caught.
     */
    public static final ChatExtractor<String> FISHING_CAUGHT_JUNK = new RegExChatExtractor<>(Patterns.PATTERN_FISHING_CAUGHT_JUNK, STRING_EXTRACTOR);
    /**
     * Extracts the name of the treasure that was caught.
     */
    public static final ChatExtractor<String> FISHING_CAUGHT_TREASURE = new RegExChatExtractor<>(Patterns.PATTERN_FISHING_CAUGHT_TREASURE, STRING_EXTRACTOR);

    // QuantitativeTreasureCatchData
    /**
     * @see QuantitativeTreasureCatchData
     */
    public static final ChatExtractor<QuantitativeTreasureCatchData> FISHING_CAUGHT_QUANTITATIVE_TREASURE = new RegExChatExtractor<>(Patterns.PATTERN_FISHING_CAUGHT_QUANTITATIVE_TREASURE, QuantitativeTreasureCatchData::fromMatcher);

    // MythicalFishCatchData
    /**
     * See MythicalFishCatchData
     */
    public static final ChatExtractor<MythicalFishCatchData> FISHING_CAUGHT_MYTHICAL_FISH = new RegExChatExtractor<>(Patterns.PATTERN_FISHING_CAUGHT_MYTHICAL_FISH, MythicalFishCatchData::fromMatcher);

    // UserData
    /**
     * Extracts the rank and username of the party leader.
     *
     * @see UserData
     */
    public static final ChatExtractor<UserData> PARTY_DISBAND_LEADER_DISBANDED = new RegExChatExtractor<>(Patterns.PATTERN_PARTY_DISBAND_LEADER_DISBANDED, UserData::fromMatcher);
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
     * Extracts the rank and username of the member that barged into the party.
     *
     * @see UserData
     */
    public static final ChatExtractor<UserData> PARTY_MEMBER_BARGE = new RegExChatExtractor<>(Patterns.PATTERN_PARTY_MEMBER_BARGE, UserData::fromMatcher);
    /**
     * Extracts the rank and username of the member that disconnected.
     *
     * @see UserData
     */
    public static final ChatExtractor<UserData> PARTY_MEMBER_DISCONNECT = new RegExChatExtractor<>(Patterns.PATTERN_PARTY_MEMBER_DISCONNECT, UserData::fromMatcher);
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
     * Extracts the rank and username of the member that rejoined.
     *
     * @see UserData
     */
    public static final ChatExtractor<UserData> PARTY_MEMBER_REJOIN = new RegExChatExtractor<>(Patterns.PATTERN_PARTY_MEMBER_REJOIN, UserData::fromMatcher);
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

    // Matchers
    public static final ChatMatcher PARTY_BARGE = new StringChatMatcher(Patterns.STRING_PARTY_BARGE);
    public static final ChatMatcher PARTY_DISBAND_EMPTY = new StringChatMatcher(Patterns.STRING_PARTY_DISBAND_EMPTY);
    public static final ChatMatcher PARTY_DISBAND_LEADER_DISCONNECTED = new StringChatMatcher(Patterns.STRING_PARTY_DISBAND_LEADER_DISCONNECTED);
    public static final ChatMatcher PARTY_LEAVE = new StringChatMatcher(Patterns.STRING_PARTY_LEAVE);

    private ChatExtractors() { }

}
