package wtf.choco.meh.client.chat.filter;

import java.util.function.Predicate;

import net.minecraft.client.GuiMessage;

@FunctionalInterface
public interface ChatMessageFilter extends Predicate<GuiMessage> {

}
