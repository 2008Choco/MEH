package wtf.choco.meh.client.mixin;

import net.minecraft.ChatFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChatFormatting.class)
public interface ChatFormattingAccessor {

    @Accessor
    public char getCode();

}
