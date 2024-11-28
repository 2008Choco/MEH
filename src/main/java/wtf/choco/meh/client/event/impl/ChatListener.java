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

public final class ChatListener {

    private static final Object2LongMap<String> LAST_COMMUNICATED = new Object2LongOpenHashMap<>();

    // Generally speaking, keep in order of most performant then most frequent
    // To be honest, the client can probably parse these strings of text pretty quick... but short circuit as best as possible
    private static final List<ChatHandler> CHAT_HANDLERS = List.of(
            new MatcherHandler(ChatExtractors.PARTY_DISBAND_EMPTY, ChatListener::handlePartyDisband),
            new ExtractorHandler<>(ChatExtractors.PRIVATE_MESSAGE, ChatListener::handlePrivateMessage),
            new ExtractorHandler<>(ChatExtractors.PARTY_DISBAND, ChatListener::handlePartyDisband),
            new ExtractorHandler<>(ChatExtractors.PARTY_JOIN_SELF, ChatListener::handlePartyJoinSelf),
            new ExtractorHandler<>(ChatExtractors.PARTY_JOIN_OTHER, ChatListener::handlePartyJoinOther),
            new ExtractorHandler<>(ChatExtractors.PARTY_KICK_OTHER, ChatListener::handlePartyKick),
            new ExtractorHandler<>(ChatExtractors.PARTY_KICKED_SELF, ChatListener::handlePartyKicked),
            new ExtractorHandler<>(ChatExtractors.PARTY_LEAVE, ChatListener::handlePartyLeave),
            new ExtractorHandler<>(ChatExtractors.PARTY_INVITE, ChatListener::handlePartyInvite),
            new ExtractorHandler<>(ChatExtractors.PARTY_TRANSFER, ChatListener::handlePartyTransfer),
            new ExtractorHandler<>(ChatExtractors.PARTY_ROLE_CHANGE, ChatListener::handlePartyRoleChange)
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

    private static void handlePartyDisband() {
        HypixelServerEvents.PARTY_DISBAND.invoker().onDisband(null, null);
    }

    private static void handlePartyDisband(UserData userData) {
        HypixelServerEvents.PARTY_DISBAND.invoker().onDisband(userData.rank(), userData.username());
    }

    private static void handlePrivateMessage(PrivateMessageData data) {
        PrivateMessageData.Direction direction = data.direction();
        String rank = data.rank();
        String username = data.username();
        String messageString = data.message();

        long lastCommunicatedTimestamp = LAST_COMMUNICATED.getLong(username);
        OptionalLong lastCommunicated = lastCommunicatedTimestamp > 0 ? OptionalLong.of(lastCommunicatedTimestamp) : OptionalLong.empty();

        if (direction == PrivateMessageData.Direction.INCOMING) {
            HypixelServerEvents.PRIVATE_MESSAGE_RECEIVED.invoker().onReceived(username, rank, messageString, lastCommunicated);
        } else if (direction == PrivateMessageData.Direction.OUTGOING) {
            HypixelServerEvents.PRIVATE_MESSAGE_SENT.invoker().onSent(username, rank, messageString, lastCommunicated);
        }

        LAST_COMMUNICATED.put(username, System.currentTimeMillis());
    }

    private static void handlePartyJoinSelf(UserData data) {
        HypixelServerEvents.PARTY_JOIN.invoker().onJoin(data.rank(), data.username());
    }

    private static void handlePartyJoinOther(UserData data) {
        HypixelServerEvents.PARTY_MEMBER_JOIN.invoker().onJoin(data.rank(), data.username());
    }

    private static void handlePartyLeave(UserData data) {
        handlePartyRemoval(data, false);
    }

    private static void handlePartyKick(UserData data) {
        handlePartyRemoval(data, true);
    }

    private static void handlePartyRemoval(UserData data, boolean kicked) {
        HypixelServerEvents.PARTY_MEMBER_LEAVE.invoker().onLeave(data.rank(), data.username(), kicked);
    }

    private static void handlePartyKicked(UserData data) {
        HypixelServerEvents.PARTY_KICKED.invoker().onKicked(data.rank(), data.username());
    }

    private static void handlePartyInvite(BiUserData data) {
        HypixelServerEvents.PARTY_MEMBER_INVITE.invoker().onInvite(data.targetRank(), data.targetUsername(), data.rank(), data.username());
    }

    private static void handlePartyTransfer(BiUserData data) {
        HypixelServerEvents.PARTY_TRANSFER.invoker().onTransfer(data.targetRank(), data.targetUsername(), data.rank(), data.username());
    }

    private static void handlePartyRoleChange(PartyRoleChangeData data) {
        String rank = data.rank();
        String username = data.username();
        String targetRank = data.targetRank();
        String targetUsername = data.targetUsername();
        String role = data.role();

        PartyRoleChangeData.Action action = data.action();
        if (action == PartyRoleChangeData.Action.PROMOTED) {
            HypixelServerEvents.PARTY_MEMBER_PROMOTE.invoker().onPromote(targetRank, targetUsername, rank, username, role);
        } else if (action == PartyRoleChangeData.Action.DEMOTED) {
            HypixelServerEvents.PARTY_MEMBER_DEMOTE.invoker().onDemote(targetRank, targetUsername, rank, username, role);
        }
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
