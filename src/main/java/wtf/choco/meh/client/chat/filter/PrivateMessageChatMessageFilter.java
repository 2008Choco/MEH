package wtf.choco.meh.client.chat.filter;

import net.minecraft.client.GuiMessage;

import wtf.choco.meh.client.chat.extractor.ChatExtractors;

final class PrivateMessageChatMessageFilter implements ChatMessageFilter {

    private final String username;

    PrivateMessageChatMessageFilter(String username) {
        this.username = username;
    }

    @Override
    public boolean test(GuiMessage message) {
        return ChatExtractors.PRIVATE_MESSAGE.extract(message.content().getString())
            .filter(data -> data.user().username().equals(username))
            .isPresent();
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof PrivateMessageChatMessageFilter other && username.equals(other.username));
    }

}
