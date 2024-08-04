package wtf.choco.meh.client.pksim;

import it.unimi.dsi.fastutil.ints.IntIntPair;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.Nullable;

public final class PKSimUtils {

    // https://www.compart.com/en/unicode - Search tool for unicode characters

    // Fonts
    private static final char FANCY_A_CAPITALIZED = '\uFF21';
    private static final char FANCY_A = '\uFF41';

    // Symbols
    private static final char WHITE_HEART = '\u2661';
    private static final char FILLED_RIGHT_POINTING_POINTER = '\u25BA';
    private static final char FILLED_FOUR_POINTED_STAR = '\u2726'; // Used for "experience"
    private static final char HEAVY_SPARKLE = '\u2748'; // Used for "gems"
    private static final char STACK_OF_COINS = '\u26C3'; // Used for "coins"

    public static final String SCOREBOARD_IDENTIFIER = "PK " + fancify("SIMULATOR") + " " + WHITE_HEART;

    private static final Pattern PATTERN_LEVEL = Pattern.compile(FILLED_RIGHT_POINTING_POINTER + "\\s*Level:\\s+(?<level>.+)");
    private static final Pattern PATTERN_EXPERIENCE = Pattern.compile(FILLED_RIGHT_POINTING_POINTER + "\\s*Exp:\\s+" + FILLED_FOUR_POINTED_STAR + "(?<currentExperience>.+)\\/(?<requiredExperience>.+)");
    private static final Pattern PATTERN_PRESTIGE = Pattern.compile(".\\s*Prestige:\\s+(?<prestige>.+)");
    private static final Pattern PATTERN_GEMS = Pattern.compile(".\\s*Gems:\\s+" + HEAVY_SPARKLE + "(?<gems>.+)");
    private static final Pattern PATTERN_COINS = Pattern.compile(FILLED_RIGHT_POINTING_POINTER + "\\s*Coins:\\s+" + STACK_OF_COINS + "(?<coins>.+)");

    private PKSimUtils() { }

    public static int extractLevel(String entry) {
        return extractSimpleIntegerValue(PATTERN_LEVEL, entry, "level");
    }

    @Nullable
    public static IntIntPair extractExperience(String entry) {
        Matcher matcher = PATTERN_EXPERIENCE.matcher(entry);
        if (matcher.matches()) {
            int currentExperience = extractInteger(matcher, "currentExperience");
            int requiredExperience = extractInteger(matcher, "requiredExperience");
            return IntIntPair.of(currentExperience, requiredExperience);
        }

        return null;
    }

    public static int extractPrestige(String entry) {
        return extractSimpleIntegerValue(PATTERN_PRESTIGE, entry, "prestige");
    }

    public static int extractGems(String entry) {
        return extractSimpleIntegerValue(PATTERN_GEMS, entry, "gems");
    }

    public static int extractCoins(String entry) {
        return extractSimpleIntegerValue(PATTERN_COINS, entry, "coins");
    }

    private static int extractSimpleIntegerValue(Pattern pattern, String entry, String groupName) {
        Matcher matcher = pattern.matcher(entry);
        return matcher.matches() ? extractInteger(matcher, groupName) : -1;
    }

    private static int extractInteger(Matcher matcher, String groupName) {
        return NumberUtils.toInt(matcher.group(groupName).replace(",", ""));
    }

    private static String fancify(String text) {
        StringBuilder string = new StringBuilder(text.length());

        for (char character : text.toCharArray()) {
            if (!Character.isLetter(character)) {
                string.append(character);
            }

            if (Character.isUpperCase(character)) {
                string.append((char) (character + FANCY_A_CAPITALIZED - 'A'));
            } else if (Character.isLowerCase(character)) {
                string.append((char) (character + FANCY_A - 'a'));
            } else {
                string.append(character);
            }
        }

        return string.toString();
    }

}
