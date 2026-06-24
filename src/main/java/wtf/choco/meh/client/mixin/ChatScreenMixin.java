package wtf.choco.meh.client.mixin;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import wtf.choco.meh.client.mixinapi.CommandInputSupplier;

@Mixin(ChatScreen.class)
public class ChatScreenMixin implements CommandInputSupplier {

    @Shadow
    protected EditBox input;

    @Override
    public boolean isCommandInBuffer() {
        String text = input.getValue();
        return !text.isEmpty() && text.charAt(0) == '/';
    }

}
