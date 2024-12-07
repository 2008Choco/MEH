package wtf.choco.meh.client.server;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

/**
 * A type of server on the Hypixel network.
 */
public enum HypixelServerType implements StringRepresentable {

    MAIN_LOBBY("HYPIXEL"),
    TOURNAMENT_LOBBY("TOURNAMENT HALL"),
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
    PAINTBALL("PAINTBALL"), // TODO: Verify
    PIT("THE HYPIXEL PIT"),
    PROTOTYPE("PROTOTYPE"),
    QUAKECRAFT("QUAKECRAFT"), // TODO: Verify
    REPLAY("REPLAY"),
    SKYBLOCK("SKYBLOCK"),
    SKYWARS("SKYWARS"),
    SMASH_HEROES("SMASH HEROES"),
    SMP, // No scoreboard
    SPEED_UHC("SPEED UHC"), // TODO: Verify
    TNT_GAMES("THE TNT GAMES"),
    TURBO_KART_RACERS("TURBO KART RACERS"), // TODO: Verify
    UHC("UHC CHAMPIONS"),
    VAMPIREZ("VAMPIREZ"), // TODO: Verify
    WALLS("WALLS"), // TODO: Verify
    WARLORDS("WARLORDS"),
    WOOL_GAMES("WOOL GAMES"),
    UNKNOWN;

    public static final Codec<HypixelServerType> CODEC = StringRepresentable.fromEnum(HypixelServerType::values);

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

    /**
     * Get the (unformatted) title used on the scoreboard of this server type.
     *
     * @return the scoreboard title
     *
     * @throws IllegalStateException if this server type does not have a scoreboard title
     */
    public String getScoreboardTitle() {
        Preconditions.checkState(scoreboardTitle != null, "This server type does not have a scoreboard title!");
        return scoreboardTitle;
    }

    private String getOrCreateTranslationKey() {
        if (translationKey == null) {
            this.translationKey = "meh.hypixel.server." + name().toLowerCase();
        }

        return translationKey;
    }

    /**
     * Get the translation key for this server type's display name.
     *
     * @return the translation key
     */
    public String getDescriptionKey() {
        return getOrCreateTranslationKey();
    }

    /**
     * Get the display name for this server type.
     *
     * @return the display name
     */
    public Component getDisplayName() {
        return Component.translatable(getDescriptionKey());
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }

    /**
     * Get a {@link HypixelServerType} from a scoreboard title. The passed scoreboard title is case
     * insensitive, but is sensitive to formatting. The scoreboard title should be unformatted.
     *
     * @param scoreboardTitle the scoreboard title
     *
     * @return the associated server type, or {@link #UNKNOWN} if unknown
     */
    public static HypixelServerType getByScoreboardTitle(String scoreboardTitle) {
        return scoreboardTitle != null ? BY_SCOREBOARD_TITLE.getOrDefault(scoreboardTitle.toUpperCase(), UNKNOWN) : UNKNOWN;
    }

}
