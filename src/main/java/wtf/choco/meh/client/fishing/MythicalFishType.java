package wtf.choco.meh.client.fishing;

import com.google.common.collect.ImmutableSet;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import wtf.choco.meh.client.util.Translatable;

public enum MythicalFishType implements Translatable {

    EMBER_OF_HELIOS("Ember of Helios", 25, FishRarity.COMMON),
    DUST_OF_SELENE("Dust of Selene", 25, FishRarity.COMMON),
    SHADOW_OF_NYX("Shadow of Nyx", 38, FishRarity.UNCOMMON),
    HEART_OF_APHRODITE("Heart of Aphrodite", 38, FishRarity.UNCOMMON),
    SPARK_OF_ZEUS("Spark of Zeus", 50, FishRarity.RARE),
    SPIRIT_OF_DEMETER("Spirit of Demeter", 50, FishRarity.RARE),
    AUTOMATON_OF_DAEDALUS("Automaton of Daedalus", 63, FishRarity.ULTRA_RARE, FishingEnvironment.WATER),
    WRATH_OF_HADES("Wrath of Hades", 63, FishRarity.ULTRA_RARE, FishingEnvironment.LAVA);

    private static final Map<String, MythicalFishType> BY_NAME = new HashMap<>();

    static {
        for (MythicalFishType type : MythicalFishType.values()) {
            BY_NAME.put(type.getName().toLowerCase(), type);
        }
    }

    private String descriptionKey;

    private final String name;
    private final int maxHealth;
    private final FishRarity rarity;
    private final Set<FishingEnvironment> environments;

    private MythicalFishType(String name, int maxHealth, FishRarity rarity, Set<FishingEnvironment> environments) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.rarity = rarity;
        this.environments = ImmutableSet.copyOf(environments);
    }

    private MythicalFishType(String name, int maxHealth, FishRarity rarity, FishingEnvironment environment, FishingEnvironment... additionalEnvironments) {
        this(name, maxHealth, rarity, EnumSet.of(environment, additionalEnvironments));
    }

    private MythicalFishType(String name, int maxHealth, FishRarity rarity) {
        this(name, maxHealth, rarity, EnumSet.allOf(FishingEnvironment.class));
    }

    public String getName() {
        return name;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public FishRarity getRarity() {
        return rarity;
    }

    public Set<FishingEnvironment> getEnvironments() {
        return environments;
    }

    @Override
    public String getDescriptionKey() {
        if (descriptionKey == null) {
            this.descriptionKey = "meh.hypixel.fishing.mythical_fish." + name().toLowerCase();
        }

        return descriptionKey;
    }

    @Nullable
    public static MythicalFishType getByName(String name) {
        return BY_NAME.get(name.toLowerCase());
    }

}
