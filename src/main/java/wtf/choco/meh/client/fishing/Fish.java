package wtf.choco.meh.client.fishing;

import com.google.common.collect.ImmutableSet;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

public enum Fish implements FishingCatch {

    COD("cod", FishingEnvironment.WATER),
    SALMON("salmon", FishingEnvironment.WATER),
    CLOWNFISH("clownfish", FishingEnvironment.WATER),
    PUFFERFISH("pufferfish", FishingEnvironment.WATER),
    COOKED_COD("cooked cod", FishingEnvironment.LAVA),
    COOKED_SALMON("cooked salmon", FishingEnvironment.LAVA),
    CHARRED_PUFFERFISH("charred pufferfish", FishingEnvironment.LAVA),
    TROUT("trout", FishingEnvironment.ICE),
    PERCH("perch", FishingEnvironment.ICE),
    PIKE("pike", FishingEnvironment.ICE),
    SECRET_FISH("secret fish", FishingEnvironment.WATER, FishingEnvironment.LAVA, FishingEnvironment.ICE);

    private static final Map<String, Fish> BY_NAME = new HashMap<>();

    static {
        for (Fish fish : values()) {
            BY_NAME.put(fish.getSimpleName().toLowerCase(), fish);
        }
    }

    private String descriptionKey;

    private final String simpleName;
    private final Set<FishingEnvironment> environments;

    private Fish(String simpleName, FishingEnvironment environment, FishingEnvironment... additionalEnvironments) {
        this.simpleName = simpleName;
        this.environments = ImmutableSet.copyOf(EnumSet.of(environment, additionalEnvironments));
    }

    private Fish(String simpleName, FishingEnvironment environment) {
        this.simpleName = simpleName;
        this.environments = ImmutableSet.copyOf(EnumSet.of(environment));
    }

    @Override
    public CatchType getType() {
        return CatchType.FISH;
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
            this.descriptionKey = "meh.hypixel.fishing.fish." + name().toLowerCase();
        }

        return descriptionKey;
    }

    @Nullable
    public static Fish getByName(String name) {
        return BY_NAME.get(name.toLowerCase());
    }

}
