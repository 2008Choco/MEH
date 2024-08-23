package wtf.choco.meh.client.pksim.potion;

public final class PotionBuff {

    private int remainingTicks = 0;

    public void setRemainingTicks(int remainingTicks) {
        this.remainingTicks = remainingTicks;
    }

    public void addRemainingTicks(int ticks) {
        this.remainingTicks += ticks;
    }

    public int getRemainingTicks() {
        return remainingTicks;
    }

    public void tick() {
        if (isActive()) {
            this.remainingTicks--;
        }
    }

    public boolean isActive() {
        return remainingTicks > 0;
    }

}
