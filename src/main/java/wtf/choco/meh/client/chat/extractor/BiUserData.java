package wtf.choco.meh.client.chat.extractor;

import com.google.common.base.Preconditions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.Nullable;

import static wtf.choco.meh.client.chat.extractor.RegExUtil.userMatchString;

/**
 * Data extracted from text that contains two usernames and their ranks (if they have one).
 * Generally the user data contains one actioner (the rank and username) and one target.
 *
 * @param rank the rank of the user, or null if none
 * @param username the username of the user
 * @param targetRank the rank of the target user, or null if none
 * @param targetUsername the username of the target user
 */
public record BiUserData(@Nullable String rank, String username, @Nullable String targetRank, String targetUsername) {

    static final Pattern PATTERN_PARTY_INVITE = Pattern.compile("^" + userMatchString() + " invited " + userMatchString("target") + " to the party! They have \\d+ seconds to accept.$");
    static final Pattern PATTERN_PARTY_TRANSFER = Pattern.compile("^The party was transferred to " + userMatchString("target") + " by " + userMatchString() + "$");

    /**
     * Construct a {@link BiUserData} instance from a RegEx {@link Matcher} that has already
     * successfully matched against a string.
     *
     * @param matcher the matched matcher instance from which to extract data
     *
     * @return the data object containing all relevant data from the matcher
     */
    static BiUserData fromMatcher(Matcher matcher) {
        Preconditions.checkArgument(matcher.hasMatch(), "Expected matcher to already have matched");

        String rank = matcher.group("rank");
        String username = matcher.group("username");
        String targetRank = matcher.group("targetRank");
        String targetUsername = matcher.group("targetUsername");

        return new BiUserData(rank, username, targetRank, targetUsername);
    }

}
