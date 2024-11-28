package wtf.choco.meh.client.chat.extractor;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.Nullable;

import static wtf.choco.meh.client.chat.extractor.RegExUtil.userMatchString;

/**
 * Data extracted from a Hypixel party promotion or demotion.
 *
 * @param rank the rank of the user that performed the role change, or null if none
 * @param username the username of the user that performed the role change
 * @param action the action that was taken
 * @param targetRank the rank of the user whose role is being changed, or null if none
 * @param targetUsername the username of the user whose role is being changed
 * @param role the new role of the target user
 *
 * @see ChatExtractors#PARTY_ROLE_CHANGE
 */
public record PartyRoleChangeData(@Nullable String rank, String username, Action action, @Nullable String targetRank, String targetUsername, String role) {

    static final Pattern PATTERN = Pattern.compile("^" + userMatchString() + " has " + Action.toMatchString("action") + " " + userMatchString("target") + " to (?<role>[\\w\\s]+)$");

    /**
     * Construct a {@link PartyRoleChangeData} instance from a RegEx {@link Matcher} that
     * has already successfully matched against a string.
     *
     * @param matcher the matched matcher instance from which to extract data
     *
     * @return the data object containing all relevant data from the matcher
     */
    static PartyRoleChangeData fromMatcher(Matcher matcher) {
        Preconditions.checkArgument(matcher.hasMatch(), "Expected matcher to already have matched");

        String rank = matcher.group("rank");
        String username = matcher.group("username");
        Action action = Action.fromText(matcher.group("action"));
        String targetRank = matcher.group("targetRank");
        String targetUsername = matcher.group("targetUsername");
        String role = matcher.group("role");

        return new PartyRoleChangeData(rank, username, action, targetRank, targetUsername, role);
    }

    /**
     * An action to occur in a party role change.
     */
    public static enum Action implements RegExUtil.Matchable {

        PROMOTED("promoted"),
        DEMOTED("demoted");

        private static final Map<String, Action> FROM_TEXT = new HashMap<>();

        static {
            for (Action action : values()) {
                FROM_TEXT.put(action.text, action);
            }
        }

        private final String text;

        private Action(String text) {
            this.text = text;
        }

        @Override
        public String getMatchText() {
            return text;
        }

        private static Action fromText(String text) {
            return FROM_TEXT.getOrDefault(text, PROMOTED);
        }

        private static String toMatchString(String groupName) {
            return RegExUtil.toMatchString(Action.class, groupName);
        }

    }

}
