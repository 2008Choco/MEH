package wtf.choco.meh.client.util;

import net.minecraft.client.gui.screens.ChatScreen;

import wtf.choco.meh.client.mixin.ChatScreenAccessor;

public final class ScreenUtil {

    private ScreenUtil() { }

    public static boolean isWritingCommand(ChatScreen screen) {
        String text = ((ChatScreenAccessor) screen).getInput().getValue();
        return !text.isEmpty() && text.charAt(0) == '/';
    }

}
