package wtf.choco.meh.client.chat.extractor;

import java.util.StringJoiner;

import org.jetbrains.annotations.Nullable;

/**
 * A utility class for RegEx pattern generation.
 */
final class RegExUtil {

    private static final String HYPIXEL_RANK_AND_USERNAME_MATCH_STRING = "(?:\\[(?<rank>[\\w+]+)\\] )?(?<username>\\w+)";

    /**
     * An object capable of containing match text.
     */
    interface Matchable {

        /**
         * Get the text relevant to a matcher.
         *
         * @return the match text
         */
        public String getMatchText();

    }

    private RegExUtil() { }

    /**
     * Generate a RegEx-compliant named capture group for all constants in the given
     * {@link Matchable} enum.
     *
     * @param <E> the enum type
     * @param enumClass the class of the enum whose constants to list in the capture group
     * @param groupName the name of the capture group
     *
     * @return the RegEx-compliant named capture group
     */
    static <E extends Enum<E> & Matchable> String toMatchString(Class<E> enumClass, String groupName) {
        StringJoiner string = new StringJoiner("|", "(?<" + groupName + ">", ")");

        for (E constant : enumClass.getEnumConstants()) {
            string.add(constant.getMatchText());
        }

        return string.toString();
    }

    /**
     * Generate a RegEx-compliant string that will match a Hypixel username (and optional rank) with
     * 2 named capture groups, {@code "rank"} and {@code "username"}.
     *
     * @return the user match string
     */
    static String userMatchString() {
        return userMatchString(null);
    }

    /**
     * Generate a RegEx-compliant string that will match a Hypixel username (and optional rank) with
     * 2 named capture groups with prefixed names. If {@code groupPrefix} is {@code null}, the group
     * names will be {@code "rank"} and {@code "username"}. If a prefix is provided, those names will
     * have the prefix prepended and the first letter capitalized (to retain camelCase). For example:
     * <pre>
     * userMatchString("target") {@literal ->} matcher.group("targetRank"), matcher.group("targetUsername");
     * userMatchString("killer") {@literal ->} matcher.group("killerRank"), matcher.group("killerUsername");
     * userMatchString(null) {@literal ->} matcher.group("rank"), matcher.group("username");
     * </pre>
     *
     * @return the user match string
     */
    static String userMatchString(@Nullable String groupPrefix) {
        if (groupPrefix == null) {
            return HYPIXEL_RANK_AND_USERNAME_MATCH_STRING;
        } else {
            return "(?:\\[(?<" + groupPrefix + "Rank>[\\w+]+)\\] )?(?<" + groupPrefix + "Username>\\w+)";
        }
    }

}
