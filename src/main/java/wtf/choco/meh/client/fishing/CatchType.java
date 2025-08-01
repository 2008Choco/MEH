package wtf.choco.meh.client.fishing;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import wtf.choco.meh.client.util.Translatable;

public enum CatchType implements Translatable {

    FISH(ChatFormatting.YELLOW),
    JUNK(ChatFormatting.RED),
    TREASURE(ChatFormatting.GREEN),
    PLANTS(ChatFormatting.DARK_GREEN),
    CREATURES(ChatFormatting.AQUA),
    SPECIAL_FISH(ChatFormatting.LIGHT_PURPLE), // TODO: No RegEx for this! Never used!
    MYTHICAL_FISH(ChatFormatting.GOLD);

    private String descriptionKey;

    private final ChatFormatting color;

    private CatchType(ChatFormatting color) {
        this.color = color;
    }

    public String getDescriptionKey() {
        if (descriptionKey == null) {
            this.descriptionKey = "meh.hypixel.fishing.catch_type." + name().toLowerCase();
        }

        return descriptionKey;
    }

    public Component getDisplayName() {
        return Component.translatable(getDescriptionKey());
    }

    public ChatFormatting getColor() {
        return color;
    }

}
