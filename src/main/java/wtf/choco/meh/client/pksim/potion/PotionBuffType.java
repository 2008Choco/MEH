package wtf.choco.meh.client.pksim.potion;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public enum PotionBuffType {

    EXPERIENCE_50(Items.LAPIS_LAZULI, "meh.pksim.potion.type.experience_50", ChatFormatting.AQUA),
    EXPERIENCE_25(Items.LAPIS_LAZULI,"meh.pksim.potion.type.experience_25", ChatFormatting.GREEN),
    EXPERIENCE_5(Items.LAPIS_LAZULI,"meh.pksim.potion.type.experience_5", ChatFormatting.YELLOW),
    LEATHER_CRUMB_15(Items.COCOA_BEANS, "meh.pksim.potion.type.leather_crumb_15", ChatFormatting.LIGHT_PURPLE);

    private Component name, coloredName;

    private final Item icon;
    private final String translationKey;
    private final ChatFormatting color;

    private PotionBuffType(Item icon, String translationKey, ChatFormatting color) {
        this.icon = icon;
        this.translationKey = translationKey;
        this.color = color;
    }

    public Item getIcon() {
        return icon;
    }

    public Component getName() {
        if (name == null) {
            this.name = Component.translatable(translationKey);
        }

        return name;
    }

    public Component getColoredName() {
        if (coloredName == null) {
           this.coloredName = Component.translatable(translationKey).withStyle(color); 
        }

        return coloredName;
    }

    public ChatFormatting getColor() {
        return color;
    }

}
