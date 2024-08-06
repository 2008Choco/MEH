package wtf.choco.meh.client.pksim.notification;

import org.spongepowered.include.com.google.common.base.Preconditions;

public final class ParkourCompletionMatchResult implements MatchResult {

    private static final ParkourCompletionMatchResult NON_MATCHING = new ParkourCompletionMatchResult();

    private final boolean matches;
    private final int currentExperience;
    private final int requiredExperience;
    private final int coins;

    private ParkourCompletionMatchResult(boolean matches, int currentExperience, int requiredExperience, int coins) {
        this.matches = matches;
        this.currentExperience = currentExperience;
        this.requiredExperience = requiredExperience;
        this.coins = coins;
    }

    public ParkourCompletionMatchResult(int currentExperience, int requiredExperience, int coins) {
        this(true, currentExperience, requiredExperience, coins);
    }

    private ParkourCompletionMatchResult() {
        this(false, -1, -1, -1);
    }

    @Override
    public boolean matches() {
        return matches;
    }

    public int getCurrentExperience() {
        Preconditions.checkState(matches, "Does not match! Cannot extract data!");
        return currentExperience;
    }

    public int getRequiredExperience() {
        Preconditions.checkState(matches, "Does not match! Cannot extract data!");
        return requiredExperience;
    }

    public int getCoins() {
        Preconditions.checkState(matches, "Does not match! Cannot extract data!");
        return coins;
    }

    static ParkourCompletionMatchResult nonMatching() {
        return NON_MATCHING;
    }

}
