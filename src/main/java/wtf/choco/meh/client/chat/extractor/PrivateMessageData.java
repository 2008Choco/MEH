package wtf.choco.meh.client.chat.extractor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.Nullable;

public final record PrivateMessageData(Direction direction, @Nullable String rank, String username, String message) {

    /*
     * (direction) (?rank) (name): (message)
     *
     * From [ADMIN] 2008Choco: message
     * To [MVP++] Player: message
     * From UnrankedPlayer: message
     */
    static final Pattern PATTERN = Pattern.compile("^(?<direction>" + Direction.INCOMING.text + "|" + Direction.OUTGOING.text + ")(?:\\s\\[(?<rank>.+)\\])?\\s(?<username>\\w+):\\s*(?<message>.+)$");

    static PrivateMessageData fromMatcher(Matcher matcher) {
        Direction direction = Direction.fromText(matcher.group("direction"));
        String rank = matcher.group("rank");
        String username = matcher.group("username");
        String message = matcher.group("message");

        return new PrivateMessageData(direction, rank, username, message);
    }

    public static enum Direction {

        INCOMING("From"),
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

        private static Direction fromText(String text) {
            return FROM_TEXT.getOrDefault(text, INCOMING);
        }

    }

}
