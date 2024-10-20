package wtf.choco.meh.client.chat.filter;

import net.minecraft.ChatFormatting;
import net.minecraft.client.GuiMessage;

final class StartsWithChatMessageFilter implements ChatMessageFilter {

    private final String text;

    StartsWithChatMessageFilter(String text) {
        this.text = text;
    }

    @Override
    public boolean test(GuiMessage message) {
        String messageString = ChatFormatting.stripFormatting(message.content().getString());
        return messageString != null && messageString.startsWith(text);
    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof StartsWithChatMessageFilter other && text.equals(other.text));
    }

}
