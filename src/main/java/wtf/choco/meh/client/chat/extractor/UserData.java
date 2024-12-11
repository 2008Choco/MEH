package wtf.choco.meh.client.chat.extractor;

import com.google.common.base.Preconditions;

import java.util.regex.Matcher;

import org.jetbrains.annotations.Nullable;

/**
 * Data extracted from text that contains a username and their rank (if they have one).
 *
 * @param rank the rank of the user, or null if none
 * @param username the username of the user
 */
public record UserData(@Nullable String rank, String username) {

    static UserData fromMatcher(Matcher matcher) {
        Preconditions.checkArgument(matcher.hasMatch(), "Expected matcher to already have matched");

        String rank = matcher.group("rank");
        String username = matcher.group("username");

        return new UserData(rank, username);
    }

}
