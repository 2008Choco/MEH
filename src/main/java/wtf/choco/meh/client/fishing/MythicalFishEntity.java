package wtf.choco.meh.client.fishing;

import java.lang.ref.Reference;

import net.minecraft.world.entity.decoration.ArmorStand;

import wtf.choco.meh.client.event.HypixelServerEvents;

public final class MythicalFishEntity {

    private static final double MAX_HEAT = 100.0D;

    private int health;
    private int ticksSinceLastClicked = 0;
    private double heat = 0;
    private boolean enraged = true; // The fish starts enraged every time

    private final Reference<ArmorStand> armorStand;
    private final MythicalFishType type;

    public MythicalFishEntity(Reference<ArmorStand> armorStand, MythicalFishType type) {
        this.armorStand = armorStand;
        this.type = type;
        this.health = type.getMaxHealth();
    }

    public Reference<ArmorStand> getArmorStand() {
        return armorStand;
    }

    public MythicalFishType getType() {
        return type;
    }

    public int getHealth() {
        return health;
    }

    public int getTicksSinceLastClicked() {
        return ticksSinceLastClicked;
    }

    public double getHeat() {
        return heat;
    }

    void setEnraged(boolean enraged) {
        this.enraged = enraged;
    }

    public boolean isEnraged() {
        return enraged;
    }

    public void tick() {
        this.ticksSinceLastClicked++;

        double newHeat = Math.max(heat - 0.75, 0.0);
        if (heat != newHeat) {
            HypixelServerEvents.FISHING_MYTHICAL_FISH_HEAT_CHANGE.invoker().onHeatChange(this, newHeat);
            this.heat = newHeat;
        }
    }

    public void click() {
        this.ticksSinceLastClicked = 0;

        if (health > 0) {
            HypixelServerEvents.FISHING_MYTHICAL_FISH_HEALTH_CHANGE.invoker().onHealthChange(this, health - 1);
            this.health--;
        }

        if (enraged) {
            double newHeat = Math.min(heat + 10, MAX_HEAT);
            if (heat != newHeat) {
                HypixelServerEvents.FISHING_MYTHICAL_FISH_HEAT_CHANGE.invoker().onHeatChange(this, newHeat);
                this.heat = newHeat;
            }
        }
    }

}
