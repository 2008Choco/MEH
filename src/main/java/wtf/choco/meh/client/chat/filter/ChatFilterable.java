package wtf.choco.meh.client.chat.filter;

import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public interface ChatFilterable {

    public default void setChatMessageFilter(@Nullable ChatMessageFilter filter) { }

    @Nullable
    public default ChatMessageFilter getChatMessageFilter() {
        return null;
    }

    public default boolean hasChatMessageFilter() {
        return getChatMessageFilter() != null;
    }

}
