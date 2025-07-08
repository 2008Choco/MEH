package wtf.choco.meh.client.fishing;

import java.util.Objects;
import java.util.Set;

import net.minecraft.ChatFormatting;

public final class MythicalFish implements FishingCatch {

    private final MythicalFishType type;
    private final int weight;

    public MythicalFish(MythicalFishType type, int weight) {
        this.type = type;
        this.weight = weight;
    }

    @Override
    public CatchType getType() {
        return CatchType.MYTHICAL_FISH;
    }

    @Override
    public String getSimpleName() {
        return type.getName();
    }

    @Override
    public Set<FishingEnvironment> getEnvironments() {
        return type.getEnvironments();
    }

    @Override
    public ChatFormatting getColor() {
        return type.getRarity().getColor();
    }

    @Override
    public String getDescriptionKey() {
        return type.getDescriptionKey();
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, weight);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof MythicalFish other && type == other.type && weight == other.weight);
    }

}
