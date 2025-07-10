package wtf.choco.meh.client.event.impl;

import com.google.common.base.Preconditions;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import wtf.choco.meh.client.chat.extractor.BiUserData;
import wtf.choco.meh.client.chat.extractor.ChatExtractor;
import wtf.choco.meh.client.chat.extractor.ChatExtractors;
import wtf.choco.meh.client.chat.extractor.ChatMatcher;
import wtf.choco.meh.client.chat.extractor.MythicalFishCatchData;
import wtf.choco.meh.client.chat.extractor.PartyRoleChangeData;
import wtf.choco.meh.client.chat.extractor.PrivateMessageData;
import wtf.choco.meh.client.chat.extractor.QuantitativeTreasureCatchData;
import wtf.choco.meh.client.chat.extractor.UserData;
import wtf.choco.meh.client.event.HypixelServerEvents;
import wtf.choco.meh.client.fishing.Creature;
import wtf.choco.meh.client.fishing.Fish;
import wtf.choco.meh.client.fishing.FishingCatch;
import wtf.choco.meh.client.fishing.Junk;
import wtf.choco.meh.client.fishing.MythicalFish;
import wtf.choco.meh.client.fishing.MythicalFishType;
import wtf.choco.meh.client.fishing.Plant;
import wtf.choco.meh.client.fishing.QuantifiedTreasure;
import wtf.choco.meh.client.fishing.QuantifiedTreasureType;
import wtf.choco.meh.client.fishing.Treasure;
import wtf.choco.meh.client.party.PartyRole;
import wtf.choco.meh.client.util.Components;

public final class ChatListener {

    private static final Object2LongMap<String> LAST_COMMUNICATED = new Object2LongOpenHashMap<>();

    // Generally speaking, keep in order of most performant then most frequent
    // To be honest, the client can probably parse these strings of text pretty quick... but short circuit as best as possible
    private static final List<ChatHandler> CHAT_HANDLERS = List.of(
            new MatcherHandler(ChatExtractors.PARTY_DISBAND_EMPTY, ChatListener::handlePartyDisbandEmpty),
            new MatcherHandler(ChatExtractors.PARTY_DISBAND_LEADER_DISCONNECTED, ChatListener::handlePartyDisbandLeaderDisconnected),
            new MatcherHandler(ChatExtractors.PARTY_LEAVE, ChatListener::handlePartyLeave),
            new MatcherHandler(ChatExtractors.PARTY_BARGE, ChatListener::handlePartyBarge),
            // Order of "fishing" patterns is important because some patterns overlap!
            new ExtractorHandler<>(ChatExtractors.FISHING_CAUGHT_JUNK, ChatListener::handleFishingCaughtJunk),
            new ExtractorHandler<>(ChatExtractors.FISHING_CAUGHT_QUANTITATIVE_TREASURE, ChatListener::handleFishingCaughtQuantitativeTreasure),
            new ExtractorHandler<>(ChatExtractors.FISHING_CAUGHT_TREASURE, ChatListener::handleFishingCaughtTreasure),
            new ExtractorHandler<>(ChatExtractors.FISHING_CAUGHT_MYTHICAL_FISH, ChatListener::handleFishingCaughtMythicalFish),
            new ExtractorHandler<>(ChatExtractors.FISHING_CAUGHT_GENERIC, ChatListener::handleFishingCaughtGeneric),
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
            if (handler.handle(message, strippedMessage)) {
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

    private static void handleFishingCaughtJunk(String name) {
        Junk junk = Junk.getByName(name);
        if (junk != null) {
            HypixelServerEvents.FISHING_CATCH.invoker().onCatch(junk);
        } else {
            Minecraft.getInstance().player.displayClientMessage(Component.literal("[MEH] Detected a Junk catch but couldn't find constant: \"" + name + "\"").withStyle(ChatFormatting.RED), false);
        }
    }

    private static void handleFishingCaughtQuantitativeTreasure(QuantitativeTreasureCatchData data) {
        QuantifiedTreasureType type = QuantifiedTreasureType.getByName(data.name());
        if (type != null) {
            HypixelServerEvents.FISHING_CATCH.invoker().onCatch(new QuantifiedTreasure(type, data.quantity()));
        } else {
            Minecraft.getInstance().player.displayClientMessage(Component.literal("[MEH] Detected a QuantitativeTresureType catch but couldn't find constant: \"" + data.name() + "\"").withStyle(ChatFormatting.RED), false);
        }
    }

    private static void handleFishingCaughtTreasure(String name) {
        Treasure treasure = Treasure.getByName(name);
        if (treasure != null) {
            HypixelServerEvents.FISHING_CATCH.invoker().onCatch(treasure);
        } else {
            Minecraft.getInstance().player.displayClientMessage(Component.literal("[MEH] Detected a Treasure catch but couldn't find constant: \"" + name + "\"").withStyle(ChatFormatting.RED), false);
        }
    }

    private static void handleFishingCaughtGeneric(Component message, String name) {
        FishingCatch fishingCatch = null;

        if (name.equals(Fish.SECRET_FISH.getSimpleName())) {
            // Special case for "secret fish" which gets sent using legacy text instead of a proper component due to its hover text
            // This is a "bug" (kind of) with Hypixel's translation system which translates the text with legacy colour codes and shoves the result
            // into a component literal.
            fishingCatch = Fish.SECRET_FISH;
        } if (Components.hasFormattingAnywhere(message, ChatFormatting.YELLOW)) {
            fishingCatch = Fish.getByName(name);
        } else if (Components.hasFormattingAnywhere(message, ChatFormatting.DARK_GREEN)) {
            fishingCatch = Plant.getByName(name);
        } else if (Components.hasFormattingAnywhere(message, ChatFormatting.AQUA)) {
            fishingCatch = Creature.getByName(name);
        }

        if (fishingCatch != null) {
            HypixelServerEvents.FISHING_CATCH.invoker().onCatch(fishingCatch);
        } else {
            Minecraft.getInstance().player.displayClientMessage(Component.literal("[MEH] Detected a generic catch but couldn't map to constant: \"" + name + "\"").withStyle(ChatFormatting.RED), false);
        }
    }

    private static void handleFishingCaughtMythicalFish(MythicalFishCatchData data) {
        MythicalFishType fishType = MythicalFishType.getByName(data.name());
        if (fishType != null) {
            HypixelServerEvents.FISHING_CATCH.invoker().onCatch(new MythicalFish(fishType, data.weight()));
        } else {
            Minecraft.getInstance().player.displayClientMessage(Component.literal("[MEH] Detected a MythicalFishType catch but couldn't map to constant: \"" + data.name() + "\"").withStyle(ChatFormatting.RED), false);
        }
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

        public boolean handle(Component text, String strippedText);

    }

    private static final class ExtractorHandler<T> implements ChatHandler {

        private final ChatExtractor<T> extractor;
        private final BiConsumer<Component, T> action;

        private ExtractorHandler(ChatExtractor<T> extractor, BiConsumer<Component, T> action) {
            this.extractor = extractor;
            this.action = action;
        }

        private ExtractorHandler(ChatExtractor<T> extractor, Consumer<T> action) {
            this(extractor, (message, strippedMessage) -> action.accept(strippedMessage));
        }

        @Override
        public boolean handle(Component text, String strippedText) {
            Optional<T> data = extractor.extract(strippedText);
            if (data.isEmpty()) {
                return false;
            }

            this.action.accept(text, data.get());
            return true;
        }

    }

    private static final class MatcherHandler implements ChatHandler {

        private final ChatMatcher matcher;
        private final Consumer<Component> action;

        private MatcherHandler(ChatMatcher matcher, Consumer<Component> action) {
            this.matcher = matcher;
            this.action = action;
        }

        private MatcherHandler(ChatMatcher matcher, Runnable action) {
            this(matcher, message -> action.run());
        }

        @Override
        public boolean handle(Component text, String strippedText) {
            boolean success = matcher.matches(strippedText);
            if (success) {
                this.action.accept(text);
            }
            return success;
        }

    }

}
