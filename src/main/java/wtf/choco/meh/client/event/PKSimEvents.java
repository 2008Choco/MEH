package wtf.choco.meh.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public final class PKSimEvents {

    /**
     * Callback for when a parkour course is completed.
     */
    public static final Event<ParkourComplete> PARKOUR_COMPLETE = EventFactory.createArrayBacked(ParkourComplete.class,
            listeners -> () -> {
                for (ParkourComplete event : listeners) {
                    event.onParkourComplete();
                }
            }
    );

    /**
     * Callback for when the player's Parkour Simulator experience level changes.
     */
    public static final Event<ExperienceChange> EXPERIENCE_CHANGE = EventFactory.createArrayBacked(ExperienceChange.class,
            listeners -> (fromExperience, toExperience, fromRequiredExperience, toRequiredExperience, reason) -> {
                for (ExperienceChange event : listeners) {
                    event.onExperienceChange(fromExperience, toExperience, fromRequiredExperience, toRequiredExperience, reason);
                }
            }
    );

    /**
     * Callback for when the player's Parkour Simulator coins change.
     */
    public static final Event<CoinChange> COIN_CHANGE = EventFactory.createArrayBacked(CoinChange.class,
            listeners -> (fromCoins, toCoins, reason) -> {
                for (CoinChange event : listeners) {
                    event.onCoinChange(fromCoins, toCoins, reason);
                }
            }
    );

    /**
     * Callback for when the player's Parkour Simulator level changes.
     */
    public static final Event<LevelChange> LEVEL_CHANGE = EventFactory.createArrayBacked(LevelChange.class,
            listeners -> (fromLevel, toLevel, reason) -> {
                for (LevelChange event : listeners) {
                    event.onLevelChange(fromLevel, toLevel, reason);
                }
            }
    );

    private PKSimEvents() { }

    @FunctionalInterface
    public interface ParkourComplete {

        /**
         * Called when completing a parkour course.
         */
        public void onParkourComplete();

    }

    @FunctionalInterface
    public interface ExperienceChange {

        /**
         * Called when the player's experience changes for some reason.
         *
         * @param fromExperience the experience before the change
         * @param toExperience the experience after the change
         * @param fromRequiredExperience the experience required to level up before the change
         * @param toRequiredExperience the experience required to level up after the change
         * @param reason the reason for the change
         */
        public void onExperienceChange(int fromExperience, int toExperience, int fromRequiredExperience, int toRequiredExperience, Reason reason);

        public static enum Reason {

            /**
             * A parkour course was completed and the player was awarded experience.
             */
            PARKOUR_COMPLETION,
            /**
             * The player has leveled up and their experience was adjusted accordingly.
             */
            LEVEL_UP,
            /**
             * The player has prestiged and their experience was reset.
             */
            PRESTIGE; // TODO: Unhandled

        }

    }

    @FunctionalInterface
    public interface CoinChange {

        /**
         * Called when the player's coins change for some reason.
         *
         * @param fromCoins the coins before the change
         * @param toCoins the coins after the change
         * @param reason the reason for the change
         */
        public void onCoinChange(int fromCoins, int toCoins, Reason reason);

        public static enum Reason {

            PARKOUR_COMPLETION,
            PRESTIGE; // TODO: Unhandled

        }

    }

    @FunctionalInterface
    public interface LevelChange {

        /**
         * Called when the player's level changes for some reason.
         *
         * @param fromLevel the player's current level
         * @param toLevel the player's new level
         * @param reason the reason for the level change
         */
        public void onLevelChange(int fromLevel, int toLevel, Reason reason);

        public static enum Reason {

            /**
             * The player has leveled up.
             */
            LEVEL_UP,
            /**
             * The player has prestiged and their level was reset.
             */
            PRESTIGE; // TODO: Unhandled

        }

    }

}
