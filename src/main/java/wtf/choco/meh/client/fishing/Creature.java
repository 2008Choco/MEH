package wtf.choco.meh.client.fishing;

import com.google.common.collect.ImmutableSet;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

public enum Creature implements FishingCatch {

    SHEEP("Sheep", FishingEnvironment.WATER, FishingEnvironment.ICE),
    CHICKEN("Chicken", FishingEnvironment.WATER, FishingEnvironment.ICE),
    PIG("Pig", FishingEnvironment.WATER, FishingEnvironment.ICE),
    COW("Cow", FishingEnvironment.WATER, FishingEnvironment.ICE),
    CREEPER("Creeper", FishingEnvironment.WATER, FishingEnvironment.LAVA),
    ZOMBIE("Zombie", FishingEnvironment.WATER, FishingEnvironment.LAVA),
    SKELETON("Skeleton", FishingEnvironment.WATER, FishingEnvironment.LAVA),
    SPIDER("Spider", FishingEnvironment.WATER, FishingEnvironment.LAVA),
    PIG_ZOMBIE("Pig Zombie", FishingEnvironment.LAVA),
    BLAZE("Blaze", FishingEnvironment.LAVA),
    MAGMA_CUBE("Magma Cube", FishingEnvironment.LAVA),
    CAVE_SPIDER("Cave Spider", FishingEnvironment.LAVA, FishingEnvironment.ICE),
    SLIME("Slime", FishingEnvironment.WATER, FishingEnvironment.ICE),
    SQUID("Squid", FishingEnvironment.WATER);

    private static final Map<String, Creature> BY_NAME = new HashMap<>();

    static {
        for (Creature creature : values()) {
            BY_NAME.put(creature.getSimpleName().toLowerCase(), creature);
        }
    }

    private String descriptionKey;

    private final String simpleName;
    private final Set<FishingEnvironment> environments;

    private Creature(String simpleName, FishingEnvironment environment, FishingEnvironment... additionalEnvironments) {
        this.simpleName = simpleName;
        this.environments = ImmutableSet.copyOf(EnumSet.of(environment, additionalEnvironments));
    }

    private Creature(String simpleName, FishingEnvironment environment) {
        this.simpleName = simpleName;
        this.environments = ImmutableSet.copyOf(EnumSet.of(environment));
    }

    @Override
    public CatchType getType() {
        return CatchType.CREATURES;
    }

    @Override
    public String getSimpleName() {
        return simpleName;
    }

    @Override
    public Set<FishingEnvironment> getEnvironments() {
        return environments;
    }

    @Override
    public String getDescriptionKey() {
        if (descriptionKey == null) {
            this.descriptionKey = "meh.hypixel.fishing.creature." + name().toLowerCase();
        }

        return descriptionKey;
    }

    @Nullable
    public static FishingCatch getByName(String name) {
        return BY_NAME.get(name.toLowerCase());
    }

}
