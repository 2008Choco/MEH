package wtf.choco.meh.client.chat.filter;

import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.chat.GuiMessage;

final class StartsWithChatMessageFilter implements ChatMessageFilter {

    private final String text;

    StartsWithChatMessageFilter(String text) {
        this.text = text;
    }

    @Override
    public boolean test(GuiMessage message) {
        return ChatFormatting.stripFormatting(message.content().getString()).startsWith(text);
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
