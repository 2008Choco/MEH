package wtf.choco.meh.client.pksim;

import it.unimi.dsi.fastutil.ints.IntIntPair;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.ChatFormatting;

import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.Nullable;

import wtf.choco.meh.client.scoreboard.HypixelScoreboard;

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
    private static final char FILLED_STACK_OF_COINS = '\u26C3'; // Used for "coins"
    private static final char UNFILLED_STACK_OF_COINS = '\u26C1'; // Also used for "coins", but only in some places

    private static final String stackOfCoinsRegExOr() {
        return "[" + FILLED_STACK_OF_COINS + UNFILLED_STACK_OF_COINS + "]";
    }

    // Scoreboard patterns and constants
    private static final int SCOREBOARD_LINE_IDENTIFIER = 12;
    private static final int SCOREBOARD_LINE_COINS = 10;
    private static final int SCOREBOARD_LINE_EXPERIENCE = 9;
    private static final int SCOREBOARD_LINE_LEVEL = 8;
    private static final int SCOREBOARD_LINE_PRESTIGE = 6;
    private static final int SCOREBOARD_LINE_GEMS = 5;

    public static final String SCOREBOARD_IDENTIFIER = "PK " + fancify("SIMULATOR") + " " + WHITE_HEART;

    private static final Pattern PATTERN_LEVEL = Pattern.compile(FILLED_RIGHT_POINTING_POINTER + "\\s*Level:\\s+(?<level>.+)");
    private static final Pattern PATTERN_EXPERIENCE_SCOREBOARD = Pattern.compile(FILLED_RIGHT_POINTING_POINTER + "\\s*Exp:\\s+" + FILLED_FOUR_POINTED_STAR + "(?<currentExperience>.+)\\/(?<requiredExperience>.+)");
    private static final Pattern PATTERN_PRESTIGE = Pattern.compile(".\\s*Prestige:\\s+(?<prestige>.+)");
    private static final Pattern PATTERN_GEMS = Pattern.compile(".\\s*Gems:\\s+" + HEAVY_SPARKLE + "(?<gems>.+)");
    private static final Pattern PATTERN_COINS_SCOREBOARD = Pattern.compile(FILLED_RIGHT_POINTING_POINTER + "\\s*Coins:\\s+" + stackOfCoinsRegExOr() + "(?<coins>.+)");

    // Title patterns and constants
    private static final Pattern PATTERN_EXPERIENCE_TITLE = Pattern.compile(FILLED_FOUR_POINTED_STAR + "(?<currentExperience>.+)\\/(?<requiredExperience>.+)");
    private static final Pattern PATTERN_COINS_SUBTITLE = Pattern.compile(stackOfCoinsRegExOr() + "(?<coins>.+)");
    private static final Pattern PATTERN_LEVEL_UP_TITLE = Pattern.compile(".\\s*Level-Up\\s*.");
    private static final Pattern PATTERN_LEVEL_UP_SUBTITLE = Pattern.compile("You are now Level (?<level>.+)");

    // Action bar patterns
    private static final Pattern PATTERN_ACTION_BAR = Pattern.compile("^Player ID:\\s+(?<playerId>[\\d,]+)\\s+.\\s+PlayTime:\\s+(?<playSeconds>[\\d]+s) (?<playMinutes>[\\d]+m) (?<playHours>[\\d]+h)\\s+.\\s+Quest:\\s+(?<questProgress>\\d+)\\/(?<questRequirement>\\d+)$");

    private PKSimUtils() { }

    public static boolean isPKSimulator(HypixelScoreboard scoreboard) {
        return scoreboard.getMaxLine() >= SCOREBOARD_LINE_IDENTIFIER && SCOREBOARD_IDENTIFIER.equals(scoreboard.getLine(SCOREBOARD_LINE_IDENTIFIER));
    }

    public static int extractLevel(HypixelScoreboard scoreboard) {
        return extractScoreboardIntegerValue(scoreboard, SCOREBOARD_LINE_LEVEL, PATTERN_LEVEL, "level");
    }

    @Nullable
    public static IntIntPair extractExperience(HypixelScoreboard scoreboard) {
        Matcher matcher = extractScoreboardMatcher(scoreboard, SCOREBOARD_LINE_EXPERIENCE, PATTERN_EXPERIENCE_SCOREBOARD);

        if (matcher != null && matcher.matches()) {
            int currentExperience = extractNamedIntegerFromMatcher(matcher, "currentExperience");
            int requiredExperience = extractNamedIntegerFromMatcher(matcher, "requiredExperience");
            return IntIntPair.of(currentExperience, requiredExperience);
        }

        return null;
    }

    @Nullable
    public static IntIntPair extractExperienceTitle(String string) {
        Matcher matcher = PATTERN_EXPERIENCE_TITLE.matcher(string);

        if (matcher != null && matcher.matches()) {
            int currentExperience = extractNamedIntegerFromMatcher(matcher, "currentExperience");
            int requiredExperience = extractNamedIntegerFromMatcher(matcher, "requiredExperience");
            return IntIntPair.of(currentExperience, requiredExperience);
        }

        return null;
    }

    public static int extractPrestige(HypixelScoreboard scoreboard) {
        return extractScoreboardIntegerValue(scoreboard, SCOREBOARD_LINE_PRESTIGE, PATTERN_PRESTIGE, "prestige");
    }

    public static int extractGems(HypixelScoreboard scoreboard) {
        return extractScoreboardIntegerValue(scoreboard, SCOREBOARD_LINE_GEMS, PATTERN_GEMS, "gems");
    }

    public static int extractCoins(HypixelScoreboard scoreboard) {
        return extractScoreboardIntegerValue(scoreboard, SCOREBOARD_LINE_COINS, PATTERN_COINS_SCOREBOARD, "coins");
    }

    public static int extractCoinsTitle(String string) {
        return extractPatternIntegerValue(PATTERN_COINS_SUBTITLE, string, "coins");
    }

    public static boolean isLevelUpTitle(String string) {
        return PATTERN_LEVEL_UP_TITLE.matcher(string).matches();
    }

    public static int extractLevelTitle(String string) {
        return extractPatternIntegerValue(PATTERN_LEVEL_UP_SUBTITLE, string, "level");
    }

    public static ActionBarData extractActionBarData(String text) {
        Matcher matcher = PATTERN_ACTION_BAR.matcher(text);
        if (!matcher.matches()) {
            return null;
        }

        int playerId = extractNamedIntegerFromMatcher(matcher, "playerId");
        int playSeconds = extractNamedIntegerFromMatcher(matcher, "playSeconds");
        int playMinutes = extractNamedIntegerFromMatcher(matcher, "playMinutes");
        int playHours = extractNamedIntegerFromMatcher(matcher, "playHours");
        int questProgress = extractNamedIntegerFromMatcher(matcher, "questProgress");
        int questRequirement = extractNamedIntegerFromMatcher(matcher, "questRequirement");

        Duration playTime = Duration.ofSeconds(playSeconds + TimeUnit.MINUTES.toSeconds(playMinutes) + TimeUnit.HOURS.toSeconds(playHours));
        return new ActionBarData(playerId, playTime, questProgress, questRequirement);
    }

    private static Matcher extractScoreboardMatcher(HypixelScoreboard scoreboard, int lineNumber, Pattern pattern) {
        if (scoreboard.getMaxLine() < lineNumber) {
            return null;
        }

        String line = ChatFormatting.stripFormatting(scoreboard.getLine(lineNumber));
        return line != null ? pattern.matcher(line) : null;
    }

    private static int extractScoreboardIntegerValue(HypixelScoreboard scoreboard, int lineNumber, Pattern pattern, String groupName) {
        Matcher matcher = extractScoreboardMatcher(scoreboard, lineNumber, pattern);
        return (matcher != null && matcher.find()) ? extractNamedIntegerFromMatcher(matcher, groupName) : -1;
    }

    private static int extractPatternIntegerValue(Pattern pattern, String entry, String groupName) {
        Matcher matcher = pattern.matcher(entry);
        return matcher.find() ? extractNamedIntegerFromMatcher(matcher, groupName) : -1;
    }

    private static int extractNamedIntegerFromMatcher(Matcher matcher, String groupName) {
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

    public static int calculateRequiredExperienceForLevel(int level) {
        return level * 75;
    }

}
