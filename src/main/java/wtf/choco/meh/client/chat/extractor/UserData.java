package wtf.choco.meh.client.chat.extractor;

import com.google.common.base.Preconditions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.Nullable;

import static wtf.choco.meh.client.chat.extractor.RegExUtil.userMatchString;

/**
 * Data extracted from text that contains a username and their rank (if they have one).
 *
 * @param rank the rank of the user, or null if none
 * @param username the username of the user
 */
public record UserData(@Nullable String rank, String username) {

    static final Pattern PATTERN_PARTY_DISBAND = Pattern.compile("^" + userMatchString() + " has disbanded the party!");
    static final Pattern PATTERN_PARTY_JOIN_OTHER = Pattern.compile("^" + userMatchString() + " joined the party.$");
    static final Pattern PATTERN_PARTY_JOIN_SELF = Pattern.compile("^You have joined " + userMatchString() + "'s* party!$");
    static final Pattern PATTERN_PARTY_LEAVE_OTHER = Pattern.compile("^" + userMatchString() + " has left the party.$");
    static final Pattern PATTERN_PARTY_KICKED_OTHER = Pattern.compile("^" + userMatchString() + " has been removed from the party.$");
    static final Pattern PATTERN_PARTY_KICKED_SELF = Pattern.compile("^You have been kicked from the party by " + userMatchString() + "$");

    /**
     * Construct a {@link UserData} instance from a RegEx {@link Matcher} that has already
     * successfully matched against a string.
     *
     * @param matcher the matched matcher instance from which to extract data
     *
     * @return the data object containing all relevant data from the matcher
     */
    static UserData fromMatcher(Matcher matcher) {
        Preconditions.checkArgument(matcher.hasMatch(), "Expected matcher to already have matched");

        String rank = matcher.group("rank");
        String username = matcher.group("username");

        return new UserData(rank, username);
    }

}
