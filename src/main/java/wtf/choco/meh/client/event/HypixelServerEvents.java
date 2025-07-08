package wtf.choco.meh.client.event;

import java.util.OptionalLong;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import org.jetbrains.annotations.Nullable;

import wtf.choco.meh.client.chat.extractor.UserData;
import wtf.choco.meh.client.fishing.FishingCatch;
import wtf.choco.meh.client.party.PartyRole;
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
    public static final Event<ServerLocationChange> SERVER_LOCATION_CHANGED = EventFactory.createArrayBacked(ServerLocationChange.class,
            listeners -> (serverType, lobby, fromServerType, fromLobby) -> {
                for (ServerLocationChange event : listeners) {
                    event.onLocationChange(serverType, lobby, fromServerType, fromLobby);
                }
            }
    );

    /**
     * Callback for when a private message has been received by the client.
     */
    public static final Event<PrivateMessageEvent.Receive> PRIVATE_MESSAGE_RECEIVED = EventFactory.createArrayBacked(PrivateMessageEvent.Receive.class,
            listeners -> (sender, message, lastCommunicated) -> {
                for (PrivateMessageEvent.Receive event : listeners) {
                    event.onReceived(sender, message, lastCommunicated);
                }
            }
    );

    /**
     * Callback for when a private message has been sent by the client.
     */
    public static final Event<PrivateMessageEvent.Send> PRIVATE_MESSAGE_SENT = EventFactory.createArrayBacked(PrivateMessageEvent.Send.class,
            listeners -> (recipient, message, lastCommunicated) -> {
                for (PrivateMessageEvent.Send event : listeners) {
                    event.onSent(recipient, message, lastCommunicated);
                }
            }
    );

    /**
     * Callback for when the client (a Hypixel Admin) barges into another user's party.
     * <p>
     * Note that a {@link #PARTY_BARGED} is called before a {@link #PARTY_MEMBER_BARGED}
     * is called. Hypixel treats barging into the party as the following process:
     * <ol>
     *   <li>Admin executes command to barge into a party.
     *   <li>Admin is now part of the party <em>(PARTY_BARGED event is called)</em>.
     *   <li>The party (which now includes the admin) is notified of the admin having
     *   barged into the party <em>(PARTY_MEMBER_BARGED event is called)</em>.
     * </ol>
     */
    public static final Event<PartyEvent.Barge> PARTY_BARGED = EventFactory.createArrayBacked(PartyEvent.Barge.class,
           listeners -> () -> {
               for (PartyEvent.Barge event : listeners) {
                   event.onBarge();
               }
           }
    );

    /**
     * Callback for when a party has been disbanded.
     */
    public static final Event<PartyEvent.Disband> PARTY_DISBANDED = EventFactory.createArrayBacked(PartyEvent.Disband.class,
            listeners -> (reason, disbander) -> {
                for (PartyEvent.Disband event : listeners) {
                    event.onDisband(reason, disbander);
                }
            }
    );

    /**
     * Callback for when the client joins another member's party.
     */
    public static final Event<PartyEvent.Join> PARTY_JOINED = EventFactory.createArrayBacked(PartyEvent.Join.class,
            listeners -> partyLeader -> {
                for (PartyEvent.Join event : listeners) {
                    event.onJoin(partyLeader);
                }
            }
    );

    /**
     * Callback for when the client is kicked from another member's party.
     */
    public static final Event<PartyEvent.Kick> PARTY_KICKED = EventFactory.createArrayBacked(PartyEvent.Kick.class,
            listeners -> kicker -> {
                for (PartyEvent.Kick event : listeners) {
                    event.onKick(kicker);
                }
            }
    );

    /**
     * Callback for when the client leaves another member's party.
     * <p>
     * Note that this callback is not invoked if the client is kicked from the party. To listen for
     * when the client is kicked, use {@link #PARTY_KICKED} instead.
     */
    public static final Event<PartyEvent.Leave> PARTY_LEFT = EventFactory.createArrayBacked(PartyEvent.Leave.class,
            listeners -> () -> {
                for (PartyEvent.Leave event : listeners) {
                    event.onLeave();
                }
            }
    );

    /**
     * Callback for when the party has been transfered to another party member.
     */
    public static final Event<PartyEvent.Transfer> PARTY_TRANSFERED = EventFactory.createArrayBacked(PartyEvent.Transfer.class,
            listeners -> (newPartyLeader, transferrer) -> {
                for (PartyEvent.Transfer event : listeners) {
                    event.onTransfer(newPartyLeader, transferrer);
                }
            }
    );

    /**
     * Callback for when a Hypixel Admin (including the client) barges into the party.
     * <p>
     * Note that if the client is the member that barged into the party, a {@link #PARTY_BARGED}
     * is also called before a {@link #PARTY_MEMBER_BARGED} (i.e. the admin is also informed of
     * their own barging). Hypixel treats barging into the party as the following process:
     * <ol>
     *   <li>Admin executes command to barge into a party.
     *   <li>Admin is now part of the party <em>(PARTY_BARGED event is called)</em>.
     *   <li>The party (which now includes the admin) is notified of the admin having
     *   barged into the party <em>(PARTY_MEMBER_BARGED event is called)</em>.
     * </ol>
     */
    public static final Event<PartyEvent.MemberBarge> PARTY_MEMBER_BARGED = EventFactory.createArrayBacked(PartyEvent.MemberBarge.class,
            listeners -> user -> {
                for (PartyEvent.MemberBarge event : listeners) {
                    event.onMemberBarge(user);
                }
            }
    );

    /**
     * Callback for when a party member (including the client) has been demoted.
     */
    public static final Event<PartyEvent.MemberDemote> PARTY_MEMBER_DEMOTED = EventFactory.createArrayBacked(PartyEvent.MemberDemote.class,
            listeners -> (demoted, demoter, role) -> {
                for (PartyEvent.MemberDemote event : listeners) {
                    event.onMemberDemote(demoted, demoter, role);
                }
            }
    );

    /**
     * Callback for when a party member (excluding the client) has disconnected from the Hypixel
     * server, thus giving them a period of time to rejoin before being removed from the party.
     * <p>
     * Not to be confused with {@link #PARTY_MEMBER_LEFT}, which is an intentional removal from the
     * party. A disconnection will lead to a leave if the time to rejoin expires. (TODO: THIS NEEDS IMPLEMENTING, THE REMOVAL MESSAGE IS UNKNOWN)
     */
    public static final Event<PartyEvent.MemberDisconnect> PARTY_MEMBER_DISCONNECTED = EventFactory.createArrayBacked(PartyEvent.MemberDisconnect.class,
            listeners -> user -> {
                for (PartyEvent.MemberDisconnect event : listeners) {
                    event.onMemberDisconnect(user);
                }
            }
    );

    /**
     * Callback for when a party member (excluding the client) has been kicked from the party.
     */
    public static final Event<PartyEvent.MemberKick> PARTY_MEMBER_KICKED = EventFactory.createArrayBacked(PartyEvent.MemberKick.class,
            listeners -> user -> {
                for (PartyEvent.MemberKick event : listeners) {
                    event.onMemberKick(user);
                }
            }
    );

    /**
     * Callback for when a party member (excluding the client) has left the party.
     */
    public static final Event<PartyEvent.MemberLeave> PARTY_MEMBER_LEFT = EventFactory.createArrayBacked(PartyEvent.MemberLeave.class,
            listeners -> user -> {
                for (PartyEvent.MemberLeave event : listeners) {
                    event.onMemberLeave(user);
                }
            }
    );

    /**
     * Callback for when a party member (including the client) has been promoted.
     */
    public static final Event<PartyEvent.MemberPromote> PARTY_MEMBER_PROMOTED = EventFactory.createArrayBacked(PartyEvent.MemberPromote.class,
            listeners -> (promoted, promoter, role) -> {
                for (PartyEvent.MemberPromote event : listeners) {
                    event.onMemberPromote(promoted, promoter, role);
                }
            }
    );

    /**
     * Callback for when a party member (excluding the client) has rejoined the party after having
     * disconnected from the Hypixel server.
     *
     * @see #PARTY_MEMBER_DISCONNECTED
     */
    public static final Event<PartyEvent.MemberRejoin> PARTY_MEMBER_REJOINED = EventFactory.createArrayBacked(PartyEvent.MemberRejoin.class,
            listeners -> user -> {
                for (PartyEvent.MemberRejoin event : listeners) {
                    event.onMemberRejoin(user);
                }
            }
    );

    /**
     * Callback for when a user (excluding the client) has joined the party.
     */
    public static final Event<PartyEvent.UserJoin> PARTY_USER_JOINED = EventFactory.createArrayBacked(PartyEvent.UserJoin.class,
            listeners -> user -> {
                for (PartyEvent.UserJoin event : listeners) {
                    event.onUserJoin(user);
                }
            }
    );

    /**
     * Callback for when a user (excluding the client) has been yoinked into the party by a Hypixel
     * Admin.
     */
    public static final Event<PartyEvent.UserYoink> PARTY_USER_YOINKED = EventFactory.createArrayBacked(PartyEvent.UserYoink.class,
            listeners -> (yoinked, yoinker) -> {
                for (PartyEvent.UserYoink event : listeners) {
                    event.onUserYoink(yoinked, yoinker);
                }
            }
    );

    public static final Event<FishingEvent.Catch> FISHING_CATCH = EventFactory.createArrayBacked(FishingEvent.Catch.class,
            listeners -> (fishingCatch) -> {
                for (FishingEvent.Catch event : listeners) {
                    event.onCatch(fishingCatch);
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
        public interface Receive {

            /**
             * Called when a private message has been received by the client.
             *
             * @param sender the user that sent the message
             * @param message the message that was received
             * @param lastCommunicated an optional containing the timestamp the last time the sender
             * was communicated with during this session, either by the sender or by the client, or
             * an empty optional if this user has not been communicated with this session
             */
            public void onReceived(UserData sender, String message, OptionalLong lastCommunicated);

        }

        @FunctionalInterface
        public interface Send {

            /**
             * Called when a private message has been sent by the client.
             *
             * @param recipient the user to receive the message
             * @param message the message that was sent
             * @param lastCommunicated an optional containing the timestamp the last time the target
             * was communicated with during this session, either by the target or by the client, or
             * an empty optional if this target has not been communicated with this session
             */
            public void onSent(UserData recipient, String message, OptionalLong lastCommunicated);

        }

    }

    /**
     * Contains events related to Hypixel's party system.
     */
    public final class PartyEvent {

        @FunctionalInterface
        public interface Barge {

            /**
             * Called when the client barges into another user's party without an invitation.
             * <p>
             * Note that a {@link Barge} is called before a {@link MemberBarge} is called.
             * Hypixel treats barging into the party as the following process:
             * <ol>
             *   <li>Admin executes command to barge into a party.
             *   <li>Admin is now part of the party <em>(Barge event is called)</em>.
             *   <li>The party (which now includes the admin) is notified of the admin having
             *   barged into the party <em>(MemberBarge event is called)</em>.
             * </ol>
             */
            public void onBarge();

        }

        @FunctionalInterface
        public interface Disband {

            /**
             * Called when a party has been disbanded.
             *
             * @param reason the reason for the disband
             * @param disbander the user that disbanded the party, or null if none
             */
            public void onDisband(Reason reason, @Nullable UserData disbander);

            public enum Reason {

                /**
                 * The party was empty and was disbanded automatically.
                 */
                EMPTY_PARTY,
                /**
                 * The leader disbanded the party.
                 */
                LEADER_DISBANDED,
                /**
                 * The leader disconnected, causing the party to disband automatically.
                 */
                LEADER_DISCONNECTED;

            }

        }

        @FunctionalInterface
        public interface Invite {

            /**
             * Called when the client receives an invitation from a user to join their party.
             *
             * @param inviter the user that sent the invitation
             */
            public void onInvite(UserData inviter);

        }

        @FunctionalInterface
        public interface Join {

            /**
             * Called when the client joins another member's party.
             *
             * @param partyLeader the party leader
             */
            public void onJoin(UserData partyLeader);

        }

        @FunctionalInterface
        public interface Kick {

            /**
             * Called when the client is kicked from another member's party.
             *
             * @param kicker the user that issued the kick
             */
            public void onKick(UserData kicker);

        }

        @FunctionalInterface
        public interface Leave {

            /**
             * Called when the client leaves another member's party.
             */
            public void onLeave();

        }

        @FunctionalInterface
        public interface Transfer {

            /**
             * Called when the party has been transfered to another party member.
             *
             * @param newPartyLeader the new party leader
             * @param transferrer the user that issued the party transfer
             */
            public void onTransfer(UserData newPartyLeader, UserData transferrer);

        }

        @FunctionalInterface
        public interface MemberBarge {

            /**
             * Called when a Hypixel Admin (including the client) barges into the party.
             * <p>
             * Note that if the client is the member that barged into the party, a {@link #PARTY_BARGED}
             * is also called before a {@link #PARTY_MEMBER_BARGED} (i.e. the admin is also
             * informed of their own barging). Hypixel treats barging into the party as the
             * following process:
             * <ol>
             *   <li>Admin executes command to barge into a party.
             *   <li>Admin is now part of the party <em>(Barge event is called)</em>.
             *   <li>The party (which now includes the admin) is notified of the admin having
             *   barged into the party <em>(MemberBarge event is called)</em>.
             * </ol>
             *
             * @param user the Hypixel Admin that barged into the party
             */
            public void onMemberBarge(UserData user);

        }

        @FunctionalInterface
        public interface MemberDemote {

            /**
             * Called when a party member (including the client) has been demoted.
             *
             * @param demoted the user that was demoted
             * @param demoter the user that issued the demotion
             * @param role the new role of the user
             */
            public void onMemberDemote(UserData demoted, UserData demoter, PartyRole role);

        }

        @FunctionalInterface
        public interface MemberDisconnect {

            /**
             * Called when a party member (excluding the client) has disconnected from the party.
             *
             * @param user the user that disconnected
             */
            public void onMemberDisconnect(UserData user);

        }

        @FunctionalInterface
        public interface MemberKick {

            /**
             * Called when a party member (excluding the client) has been kicked from the party.
             *
             * @param user the user that was kicked
             */
            public void onMemberKick(UserData user);

        }

        @FunctionalInterface
        public interface MemberLeave {

            /**
             * Called when a party member (excluding the client) has left the party.
             *
             * @param user the user that left the party
             */
            public void onMemberLeave(UserData user);

        }

        @FunctionalInterface
        public interface MemberPromote {

            /**
             * Called when a party member (including the client) has been promoted.
             *
             * @param promoted the user that was promoted
             * @param promoter the user that issued the promotion
             * @param role the new role of the user
             */
            public void onMemberPromote(UserData promoted, UserData promoter, PartyRole role);

        }

        @FunctionalInterface
        public interface MemberRejoin {

            /**
             * Called when a party member that had previously disconnected rejoins the party
             * before the auto-leave timer expired.
             *
             * @param user the user that rejoined
             */
            public void onMemberRejoin(UserData user);

        }

        @FunctionalInterface
        public interface UserJoin {

            /**
             * Called when a user (excluding the client) has joined the party.
             *
             * @param user the user that joined the party
             */
            public void onUserJoin(UserData user);

        }

        @FunctionalInterface
        public interface UserInvite {

            /**
             * Called when a user (excluding the client) has been invited to the party.
             *
             * @param invited the user that was invited
             * @param inviter the user that issued the invitation
             */
            public void onUserInvite(UserData invited, UserData inviter);

        }

        @FunctionalInterface
        public interface UserYoink {

            /**
             * Called when a user (excluding the client) has been yoinked into the party by a
             * Hypixel Admin.
             *
             * @param yoinked the user that was yoinked
             * @param yoinker the Hypixel Admin that performed the yoink
             */
            public void onUserYoink(UserData yoinked, UserData yoinker);

        }

    }

    /**
     * Contains events related to Hypixel's main lobby fishing.
     */
    public final class FishingEvent {

        private FishingEvent() { }

        @FunctionalInterface
        public interface Catch {

            public void onCatch(FishingCatch fishingCatch);

        }

    }

}
