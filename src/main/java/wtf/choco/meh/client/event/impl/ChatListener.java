package wtf.choco.meh.client.event.impl;

import com.google.common.base.Preconditions;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

import java.util.OptionalLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import wtf.choco.meh.client.event.HypixelServerEvents;

public final class ChatListener {

    private static final String FROM = "From", TO = "To";

    /*
     * (direction) (?rank) (name): (message)
     *
     * From [ADMIN] 2008Choco: message
     * To [MVP++] Player: message
     * From UnrankedPlayer: message
     */
    private static final Pattern PATTERN_MESSAGE = Pattern.compile("^(?<direction>" + FROM + "|" + TO + ")(?:\\s\\[(?<rank>.+)\\])?\\s(?<username>\\w+):\\s*(?<message>.+)$");

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

        Matcher matcher = PATTERN_MESSAGE.matcher(ChatFormatting.stripFormatting(message.getString()));
        if (!matcher.matches()) {
            return;
        }

        String direction = matcher.group("direction");
        String rank = matcher.group("rank");
        String username = matcher.group("username");
        String messageString = matcher.group("message");

        long lastCommunicatedTimestamp = LAST_COMMUNICATED.getLong(username);
        OptionalLong lastCommunicated = lastCommunicatedTimestamp > 0 ? OptionalLong.of(lastCommunicatedTimestamp) : OptionalLong.empty();

        if (FROM.equals(direction)) {
            HypixelServerEvents.PRIVATE_MESSAGE_RECEIVED.invoker().onReceived(username, rank, messageString, lastCommunicated);
        } else if (TO.equals(direction)) {
            HypixelServerEvents.PRIVATE_MESSAGE_SENT.invoker().onSent(username, rank, messageString, lastCommunicated);
        }

        LAST_COMMUNICATED.put(username, System.currentTimeMillis());
    }

}
