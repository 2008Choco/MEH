package wtf.choco.meh.client.fishing;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.ChatFormatting;

import org.jetbrains.annotations.Nullable;

public enum MythicalFishType {

    EMBER_OF_HELIOS("Ember of Helios", FishRarity.COMMON),
    DUST_OF_SELENE("Dust of Selene", FishRarity.COMMON),
    SHADOW_OF_NYX("Shadow of Nyx", FishRarity.UNCOMMON),
    HEART_OF_APHRODITE("Heart of Aphrodite", FishRarity.UNCOMMON),
    SPARK_OF_ZEUS("Spark of Zeus", FishRarity.RARE),
    SPIRIT_OF_DEMETER("Spirit of Demeter", FishRarity.RARE),
    AUTOMATON_OF_DAEDALUS("Automaton of Daedalus", FishRarity.ULTRA_RARE, FishingEnvironment.WATER),
    WRATH_OF_HADES("Wrath of Hades", FishRarity.ULTRA_RARE, FishingEnvironment.LAVA);

    private static final Map<String, MythicalFishType> BY_NAME = new HashMap<>();

    static {
        for (MythicalFishType type : MythicalFishType.values()) {
            BY_NAME.put(type.getName().toLowerCase(), type);
        }
    }

    private final String name;
    private final FishRarity rarity;
    private final Set<FishingEnvironment> environments;

    private MythicalFishType(String name, FishRarity rarity, FishingEnvironment environment, FishingEnvironment... additionalEnvironments) {
        this.name = name;
        this.rarity = rarity;
        this.environments = EnumSet.of(environment, additionalEnvironments);
    }

    private MythicalFishType(String name, FishRarity rarity) {
        this.name = name;
        this.rarity = rarity;
        this.environments = EnumSet.allOf(FishingEnvironment.class);
    }

    public String getName() {
        return name;
    }

    public FishRarity getRarity() {
        return rarity;
    }

    public ChatFormatting getColor() {
        return rarity.getColor();
    }

    public Set<FishingEnvironment> getEnvironments() {
        return environments;
    }

    @Nullable
    public static MythicalFishType getByName(String name) {
        return BY_NAME.get(name.toLowerCase());
    }

}
