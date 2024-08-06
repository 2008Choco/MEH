package wtf.choco.meh.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import wtf.choco.meh.client.scoreboard.HypixelScoreboard;

public final class MEHEvents {

    /**
     * Callback for when a {@link HypixelScoreboard}'s entries have been refreshed.
     */
    public static final Event<HypixelScoreboardRefresh> HYPIXEL_SCOREBOARD_REFRESH = EventFactory.createArrayBacked(HypixelScoreboardRefresh.class,
            listeners -> scoreboard -> {
                for (HypixelScoreboardRefresh event : listeners) {
                    event.onRefresh(scoreboard);
                }
            }
    );

    private MEHEvents() { }

    @FunctionalInterface
    public interface HypixelScoreboardRefresh {

        /**
         * Called when a {@link HypixelScoreboard}'s entries have been refreshed. The scoreboard may be
         * safely read from.
         *
         * @param scoreboard the scoreboard
         */
        public void onRefresh(HypixelScoreboard scoreboard);

    }

}
