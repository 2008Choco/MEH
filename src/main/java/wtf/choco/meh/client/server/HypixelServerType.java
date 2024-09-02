package wtf.choco.meh.client.server;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.network.chat.Component;

public enum HypixelServerType {

    MAIN_LOBBY("HYPIXEL"),
    ARCADE("ARCADE GAMES"),
    BEDWARS("BED WARS"),
    BLITZ_SURVIVAL_GAMES("BLITZ SG"),
    BUILD_BATTLE("BUILD BATTLE"),
    CLASSIC_GAMES("CLASSIC GAMES"),
    COPS_AND_CRIMS("COPS AND CRIMS"),
    DUELS("DUELS"),
    HOUSING("HOUSING"),
    MEGA_WALLS("MEGA WALLS"),
    MURDER_MYSTERY("MURDER MYSTERY"),
    PIT("THE HYPIXEL PIT"),
    PROTOTYPE("PROTOTYPE"),
    SKYBLOCK("SKYBLOCK"),
    SKYWARS("SKYWARS"),
    SMASH_HEROES("SMASH HEROES"),
    SMP, // No scoreboard
    TNT_GAMES("THE TNT GAMES"),
    UHC("UHC CHAMPIONS"),
    WARLORDS("WARLORDS"),
    WOOL_GAMES("WOOL GAMES"),
    UNKNOWN;

    private static final Map<String, HypixelServerType> BY_SCOREBOARD_TITLE = new HashMap<>();

    static {
        for (HypixelServerType type : values()) {
            if (type.scoreboardTitle == null) {
                continue;
            }

            BY_SCOREBOARD_TITLE.put(type.scoreboardTitle.toUpperCase(), type);
        }
    }

    private String translationKey;
    private final String scoreboardTitle;

    private HypixelServerType(String scoreboardTitle) {
        this.scoreboardTitle = scoreboardTitle;
    }

    @Deprecated
    private HypixelServerType() {
        this(null);
    }

    public String getScoreboardTitle() {
        return scoreboardTitle;
    }

    private String getOrCreateTranslationKey() {
        if (translationKey == null) {
            this.translationKey = "meh.hypixel.server." + name().toLowerCase();
        }

        return translationKey;
    }

    public String getTranslationKey() {
        return getOrCreateTranslationKey();
    }

    public Component getDisplayName() {
        return Component.translatable(getTranslationKey());
    }

    public static HypixelServerType getByScoreboardTitle(String scoreboardTitle) {
        return scoreboardTitle != null ? BY_SCOREBOARD_TITLE.getOrDefault(scoreboardTitle.toUpperCase(), UNKNOWN) : UNKNOWN;
    }

}
