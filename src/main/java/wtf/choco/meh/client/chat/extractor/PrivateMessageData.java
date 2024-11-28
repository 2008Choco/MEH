package wtf.choco.meh.client.chat.extractor;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.Nullable;

import static wtf.choco.meh.client.chat.extractor.RegExUtil.userMatchString;

/**
 * Data extracted from a Hypixel private message.
 *
 * @param direction the direction of the private message
 * @param rank the rank of the user with whom the client is communicating, or null if none
 * @param username the username of the user with whom the client is communicating
 * @param message the plain text message sent in the private message
 *
 * @see ChatExtractors#PRIVATE_MESSAGE
 */
public final record PrivateMessageData(Direction direction, @Nullable String rank, String username, String message) {

    /*
     * (direction) (?rank) (name): (message)
     *
     * From [ADMIN] 2008Choco: message
     * To [MVP++] Player: message
     * From UnrankedPlayer: message
     */
    static final Pattern PATTERN = Pattern.compile("^" + Direction.toMatchString("direction") + " " + userMatchString() + ": (?<message>.+)");

    /**
     * Construct a {@link PrivateMessageData} instance from a RegEx {@link Matcher} that has
     * already successfully matched against a string.
     *
     * @param matcher the matched matcher instance from which to extract data
     *
     * @return the message data object containing all relevant data from the matcher
     */
    static PrivateMessageData fromMatcher(Matcher matcher) {
        Preconditions.checkArgument(matcher.hasMatch(), "Expected matcher to already have matched");

        Direction direction = Direction.fromText(matcher.group("direction"));
        String rank = matcher.group("rank");
        String username = matcher.group("username");
        String message = matcher.group("message");

        return new PrivateMessageData(direction, rank, username, message);
    }

    /**
     * A direction in which a private message may travel.
     */
    public static enum Direction implements RegExUtil.Matchable {

        /**
         * The message is incoming, sent from another user.
         */
        INCOMING("From"),

        /**
         * The message is outgoing, sent to another user.
         */
        OUTGOING("To");

        private static final Map<String, Direction> FROM_TEXT = new HashMap<>();

        static {
            for (Direction direction : values()) {
                FROM_TEXT.put(direction.text, direction);
            }
        }

        private final String text;

        private Direction(String text) {
            this.text = text;
        }

        @Override
        public String getMatchText() {
            return text;
        }

        private static Direction fromText(String text) {
            return FROM_TEXT.getOrDefault(text, INCOMING);
        }

        private static String toMatchString(String groupName) {
            return RegExUtil.toMatchString(Direction.class, groupName);
        }

    }

}
