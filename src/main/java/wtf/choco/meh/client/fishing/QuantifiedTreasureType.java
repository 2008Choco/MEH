package wtf.choco.meh.client.fishing;

import com.google.common.collect.ImmutableSet;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import wtf.choco.meh.client.util.Translatable;

public enum QuantifiedTreasureType implements Translatable {

    ARCADE_GAMES_COINS("Arcade Games Coins"),
    ARENA_BRAWL_COINS("Arena Brawl Coins"),
    BLITZ_SG_COINS("Blitz SG Coins"),
    COPS_AND_CRIMS_COINS("Cops and Crims Coins"),
    MEGA_WALLS_COINS("Mega Walls Coins"),
    PAINTBALL_WARFARE_COINS("Paintball Warfare Coins"),
    QUAKECRAFT_COINS("Quakecraft Coins"),
    SKYWARS_COINS("SkyWars Coins"),
    SMASH_HEROES_COINS("Smash Heroes Coins"),
    TURBO_KART_RACERS_COINS("Turbo Kart Racers Coins"),
    UHC_CHAMPIONS_COINS("UHC Champions Coins"),
    VAMPIREZ_COINS("VampireZ Coins"),
    WALLS_COINS("The Walls Coins"),
    WARLORDS_COINS("Warlords Coins"),

    BED_WARS_TOKENS("Bed Wars Tokens"),
    BUILD_BATTLE_TOKENS("Build Battle Tokens"),
    DUELS_TOKENS("Duels Tokens"),
    MURDER_MYSTERY_TOKENS("Murder Mystery Tokens"),
    TNT_GAMES_TOKENS("The TNT Games Tokens"),

    HYPIXEL_EXPERIENCE("Hypixel Experience"),
    GUILD_EXPERIENCE("Guild Experience"),
    EVENT_EXPERIENCE("Event Experience");

    private static final Map<String, QuantifiedTreasureType> BY_NAME = new HashMap<>();

    static {
        for (QuantifiedTreasureType type : QuantifiedTreasureType.values()) {
            BY_NAME.put(type.getSimpleName().toLowerCase(), type);
        }
    }

    private String descriptionKey;

    private final String simpleName;
    private final Set<FishingEnvironment> environments = ImmutableSet.copyOf(EnumSet.allOf(FishingEnvironment.class));

    private QuantifiedTreasureType(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public Set<FishingEnvironment> getEnvironments() {
        return environments;
    }

    @Override
    public String getDescriptionKey() {
        if (descriptionKey == null) {
            this.descriptionKey = "meh.hypixel.fishing.treasure_type." + name().toLowerCase();
        }

        return descriptionKey;
    }

    public static QuantifiedTreasureType getByName(String name) {
        return BY_NAME.get(name.toLowerCase());
    }

}
