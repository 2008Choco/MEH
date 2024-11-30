package wtf.choco.meh.client.event;

import java.util.OptionalLong;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import org.jetbrains.annotations.Nullable;

import wtf.choco.meh.client.server.HypixelServerType;

/**
 * Contains events related to in-game situations on the Hypixel server.
 */
public final class HypixelServerEvents {

    /**
     * Callback for when the client is aware of a Hypixel server type change.
     * <p>
     * This event is not a replacement for the client connection events. This event <strong>WILL</strong>
     * be called much later once the client has established which Hypixel server type was joined. This
     * should be used only to check what type of server the client joined into, not whether or not they
     * joined a Hypixel server at all.
     */
    public static final Event<ServerLocationChange> SERVER_LOCATION_CHANGE = EventFactory.createArrayBacked(ServerLocationChange.class,
            listeners -> (serverType, lobby, fromServerType, fromLobby) -> {
                for (ServerLocationChange event : listeners) {
                    event.onLocationChange(serverType, lobby, fromServerType, fromLobby);
                }
            }
    );

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

    /**
     * Callback for when a party disbands.
     */
    public static final Event<PartyEvent.Disband> PARTY_DISBAND = EventFactory.createArrayBacked(PartyEvent.Disband.class,
            listeners -> (disbanderRank, disbanderUsername, reason) -> {
                for (PartyEvent.Disband event : listeners) {
                    event.onDisband(disbanderRank, disbanderUsername, reason);
                }
            }
    );

    /**
     * Callback for when the client joins another user's party.
     */
    public static final Event<PartyEvent.PartyJoin> PARTY_JOIN = EventFactory.createArrayBacked(PartyEvent.PartyJoin.class,
            listeners -> (rank, username) -> {
                for (PartyEvent.PartyJoin event : listeners) {
                    event.onJoin(rank, username);
                }
            }
    );

    /**
     * Callback for when the client is kicked from the party by a party moderator.
     */
    public static final Event<PartyEvent.PartyKicked> PARTY_KICKED = EventFactory.createArrayBacked(PartyEvent.PartyKicked.class,
            listeners -> (rank, username) -> {
                for (PartyEvent.PartyKicked event : listeners) {
                    event.onKicked(rank, username);
                }
            }
    );

    /**
     * Callback for when a member joins the party.
     */
    public static final Event<PartyEvent.MemberJoin> PARTY_MEMBER_JOIN = EventFactory.createArrayBacked(PartyEvent.MemberJoin.class,
            listeners -> (rank, username) -> {
                for (PartyEvent.MemberJoin event : listeners) {
                    event.onJoin(rank, username);
                }
            }
    );

    /**
     * Callback for when a member leaves the party.
     */
    public static final Event<PartyEvent.MemberLeave> PARTY_MEMBER_LEAVE = EventFactory.createArrayBacked(PartyEvent.MemberLeave.class,
            listeners -> (rank, username, kicked) -> {
                for (PartyEvent.MemberLeave event : listeners) {
                    event.onLeave(rank, username, kicked);
                }
            }
    );

    /**
     * Callback for when a member is invited to the party.
     */
    public static final Event<PartyEvent.MemberInvite> PARTY_MEMBER_INVITE = EventFactory.createArrayBacked(PartyEvent.MemberInvite.class,
            listeners -> (rank, username, inviterRank, inviterUsername) -> {
                for (PartyEvent.MemberInvite event : listeners) {
                    event.onInvite(rank, username, inviterRank, inviterUsername);
                }
            }
    );

    /**
     * Callback for when the party is transferred to another player.
     */
    public static final Event<PartyEvent.Transfer> PARTY_TRANSFER = EventFactory.createArrayBacked(PartyEvent.Transfer.class,
            listeners -> (rank, username, transferrerRank, transferrerUsername) -> {
                for (PartyEvent.Transfer event : listeners) {
                    event.onTransfer(rank, username, transferrerRank, transferrerUsername);
                }
            }
    );

    /**
     * Callback for when a party member is promoted to a new role.
     */
    public static final Event<PartyEvent.MemberPromote> PARTY_MEMBER_PROMOTE = EventFactory.createArrayBacked(PartyEvent.MemberPromote.class,
            listeners -> (rank, username, promoterRank, promoterUsername, role) -> {
                for (PartyEvent.MemberPromote event : listeners) {
                    event.onPromote(rank, username, promoterRank, promoterUsername, role);
                }
            }
    );

    /**
     * Callback for when a party member is demoted to a new role.
     */
    public static final Event<PartyEvent.MemberDemote> PARTY_MEMBER_DEMOTE = EventFactory.createArrayBacked(PartyEvent.MemberDemote.class,
            listeners -> (rank, username, demoterRank, demoterUsername, role) -> {
                for (PartyEvent.MemberDemote event : listeners) {
                    event.onDemote(rank, username, demoterRank, demoterUsername, role);
                }
            }
    );

    private HypixelServerEvents() { }

    @FunctionalInterface
    public interface ServerLocationChange {

        /**
         * Called when the client is aware of a Hypixel server type change.
         *
         * @param serverType the server type that the client is now connected to
         * @param lobby whether or not the connected server is a lobby
         * @param fromServerType the server type that the client was last connected to, or null if either that
         * could not be determined reliably enough
         * @param fromLobby whether or not the previous server type was a lobby. If {@code fromServerType} is
         * null, this value will likely also be false and should probably be ignored
         */
        public void onLocationChange(HypixelServerType serverType, boolean lobby, @Nullable HypixelServerType fromServerType, boolean fromLobby);

    }

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

    /**
     * Contains events related to Hypixel's party system.
     */
    public final class PartyEvent {

        @FunctionalInterface
        public interface Disband {

            /**
             * Called when a party is disbanded.
             *
             * @param disbanderRank the rank of the user that disbanded the party, or null if none
             * @param disbanderUsername the username of the user that disbanded the party, or null
             * if a player did not disband it
             * @param reason the reason for the disband
             */
            public void onDisband(@Nullable String disbanderRank, @Nullable String disbanderUsername, Reason reason);

            public enum Reason {

                /**
                 * The party was disbanded because there were no more members.
                 */
                EMPTY_PARTY,

                /**
                 * The party was disbanded because the leader disbanded it manually.
                 */
                LEADER_DISBANDED,

                /**
                 * The party was disbanded because the leader disconnected from the network.
                 */
                LEADER_DISCONNECTED;

            }

        }

        @FunctionalInterface
        public interface PartyJoin {

            /**
             * Called when you join another member's party.
             *
             * @param partyLeaderRank the rank of the party leader, or null if none
             * @param partyLeaderUsername the username of the party leader
             */
            public void onJoin(@Nullable String partyLeaderRank, @Nullable String partyLeaderUsername);

        }

        @FunctionalInterface
        public interface PartyKicked {

            /**
             * Called when the client is kicked from the party by a party moderator.
             *
             * @param kickerRank the rank of the party member that kicked the client, or null if none
             * @param kickerUsername the username of the party member that kicked the client
             */
            public void onKicked(@Nullable String kickerRank, @Nullable String kickerUsername);

        }

        @FunctionalInterface
        public interface MemberJoin {

            /**
             * Called when a member joins the party.
             *
             * @param rank the rank of the user joining the party, or null if none
             * @param username the username of the user joining the party
             */
            public void onJoin(@Nullable String rank, String username);

        }

        @FunctionalInterface
        public interface MemberLeave {

            /**
             * Called when a member leaves the party, whether voluntary or by a kick.
             *
             * @param rank the rank of the user leaving the party, or null if none
             * @param username the username of the user leaving the party
             * @param kicked true if kicked, false if left voluntarily
             */
            public void onLeave(@Nullable String rank, String username, boolean kicked);

        }

        @FunctionalInterface
        public interface MemberInvite {

            /**
             * Called when a member is invited to join the party.
             *
             * @param rank the rank of the user being invited to the party, or null if none
             * @param username the username of the user being invited to the party
             * @param inviterRank the rank of the user that sent the invitation, or null if none
             * @param inviterUsername the username of the user that sent the invitation
             */
            public void onInvite(@Nullable String rank, String username, @Nullable String inviterRank, String inviterUsername);

        }

        @FunctionalInterface
        public interface Transfer {

            /**
             * Called when the party is transferred to another party member.
             *
             * @param rank the rank of the new party leader, or null if none
             * @param username the username of the the new party leader
             * @param transferrerRank the rank of the user that initiated the transfer, or null if none
             * @param transferrerUsername the username of the user that initiated the transfer
             */
            public void onTransfer(@Nullable String rank, String username, @Nullable String transferrerRank, String transferrerUsername);

        }

        @FunctionalInterface
        public interface MemberPromote {

            /**
             * Called when a party member is promoted.
             *
             * @param rank the rank of the promoted user, or null if none
             * @param username the username of the promoted user
             * @param promoterRank the rank of the user that promoted the target, or null if none
             * @param promoterUsername the username of the user that promoted the target
             * @param role the user's new role
             */
            public void onPromote(@Nullable String rank, String username, @Nullable String promoterRank, String promoterUsername, String role);

        }

        @FunctionalInterface
        public interface MemberDemote {

            /**
             * Called when a party member is demoted.
             *
             * @param rank the rank of the demoted user, or null if none
             * @param username the username of the demoted user
             * @param demoterRank the rank of the user that demoted the target, or null if none
             * @param demoterUsername the username of the user that demoted the target
             * @param role the user's new role
             */
            public void onDemote(@Nullable String rank, String username, @Nullable String demoterRank, String demoterUsername, String role);

        }

    }

}
