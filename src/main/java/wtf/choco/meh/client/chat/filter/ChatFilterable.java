package wtf.choco.meh.client.chat.filter;

import org.jetbrains.annotations.Nullable;

/**
 * Represents an element capable of being filtered with a {@link ChatMessageFilter}.
 */
public interface ChatFilterable {

    /**
     * Set the {@link ChatMessageFilter}.
     *
     * @param filter the filter to set, or null to remove any active chat filter
     */
    public default void setChatMessageFilter(@Nullable ChatMessageFilter filter) { }

    /**
     * Get the currently active {@link ChatMessageFilter}.
     *
     * @return the active chat message filter, or null if none
     */
    @Nullable
    public default ChatMessageFilter getChatMessageFilter() {
        return null;
    }

    /**
     * Check whether or not there is an active chat message filter on this object.
     *
     * @return true if a chat message filter is active, false if no filter is applied
     */
    public default boolean hasChatMessageFilter() {
        return getChatMessageFilter() != null;
    }

}
