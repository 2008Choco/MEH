package wtf.choco.meh.client.fishing;

import java.util.Set;

import net.minecraft.ChatFormatting;

import wtf.choco.meh.client.util.Translatable;

public interface FishingCatch extends Translatable {

    public CatchType getType();

    public String getSimpleName();

    public Set<FishingEnvironment> getEnvironments();

    public default ChatFormatting getColor() {
        return getType().getColor();
    }

}
