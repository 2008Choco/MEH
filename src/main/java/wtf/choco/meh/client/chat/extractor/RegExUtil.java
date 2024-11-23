package wtf.choco.meh.client.chat.extractor;

import java.util.StringJoiner;

/**
 * A utility class for RegEx pattern generation.
 */
final class RegExUtil {

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

}
