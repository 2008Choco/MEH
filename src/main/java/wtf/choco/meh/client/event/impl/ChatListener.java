package wtf.choco.meh.client.event.impl;

import com.google.common.base.Preconditions;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

import java.util.OptionalLong;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import wtf.choco.meh.client.chat.extractor.ChatExtractors;
import wtf.choco.meh.client.chat.extractor.PrivateMessageData;
import wtf.choco.meh.client.event.HypixelServerEvents;

public final class ChatListener {

    private static final Object2LongMap<String> LAST_COMMUNICATED = new Object2LongOpenHashMap<>();

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

        ChatExtractors.PRIVATE_MESSAGE.extract(ChatFormatting.stripFormatting(message.getString())).ifPresent(data -> {
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
        });
    }

}
