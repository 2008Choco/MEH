package wtf.choco.meh.client.chat.extractor;

import com.google.common.base.Enums;
import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import wtf.choco.meh.client.party.PartyRole;

/**
 * Data extracted from a Hypixel party promotion or demotion.
 *
 * @param user the user that performed the role change
 * @param action the action that was taken
 * @param target the user whose role is being changed
 * @param role the new role of the target user
 *
 * @see ChatExtractors#PARTY_ROLE_CHANGE
 */
public record PartyRoleChangeData(UserData user, Action action, UserData target, PartyRole role) {

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
        PartyRole role = Enums.getIfPresent(PartyRole.class, matcher.group("role").toUpperCase()).get();

        return new PartyRoleChangeData(new UserData(rank, username), action, new UserData(targetRank, targetUsername), role);
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

    }

}
