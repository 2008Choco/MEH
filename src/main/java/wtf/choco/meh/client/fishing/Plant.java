package wtf.choco.meh.client.fishing;

import com.google.common.collect.ImmutableSet;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

public enum Plant implements FishingCatch {

    KELP("kelp", FishingEnvironment.WATER),
    WHEAT("wheat", FishingEnvironment.WATER),
    POTATO("potato", FishingEnvironment.WATER),
    MELON("melon", FishingEnvironment.WATER),
    SWEET_BERRIES("sweet berries", FishingEnvironment.WATER),
    GLOW_BERRIES("glow berries", FishingEnvironment.WATER),
    BAMBOO("bamboo", FishingEnvironment.WATER),
    DRIED_KELP("dried kelp", FishingEnvironment.LAVA),
    BAKED_POTATO("baked potato", FishingEnvironment.LAVA),
    GLISTERING_MELON("glistering melon", FishingEnvironment.LAVA),
    CHARRED_BERRIES("charred berries", FishingEnvironment.LAVA),
    NETHER_WART("nether wart", FishingEnvironment.LAVA),
    WARPED_ROOTS("warped roots", FishingEnvironment.LAVA),
    FROZEN_KELP("frozen kelp", FishingEnvironment.ICE);

    private static final Map<String, Plant> BY_NAME = new HashMap<>();

    static {
        for (Plant plant : values()) {
            BY_NAME.put(plant.getSimpleName().toLowerCase(), plant);
        }
    }

    private String descriptionKey;

    private final String simpleName;
    private final Set<FishingEnvironment> environment;

    private Plant(String simpleName, FishingEnvironment environment) {
        this.simpleName = simpleName;
        this.environment = ImmutableSet.copyOf(EnumSet.of(environment));
    }

    @Override
    public CatchType getType() {
        return CatchType.PLANTS;
    }

    @Override
    public String getSimpleName() {
        return simpleName;
    }

    @Override
    public Set<FishingEnvironment> getEnvironments() {
        return environment;
    }

    @Override
    public String getDescriptionKey() {
        if (descriptionKey == null) {
            this.descriptionKey = "meh.hypixel.fishing.plant." + name().toLowerCase();
        }

        return descriptionKey;
    }

    @Nullable
    public static FishingCatch getByName(String name) {
        return BY_NAME.get(name.toLowerCase());
    }

}
