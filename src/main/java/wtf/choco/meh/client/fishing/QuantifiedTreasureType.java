package wtf.choco.meh.client.fishing;

import com.google.common.collect.ImmutableSet;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import wtf.choco.meh.client.util.Translatable;

public enum QuantifiedTreasureType implements Translatable {

    // TODO: All variants of game coins
    HYPIXEL_EXPERIENCE("Hypixel Experience"),
    GUILD_EXPERIENCE("Guild Experience"),
    EVENT_EXPERIENCE("Event Experience"),

    /**
     * @deprecated This shouldn't exist. Ideally this gets removed once we know all the variants of game coin rewards!
     */
    @Deprecated
    UNKNOWN("unknown");

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
        return BY_NAME.getOrDefault(name.toLowerCase(), QuantifiedTreasureType.UNKNOWN);
    }

}
