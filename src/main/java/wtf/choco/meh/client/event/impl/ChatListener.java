package wtf.choco.meh.client.event.impl;

import com.google.common.base.Preconditions;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.function.Consumer;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import wtf.choco.meh.client.chat.extractor.BiUserData;
import wtf.choco.meh.client.chat.extractor.ChatExtractor;
import wtf.choco.meh.client.chat.extractor.ChatExtractors;
import wtf.choco.meh.client.chat.extractor.ChatMatcher;
import wtf.choco.meh.client.chat.extractor.PartyRoleChangeData;
import wtf.choco.meh.client.chat.extractor.PrivateMessageData;
import wtf.choco.meh.client.chat.extractor.UserData;
import wtf.choco.meh.client.event.HypixelServerEvents;
import wtf.choco.meh.client.party.PartyRole;

public final class ChatListener {

    private static final Object2LongMap<String> LAST_COMMUNICATED = new Object2LongOpenHashMap<>();

    // Generally speaking, keep in order of most performant then most frequent
    // To be honest, the client can probably parse these strings of text pretty quick... but short circuit as best as possible
    private static final List<ChatHandler> CHAT_HANDLERS = List.of(
            new MatcherHandler(ChatExtractors.PARTY_DISBAND_EMPTY, ChatListener::handlePartyDisbandEmpty),
            new MatcherHandler(ChatExtractors.PARTY_DISBAND_LEADER_DISCONNECTED, ChatListener::handlePartyDisbandLeaderDisconnected),
            new MatcherHandler(ChatExtractors.PARTY_LEAVE, ChatListener::handlePartyLeave),
            new MatcherHandler(ChatExtractors.PARTY_BARGE, ChatListener::handlePartyBarge),
            new ExtractorHandler<>(ChatExtractors.PRIVATE_MESSAGE, ChatListener::handlePrivateMessage),
            new ExtractorHandler<>(ChatExtractors.PARTY_DISBAND_LEADER_DISBANDED, ChatListener::handlePartyDisbandLeaderDisbanded),
            new ExtractorHandler<>(ChatExtractors.PARTY_JOIN, ChatListener::handlePartyJoin),
            new ExtractorHandler<>(ChatExtractors.PARTY_USER_JOIN, ChatListener::handlePartyUserJoin),
            new ExtractorHandler<>(ChatExtractors.PARTY_MEMBER_DISCONNECT, ChatListener::handlePartyMemberDisconnect),
            new ExtractorHandler<>(ChatExtractors.PARTY_MEMBER_REJOIN, ChatListener::handlePartyMemberRejoin),
            new ExtractorHandler<>(ChatExtractors.PARTY_MEMBER_KICK, ChatListener::handlePartyMemberKick),
            new ExtractorHandler<>(ChatExtractors.PARTY_KICK, ChatListener::handlePartyKick),
            new ExtractorHandler<>(ChatExtractors.PARTY_MEMBER_LEAVE, ChatListener::handlePartyMemberLeave),
            new ExtractorHandler<>(ChatExtractors.PARTY_MEMBER_BARGE, ChatListener::handlePartyMemberBarge),
            new ExtractorHandler<>(ChatExtractors.PARTY_INVITE, ChatListener::handlePartyInvite),
            new ExtractorHandler<>(ChatExtractors.PARTY_USER_INVITE, ChatListener::handlePartyUserInvite),
            new ExtractorHandler<>(ChatExtractors.PARTY_TRANSFER, ChatListener::handlePartyTransfer),
            new ExtractorHandler<>(ChatExtractors.PARTY_ROLE_CHANGE, ChatListener::handlePartyRoleChange),
            new ExtractorHandler<>(ChatExtractors.PARTY_MEMBER_YOINK, ChatListener::handlePartyMemberYoink)
    );

    private static boolean initialized = false;

    private ChatListener() { }

    public static void initialize() {
        Preconditions.checkState(!initialized, "Already initialized");

        ClientReceiveMessageEvents.GAME.register(ChatListener::onReceiveChatMessage);

        initialized = true;
    }

    private static void onReceiveChatMessage(Component message, boolean overlay) {
        if (overlay) {
            return;
        }

        String strippedMessage = ChatFormatting.stripFormatting(message.getString());
        for (ChatHandler handler : CHAT_HANDLERS) {
            if (handler.handle(strippedMessage)) {
                break;
            }
        }
    }

    private static void handlePartyDisbandEmpty() {
        HypixelServerEvents.PARTY_DISBANDED.invoker().onDisband(HypixelServerEvents.PartyEvent.Disband.Reason.EMPTY_PARTY, null);
    }

    private static void handlePartyDisbandLeaderDisconnected() {
        HypixelServerEvents.PARTY_DISBANDED.invoker().onDisband(HypixelServerEvents.PartyEvent.Disband.Reason.LEADER_DISCONNECTED, null);
    }

    private static void handlePartyDisbandLeaderDisbanded(UserData userData) {
        HypixelServerEvents.PARTY_DISBANDED.invoker().onDisband(HypixelServerEvents.PartyEvent.Disband.Reason.LEADER_DISBANDED, userData);
    }

    private static void handlePrivateMessage(PrivateMessageData data) {
        PrivateMessageData.Direction direction = data.direction();
        String username = data.user().username();

        long lastCommunicatedTimestamp = LAST_COMMUNICATED.getLong(username);
        OptionalLong lastCommunicated = lastCommunicatedTimestamp > 0 ? OptionalLong.of(lastCommunicatedTimestamp) : OptionalLong.empty();

        if (direction == PrivateMessageData.Direction.INCOMING) {
            HypixelServerEvents.PRIVATE_MESSAGE_RECEIVED.invoker().onReceived(data.user(), data.message(), lastCommunicated);
        } else if (direction == PrivateMessageData.Direction.OUTGOING) {
            HypixelServerEvents.PRIVATE_MESSAGE_SENT.invoker().onSent(data.user(), data.message(), lastCommunicated);
        }

        LAST_COMMUNICATED.put(username, System.currentTimeMillis());
    }

    private static void handlePartyJoin(UserData data) {
        HypixelServerEvents.PARTY_JOINED.invoker().onJoin(data);
    }

    private static void handlePartyUserJoin(UserData data) {
        HypixelServerEvents.PARTY_USER_JOINED.invoker().onUserJoin(data);
    }

    private static void handlePartyMemberDisconnect(UserData data) {
        HypixelServerEvents.PARTY_MEMBER_DISCONNECTED.invoker().onMemberDisconnect(data);
    }

    private static void handlePartyMemberRejoin(UserData data) {
        HypixelServerEvents.PARTY_MEMBER_REJOINED.invoker().onMemberRejoin(data);
    }

    private static void handlePartyLeave() {
        HypixelServerEvents.PARTY_LEFT.invoker().onLeave();
    }

    private static void handlePartyBarge() {
        HypixelServerEvents.PARTY_BARGED.invoker().onBarge();
    }

    private static void handlePartyMemberLeave(UserData data) {
        HypixelServerEvents.PARTY_MEMBER_LEFT.invoker().onMemberLeave(data);
    }

    private static void handlePartyMemberKick(UserData data) {
        HypixelServerEvents.PARTY_MEMBER_KICKED.invoker().onMemberKick(data);
    }

    private static void handlePartyKick(UserData data) {
        HypixelServerEvents.PARTY_KICKED.invoker().onKick(data);
    }

    private static void handlePartyMemberBarge(UserData data) {
        HypixelServerEvents.PARTY_MEMBER_BARGED.invoker().onMemberBarge(data);
    }

    private static void handlePartyInvite(UserData data) {
        HypixelServerEvents.PARTY_INVITED.invoker().onInvite(data);
    }

    private static void handlePartyUserInvite(BiUserData data) {
        HypixelServerEvents.PARTY_USER_INVITED.invoker().onUserInvite(data.targetUser(), data.user());
    }

    private static void handlePartyTransfer(BiUserData data) {
        HypixelServerEvents.PARTY_TRANSFERED.invoker().onTransfer(data.targetUser(), data.user());
    }

    private static void handlePartyRoleChange(PartyRoleChangeData data) {
        PartyRole role = data.role();

        PartyRoleChangeData.Action action = data.action();
        if (action == PartyRoleChangeData.Action.PROMOTED) {
            HypixelServerEvents.PARTY_MEMBER_PROMOTED.invoker().onMemberPromote(data.target(), data.user(), role);
        } else if (action == PartyRoleChangeData.Action.DEMOTED) {
            HypixelServerEvents.PARTY_MEMBER_DEMOTED.invoker().onMemberDemote(data.target(), data.user(), role);
        }
    }

    private static void handlePartyMemberYoink(BiUserData data) {
        HypixelServerEvents.PARTY_USER_YOINKED.invoker().onUserYoink(data.targetUser(), data.user());
    }

    private static interface ChatHandler {

        public boolean handle(String input);

    }

    private static final class ExtractorHandler<T> implements ChatHandler {

        private final ChatExtractor<T> extractor;
        private final Consumer<T> action;

        private ExtractorHandler(ChatExtractor<T> extractor, Consumer<T> action) {
            this.extractor = extractor;
            this.action = action;
        }

        @Override
        public boolean handle(String input) {
            Optional<T> data = extractor.extract(input);
            if (data.isEmpty()) {
                return false;
            }

            action.accept(data.get());
            return true;
        }

    }

    private static final class MatcherHandler implements ChatHandler {

        private final ChatMatcher matcher;
        private final Runnable action;

        private MatcherHandler(ChatMatcher matcher, Runnable action) {
            this.matcher = matcher;
            this.action = action;
        }

        @Override
        public boolean handle(String input) {
            boolean success = matcher.matches(input);
            if (success) {
                action.run();
            }
            return success;
        }

    }

}
