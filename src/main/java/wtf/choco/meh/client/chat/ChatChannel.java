package wtf.choco.meh.client.chat;

import java.util.Objects;

import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.include.com.google.common.base.Preconditions;

public final class ChatChannel {

    private final String id;
    private final Component displayName;
    private final Component coloredDisplayName;
    private final int color;
    private final String commandPrefix;
    private boolean removable = true;

    public ChatChannel(String id, Component displayName, int color, @Nullable String commandPrefix, boolean removable) {
        Preconditions.checkArgument(commandPrefix == null || commandPrefix.length() >= 1, "commandPrefix cannot be empty");

        this.id = id;
        this.displayName = displayName;
        this.coloredDisplayName = displayName.copy().withColor(color);
        this.color = color;
        this.commandPrefix = sanitizeCommandPrefix(commandPrefix);
        this.removable = removable;
    }

    public ChatChannel(String id, int color, @Nullable String commandPrefix, boolean removable) {
        this(id, Component.translatable("meh.channel." + id + ".name"), color, commandPrefix, removable);
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

    public int getColor() {
        return color;
    }

    public boolean hasCommandPrefix() {
        return (commandPrefix != null);
    }

    @Nullable
    public String getCommandPrefix() {
        return commandPrefix;
    }

    public boolean isRemovable() {
        return removable;
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
