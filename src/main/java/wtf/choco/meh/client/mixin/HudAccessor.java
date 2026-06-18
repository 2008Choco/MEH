package wtf.choco.meh.client.mixin;

import net.minecraft.client.gui.Hud;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Hud.class)
public interface HudAccessor {

    @Accessor("overlayMessageString")
    public void setActionBarText(Component text);

    @Nullable
    @Accessor("overlayMessageString")
    public Component getActionBarText();

    @Accessor("overlayMessageTime")
    public int getActionBarTicks();

}
