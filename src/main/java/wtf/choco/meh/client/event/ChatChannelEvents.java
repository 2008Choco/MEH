package wtf.choco.meh.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import wtf.choco.meh.client.chat.ChatChannel;

/**
 * Contains client-sided events triggered by chat channels.
 */
public final class ChatChannelEvents {

    /**
     * Callback before a chat channel is created.
     */
    public static final Event<Create> CREATE = EventFactory.createArrayBacked(Create.class,
            listeners -> channel -> {
                for (Create event : listeners) {
                    if (!event.onCreateChatChannel(channel)) {
                        return false;
                    }
                }

                return true;
            }
    );

    /**
     * Callback before a chat channel is deleted.
     */
    public static final Event<Delete> DELETE = EventFactory.createArrayBacked(Delete.class,
            listeners -> channel -> {
                for (Delete event : listeners) {
                    if (!event.onDeleteChatChannel(channel)) {
                        return false;
                    }
                }

                return true;
            }
    );

    /**
     * Callback before a channel is switched to.
     */
    public static final Event<Switch> SWITCH = EventFactory.createArrayBacked(Switch.class,
            listeners -> (from, to, reason) -> {
                for (Switch event : listeners) {
                    if (!event.onSwitchChatChannel(from, to, reason)) {
                        return false;
                    }
                }

                return true;
            }
    );

    private ChatChannelEvents() { }

    @FunctionalInterface
    public interface Create {

        /**
         * Called before a {@link ChatChannel} is created.
         *
         * @param channel the channel being created
         * @return false if the channel should not be created, true to allow it to be created
         */
        public boolean onCreateChatChannel(ChatChannel channel);

    }

    @FunctionalInterface
    public interface Delete {

        /**
         * Called before a {@link ChatChannel} is deleted.
         *
         * @param channel the channel being deleted
         *
         * @return false if the channel should not be deleted, true to allow it to be deleted
         */
        public boolean onDeleteChatChannel(ChatChannel channel);

    }

    @FunctionalInterface
    public interface Switch {

        /**
         * Called before a channel switch occurs.
         *
         * @param from the channel being switched from
         * @param to the channel being switched to
         * @param reason the reason for the channel switch
         *
         * @return false if the channel should not be switched, true to allow it to switch
         */
        public boolean onSwitchChatChannel(ChatChannel from, ChatChannel to, Reason reason);

        public static enum Reason {

            /**
             * The channel was switched to manually by the client (usually by keybind) to the next
             * sequential channel in the registry.
             */
            MANUAL_NEXT,
            /**
             * The channel was switched to manually by the client (usually by keybind) to the previous
             * sequential channel in the registry.
             */
            MANUAL_PREVIOUS,
            /**
             * The channel was switched to automatically as a result of a new incoming private message.
             */
            AUTOMATIC_INCOMING,
            /**
             * The channel was switched to automatically as a result of a new outgoing private message.
             */
            AUTOMATIC_OUTGOING;

        }

    }

}
