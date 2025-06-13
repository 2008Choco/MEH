package wtf.choco.meh.client.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Gui.class)
public interface GuiAccessor {

    @Nullable
    @Accessor("overlayMessageString")
    public Component getActionBarText();

    @Accessor("overlayMessageTime")
    public int getActionBarTicks();

}
