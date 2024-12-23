package wtf.choco.meh.client.chat.extractor;

import java.util.regex.Pattern;

import static wtf.choco.meh.client.chat.extractor.RegExUtil.enumMatchString;
import static wtf.choco.meh.client.chat.extractor.RegExUtil.userMatchString;

/**
 * A holder class for strings and RegEx patterns used by {@link ChatExtractors}.
 */
final class Patterns {

    // PartyRoleChangeData
    static final Pattern PATTERN_PARTY_ROLE_CHANGE = Pattern.compile("^" + userMatchString() + " has " + enumMatchString(PartyRoleChangeData.Action.class, "action") + " " + userMatchString("target") + " to (?<role>[\\w\\s]+)$");

    // PrivateMessageData
    static final Pattern PATTERN_PRIVATE_MESSAGE = Pattern.compile("^" + enumMatchString(PrivateMessageData.Direction.class, "direction") + " " + userMatchString() + ": (?<message>.+)");

    // UserData
    static final Pattern PATTERN_PARTY_DISBAND_LEADER_DISBANDED = Pattern.compile("^" + userMatchString() + " has disbanded the party!");
    static final Pattern PATTERN_PARTY_INVITE = Pattern.compile("^" + userMatchString() + " has invited you to join their party!$");
    static final Pattern PATTERN_PARTY_JOIN = Pattern.compile("^You have joined " + userMatchString() + "'s* party!$");
    static final Pattern PATTERN_PARTY_KICK = Pattern.compile("^You have been kicked from the party by " + userMatchString() + "$");
    static final Pattern PATTERN_PARTY_MEMBER_BARGE = Pattern.compile("^" + userMatchString() + " barged into the party.$");
    static final Pattern PATTERN_PARTY_MEMBER_JOIN = Pattern.compile("^" + userMatchString() + " joined the party.$");
    static final Pattern PATTERN_PARTY_MEMBER_KICK = Pattern.compile("^" + userMatchString() + " has been removed from the party.$");
    static final Pattern PATTERN_PARTY_MEMBER_LEAVE = Pattern.compile("^" + userMatchString() + " has left the party.$");

    // BiUserData
    static final Pattern PATTERN_PARTY_MEMBER_INVITE = Pattern.compile("^" + userMatchString() + " invited " + userMatchString("target") + " to the party! They have \\d+ seconds to accept.$");
    static final Pattern PATTERN_PARTY_MEMBER_YOINK = Pattern.compile("^" + userMatchString("target") + " was yoinked into the party by " + userMatchString() + "$");
    static final Pattern PATTERN_PARTY_TRANSFER = Pattern.compile("^The party was transferred to " + userMatchString("target") + " by " + userMatchString() + "$");

    // Strings
    static final String STRING_PARTY_BARGE = "You have successfully barged into the party.";
    static final String STRING_PARTY_DISBAND_EMPTY = "The party was disbanded because all invites expired and the party was empty.";
    static final String STRING_PARTY_DISBAND_LEADER_DISCONNECTED = "The party was disbanded because the party leader disconnected.";
    static final String STRING_PARTY_LEAVE = "You left the party.";

    private Patterns() { }

}
