package wtf.choco.meh.client.fishing;

import net.minecraft.ChatFormatting;

public enum FishRarity {

    COMMON(ChatFormatting.YELLOW),
    UNCOMMON(ChatFormatting.GREEN),
    RARE(ChatFormatting.AQUA),
    ULTRA_RARE(ChatFormatting.LIGHT_PURPLE);

    private ChatFormatting color;

    private FishRarity(ChatFormatting color) {
        this.color = color;
    }

    public ChatFormatting getColor() {
        return color;
    }

}
