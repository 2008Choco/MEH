package wtf.choco.meh.client.fishing;

import com.google.common.collect.ImmutableSet;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

public enum Junk implements FishingCatch {

    BOWL("bowl", FishingEnvironment.WATER),
    LEATHER("leather", FishingEnvironment.WATER),
    LEATHER_BOOTS("leather boots", FishingEnvironment.WATER),
    ROTTEN_FLESH("rotten flesh", FishingEnvironment.WATER),
    STICK("stick", FishingEnvironment.WATER),
    STRING("string", FishingEnvironment.WATER),
    BONE("bone", FishingEnvironment.WATER),
    INK_SAC("ink sac", FishingEnvironment.WATER),
    LILY_PAD("lily pad", FishingEnvironment.WATER),
    WATER_BOTTLE("water bottle", FishingEnvironment.WATER),
    BROKEN_FISHING_ROD("broken fishing rod", FishingEnvironment.WATER),
    SOGGY_PIECE_OF_PAPER("soggy piece of paper", FishingEnvironment.WATER),
    COAL("coal", FishingEnvironment.LAVA),
    CHARCOAL("charcoal", FishingEnvironment.LAVA),
    NETHER_BRICK("nether brick", FishingEnvironment.LAVA),
    STEAK("steak", FishingEnvironment.LAVA),
    BURNED_FLESH("burned flesh", FishingEnvironment.LAVA),
    FERMENTED_SPIDER_EYE("fermented spider eye", FishingEnvironment.LAVA),
    LAVA_BUCKET("lava bucket", FishingEnvironment.LAVA),
    SNOWBALL("snowball", FishingEnvironment.ICE),
    ICE_SHARD("ice shard", FishingEnvironment.ICE),
    CLUMP_OF_LEAVES("clump of leaves", FishingEnvironment.ICE),
    FROZEN_FLESH("frozen flesh", FishingEnvironment.ICE);

    private static final Map<String, Junk> BY_NAME = new HashMap<>();

    static {
        for (Junk junk : values()) {
            BY_NAME.put(junk.getSimpleName().toLowerCase(), junk);
        }
    }

    private String descriptionKey;

    private final String simpleName;
    private final Set<FishingEnvironment> environment;

    private Junk(String simpleName, FishingEnvironment environment) {
        this.simpleName = simpleName;
        this.environment = ImmutableSet.copyOf(EnumSet.of(environment));
    }

    @Override
    public CatchType getType() {
        return CatchType.JUNK;
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
            this.descriptionKey = "meh.hypixel.fishing.junk." + name().toLowerCase();
        }

        return descriptionKey;
    }

    @Nullable
    public static Junk getByName(String name) {
        return BY_NAME.get(name.toLowerCase());
    }

}
