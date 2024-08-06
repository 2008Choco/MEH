package wtf.choco.meh.client.pksim.notification;

import org.spongepowered.include.com.google.common.base.Preconditions;

public final class LevelUpMatchResult implements MatchResult {

    private static final LevelUpMatchResult NON_MATCHING = new LevelUpMatchResult();

    private final boolean matches;
    private final int level;

    private LevelUpMatchResult(boolean matches, int level) {
        this.matches = matches;
        this.level = level;
    }

    public LevelUpMatchResult(int level) {
        this(true, level);
    }

    private LevelUpMatchResult() {
        this(false, -1);
    }

    @Override
    public boolean matches() {
        return matches;
    }

    public int getLevel() {
        Preconditions.checkState(matches, "Does not match! Cannot extract data!");
        return level;
    }

    static LevelUpMatchResult nonMatching() {
        return NON_MATCHING;
    }

}
