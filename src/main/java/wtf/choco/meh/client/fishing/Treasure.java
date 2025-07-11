package wtf.choco.meh.client.fishing;

import com.google.common.collect.ImmutableSet;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

public enum Treasure implements FishingCatch {

    ENCHANTED_BOOK("enchanted book", FishingEnvironment.WATER, FishingEnvironment.ICE),
    NAME_TAG("name tag", FishingEnvironment.WATER),
    SADDLE("saddle", FishingEnvironment.WATER),
    ENCHANTED_FISHING_ROD("enchanted fishing rod", FishingEnvironment.WATER, FishingEnvironment.ICE),
    DIAMOND("diamond", FishingEnvironment.WATER, FishingEnvironment.ICE),
    DIAMOND_SWORD("diamond sword", FishingEnvironment.WATER, FishingEnvironment.ICE),
    ENCHANTED_BOW("enchanted bow", FishingEnvironment.WATER, FishingEnvironment.ICE),
    GOLD_PICKAXE("gold pickaxe", FishingEnvironment.WATER),
    COMPASS("compass", FishingEnvironment.WATER),
    EMERALD("emerald", FishingEnvironment.WATER),
    NAUTILUS_SHELL("nautilus shell", FishingEnvironment.WATER),
    BLAZE_POWDER("blaze powder", FishingEnvironment.LAVA),
    MAGMA_CREAM("magma cream", FishingEnvironment.LAVA),
    MOLTEN_GOLD("molten gold", FishingEnvironment.LAVA),
    BLAZE_ROD("blaze rod", FishingEnvironment.LAVA),
    EYE_OF_ENDER("eye of ender", FishingEnvironment.LAVA),
    CHAINMAIL_CHESTPLATE("chainmail chestplate", FishingEnvironment.LAVA),
    GOLD_SWORD("gold sword", FishingEnvironment.LAVA),
    IRON_SWORD("iron sword", FishingEnvironment.ICE);

    private static final Map<String, Treasure> BY_NAME = new HashMap<>();

    static {
        for (Treasure treasure : values()) {
            BY_NAME.put(treasure.getSimpleName().toLowerCase(), treasure);
        }
    }

    private String descriptionKey;

    private final String simpleName;
    private final Set<FishingEnvironment> environments;

    private Treasure(String simpleName, FishingEnvironment environment, FishingEnvironment... additionalEnvironments) {
        this.simpleName = simpleName;
        this.environments = ImmutableSet.copyOf(EnumSet.of(environment, additionalEnvironments));
    }

    private Treasure(String simpleName, FishingEnvironment environment) {
        this.simpleName = simpleName;
        this.environments = ImmutableSet.copyOf(EnumSet.of(environment));
    }

    @Override
    public CatchType getType() {
        return CatchType.TREASURE;
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
            this.descriptionKey = "meh.hypixel.fishing.treasure_type." + name().toLowerCase();
        }

        return descriptionKey;
    }

    @Nullable
    public static Treasure getByName(String name) {
        return BY_NAME.get(name.toLowerCase());
    }

}
