package wtf.choco.meh.client.fishing;

import net.minecraft.ChatFormatting;

import wtf.choco.meh.client.util.Translatable;

public enum FishRarity implements Translatable {

    COMMON(ChatFormatting.YELLOW),
    UNCOMMON(ChatFormatting.GREEN),
    RARE(ChatFormatting.AQUA),
    ULTRA_RARE(ChatFormatting.LIGHT_PURPLE);

    private String descriptionKey;

    private final ChatFormatting color;

    private FishRarity(ChatFormatting color) {
        this.color = color;
    }

    public ChatFormatting getColor() {
        return color;
    }

    @Override
    public String getDescriptionKey() {
        if (descriptionKey == null) {
            this.descriptionKey = "meh.hypixel.fishing.rarity." + name().toLowerCase();
        }

        return descriptionKey;
    }

}
