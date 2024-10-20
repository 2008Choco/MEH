package wtf.choco.meh.client.chat;

import java.util.Objects;

import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.include.com.google.common.base.Preconditions;

import wtf.choco.meh.client.chat.filter.ChatMessageFilter;

public final class ChatChannel {

    private final String id;
    private final Component displayName;
    private Component coloredDisplayName;
    private int color;
    private final String commandPrefix;
    private final ChatChannelType type;
    private final ChatMessageFilter messageFilter;

    public ChatChannel(String id, Component displayName, int color, @Nullable String commandPrefix, ChatChannelType type, ChatMessageFilter messageFilter) {
        Preconditions.checkArgument(commandPrefix == null || commandPrefix.length() >= 1, "commandPrefix cannot be empty");

        this.id = id;
        this.displayName = displayName;
        this.coloredDisplayName = displayName.copy().withColor(color);
        this.color = color;
        this.commandPrefix = sanitizeCommandPrefix(commandPrefix);
        this.type = type;
        this.messageFilter = messageFilter;
    }

    public ChatChannel(String id, int color, @Nullable String commandPrefix, ChatChannelType type, ChatMessageFilter filter) {
        this(id, Component.translatable("meh.channel." + id + ".name"), color, commandPrefix, type, filter);
    }

    public String getId() {
        return id;
    }

    public Component getDisplayName(boolean colored) {
        return colored ? coloredDisplayName : displayName;
    }

    public Component getDisplayName() {
        return getDisplayName(false);
    }

    public void setColor(int color) {
        this.color = color;
        this.coloredDisplayName = displayName.copy().withColor(color);
    }

    public int getColor() {
        return color;
    }

    public boolean hasCommandPrefix() {
        return (commandPrefix != null);
    }

    public int getCommandPrefixLength() {
        return hasCommandPrefix() ? commandPrefix.length() : 0;
    }

    @Nullable
    public String getCommandPrefix() {
        return commandPrefix;
    }

    public ChatChannelType getType() {
        return type;
    }

    public boolean isRemovable() {
        return type.isRemovable();
    }

    public ChatMessageFilter getMessageFilter() {
        return messageFilter;
    }

    private String sanitizeCommandPrefix(String prefix) {
        if (prefix == null) {
            return null;
        }

        prefix = prefix.strip();
        if (prefix.isEmpty()) {
            return null;
        }

        if (prefix.charAt(0) == '/') {
            Preconditions.checkArgument(prefix.length() > 1, "commandPrefix cannot be empty");
            prefix = prefix.substring(1);
        }

        prefix = prefix.toLowerCase();
        return prefix;
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandPrefix, displayName);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof ChatChannel other)) {
            return false;
        }

        return displayName.equals(other.displayName) && Objects.equals(commandPrefix, other.commandPrefix);
    }

}
