package wtf.choco.meh.client.pksim.notification;

import wtf.choco.meh.client.pksim.potion.PotionBuffType;

public final class PotionBuffMatchResult implements MatchResult {

    private static final PotionBuffMatchResult NON_MATCHING = new PotionBuffMatchResult();

    private final boolean matches;
    private final PotionBuffType type;
    private final int durationSeconds;

    private PotionBuffMatchResult(boolean matches, PotionBuffType type, int durationSeconds) {
        this.matches = matches;
        this.type = type;
        this.durationSeconds = durationSeconds;
    }

    public PotionBuffMatchResult(PotionBuffType type, int durationSeconds) {
        this(true, type, durationSeconds);
    }

    private PotionBuffMatchResult() {
        this(false, null, -1);
    }

    @Override
    public boolean matches() {
        return matches;
    }

    public PotionBuffType getType() {
        return type;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    static PotionBuffMatchResult nonMatching() {
        return NON_MATCHING;
    }

}
