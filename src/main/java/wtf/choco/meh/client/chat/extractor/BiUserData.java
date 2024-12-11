package wtf.choco.meh.client.chat.extractor;

import com.google.common.base.Preconditions;

import java.util.regex.Matcher;

/**
 * Data extracted from text that contains two usernames and their ranks (if they have one).
 * Generally the user data contains one actioner (the rank and username) and one target.
 *
 * @param user the primary user
 * @param targetUser the target user
 */
public record BiUserData(UserData user, UserData targetUser) {

    static BiUserData fromMatcher(Matcher matcher) {
        Preconditions.checkArgument(matcher.hasMatch(), "Expected matcher to already have matched");

        String rank = matcher.group("rank");
        String username = matcher.group("username");
        String targetRank = matcher.group("targetRank");
        String targetUsername = matcher.group("targetUsername");

        return new BiUserData(new UserData(rank, username), new UserData(targetRank, targetUsername));
    }

}
