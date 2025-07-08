package wtf.choco.meh.client.fishing;

import java.util.Set;

public final class QuantifiedTreasure implements FishingCatch {

    private final QuantifiedTreasureType type;
    private final int quantity;

    public QuantifiedTreasure(QuantifiedTreasureType type, int quantity) {
        this.type = type;
        this.quantity = quantity;
    }

    @Override
    public CatchType getType() {
        return CatchType.TREASURE;
    }

    @Override
    public String getSimpleName() {
        return type.getSimpleName();
    }

    @Override
    public Set<FishingEnvironment> getEnvironments() {
        return type.getEnvironments();
    }

    @Override
    public String getDescriptionKey() {
        return type.getDescriptionKey();
    }

    public int getQuantity() {
        return quantity;
    }

}
