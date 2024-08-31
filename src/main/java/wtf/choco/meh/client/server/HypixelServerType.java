package wtf.choco.meh.client.server;

import java.util.HashMap;
import java.util.Map;

public enum HypixelServerType {

    // TODO: Define scoreboard titles
    MAIN_LOBBY,
    ARCADE,
    BEDWARS,
    BLITZ_SURVIVAL_GAMES,
    BUILD_BATTLE,
    CLASSIC_GAMES,
    COPS_AND_CRIMS,
    DUELS,
    HOUSING,
    MEGA_WALLS,
    MURDER_MYSTERY,
    PIT,
    PROTOTYPE,
    SKYBLOCK,
    SKYWARS,
    SMASH_HEROS,
    SMP,
    TNT_GAMES,
    UHC,
    WARLORDS,
    WOOL_GAMES,
    UNKNOWN;

    private static final Map<String, HypixelServerType> BY_SCOREBOARD_TITLE = new HashMap<>();

    static {
        for (HypixelServerType type : values()) {
            BY_SCOREBOARD_TITLE.put(type.scoreboardTitle.toUpperCase(), type);
        }
    }

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

    public static HypixelServerType getByScoreboardTitle(String scoreboardTitle) {
        return BY_SCOREBOARD_TITLE.getOrDefault(scoreboardTitle.toUpperCase(), UNKNOWN);
    }

}
