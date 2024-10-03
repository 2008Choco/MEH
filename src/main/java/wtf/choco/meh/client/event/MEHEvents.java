package wtf.choco.meh.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import wtf.choco.meh.client.mnemonic.Mnemonic;
import wtf.choco.meh.client.scoreboard.HypixelScoreboard;

/**
 * Contains general-purpose events invoked by MEHEvents, not tied to any specific feature.
 */
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

    /**
     * Callback for when a {@link Mnemonic} has been performed by the client.
     */
    public static final Event<MnemonicCompletion> MNEMONIC_COMPLETION = EventFactory.createArrayBacked(MnemonicCompletion.class,
            listeners -> mnemonic -> {
                for (MnemonicCompletion event : listeners) {
                    if (!event.onMnemonic(mnemonic)) {
                        return false;
                    }
                }

                return true;
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

    @FunctionalInterface
    public interface MnemonicCompletion {

        /**
         * Called when the client successfully completes a mnemonic.
         *
         * @param mnemonic the mnemonic that was completed
         *
         * @return true if the mnemonic should continue being handled, or false if the mnemonic processing
         * should be cancelled. If the mnemonic processing is successful (i.e. this listener returns true),
         * then LWJGL will not pass the key press to Minecraft. Consequently, if the mnemonic processing is
         * NOT successful (i.e. this listener returns false), then the key press will be passed to Minecraft
         * for further processing
         */
        public boolean onMnemonic(Mnemonic mnemonic);

    }

}
