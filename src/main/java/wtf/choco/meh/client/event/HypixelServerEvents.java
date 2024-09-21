package wtf.choco.meh.client.event;

import java.util.OptionalLong;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import org.jetbrains.annotations.Nullable;

/**
 * Contains events related to in-game situations on the Hypixel server.
 */
public final class HypixelServerEvents {

    /**
     * Callback for when a private message has been received by the client.
     */
    public static final Event<PrivateMessageEvent.Received> PRIVATE_MESSAGE_RECEIVED = EventFactory.createArrayBacked(PrivateMessageEvent.Received.class,
            listeners -> (senderUsername, senderRank, message, lastCommunicated) -> {
                for (PrivateMessageEvent.Received event : listeners) {
                    event.onReceived(senderUsername, senderRank, message, lastCommunicated);
                }
            }
    );

    /**
     * Callback for when a private message has been sent by the client.
     */
    public static final Event<PrivateMessageEvent.Sent> PRIVATE_MESSAGE_SENT = EventFactory.createArrayBacked(PrivateMessageEvent.Sent.class,
            listeners -> (targetUsername, targetRank, message, lastCommunicated) -> {
                for (PrivateMessageEvent.Sent event : listeners) {
                    event.onSent(targetUsername, targetRank, message, lastCommunicated);
                }
            }
    );

    private HypixelServerEvents() { }

    /**
     * Contains events related to Hypixel's private messaging system.
     */
    public final class PrivateMessageEvent {

        @FunctionalInterface
        public interface Received {

            /**
             * Called when a private message has been received by the client.
             *
             * @param senderUsername the username of the player that sent the message
             * @param senderRank the rank of the sender, or null if no rank
             * @param message the message that was received
             * @param lastCommunicated an optional containing the timestamp the last time the sender
             * was communicated with during this session, either by the sender or by the client, or
             * an empty optional if this user has not been communicated with this session
             */
            public void onReceived(String senderUsername, @Nullable String senderRank, String message, OptionalLong lastCommunicated);

        }

        @FunctionalInterface
        public interface Sent {

            /**
             * Called when a private message has been sent by the client.
             *
             * @param targetUsername the username of the player to which the message was sent
             * @param targetRank the rank of the sender, or null if none
             * @param message the message that was sent
             * @param lastCommunicated an optional containing the timestamp the last time the target
             * was communicated with during this session, either by the target or by the client, or
             * an empty optional if this target has not been communicated with this session
             */
            public void onSent(String targetUsername, @Nullable String targetRank, String message, OptionalLong lastCommunicated);

        }

    }

}
