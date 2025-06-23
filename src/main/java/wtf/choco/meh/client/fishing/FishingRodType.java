package wtf.choco.meh.client.fishing;

import java.util.function.Predicate;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.util.Components;

import static wtf.choco.meh.client.util.Components.emptyNonItalic;

/**
 * A type of fishing rod used in main lobby fishing on Hypixel.
 */
public enum FishingRodType implements Predicate<Component> {

    /*
     * Alternate display names are used for situations where the display name differs in different environments,
     * such as in inventories, where additional formatting is applied.
     */

    FISHING_ROD_3000(emptyNonItalic()
            .append(Component.literal("Fishing Rod ").withStyle(ChatFormatting.GOLD))
            .append(Component.literal("3000").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)),
        emptyNonItalic()
            .append(Component.empty().withStyle(ChatFormatting.GREEN))
            .append(Component.literal("Fishing Rod ").withStyle(ChatFormatting.GOLD))
            .append(Component.literal("3000").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD))
    ),
    INAUGURAL_ICE_FISHING_ROD("Inaugural Ice Fishing Rod", ChatFormatting.AQUA),
    SPRINGTIME_FISHING_ROD("Springtime Fishing Rod", ChatFormatting.YELLOW),
    HAUNTED_FISHING_ROD("Haunted Fishing Rod", ChatFormatting.DARK_PURPLE),
    FESTIVE_FISHING_ROD("Festive Fishing Rod", ChatFormatting.RED),
    SOLAR_FISHING_ROD("Solar Fishing Rod", ChatFormatting.GOLD);

    private static final String MODEL_PREFIX = "item/";
    private static final String TEXTURE_LOCATION_PREFIX = "item/fishing_rod/";

    private ResourceLocation itemModelLocation;
    private ResourceLocation modelLocation, castModelLocation;
    private ResourceLocation textureLocation, castTextureLocation;

    private final Component[] displayNames;

    private FishingRodType(Component... displayNames) {
        this.displayNames = displayNames;
    }

    private FishingRodType(String displayName, ChatFormatting color) {
        this(
            emptyNonItalic().append(Component.literal(displayName).withStyle(color)),
            emptyNonItalic().append(Component.empty().withStyle(ChatFormatting.GREEN)).append(Component.literal(displayName).withStyle(color))
        );
    }

    @Override
    public boolean test(Component displayName) {
        return Components.anyMatch(displayNames, displayName);
    }

    /**
     * Get the item model location of the fishing rod ({@literal assets/<mod>/items}).
     *
     * @return the model location
     */
    public ResourceLocation getItemModelLocation() {
        if (itemModelLocation == null) {
            this.itemModelLocation = MEHClient.key(name().toLowerCase());
        }

        return itemModelLocation;
    }

    /**
     * Get the model location of the fishing rod ({@literal assets/<mod>/models/item}).
     *
     * @return the model location
     */
    public ResourceLocation getModelLocation() {
        if (modelLocation == null) {
            this.modelLocation = getItemModelLocation().withPrefix(MODEL_PREFIX);
        }

        return modelLocation;
    }

    /**
     * Get the model location of the cast fishing rod ({@literal assets/<mod>/models/item}).
     *
     * @return the model location
     */
    public ResourceLocation getCastModelLocation() {
        if (castModelLocation == null) {
            this.castModelLocation = getModelLocation().withSuffix("_cast");
        }

        return castModelLocation;
    }

    /**
     * Get the texture location of the uncast fishing rod.
     *
     * @return the texture location
     */
    public ResourceLocation getTextureLocation() {
        if (textureLocation == null) {
            this.textureLocation = MEHClient.key(name().toLowerCase()).withPrefix(TEXTURE_LOCATION_PREFIX);
        }

        return textureLocation;
    }

    /**
     * Get the texture location of the cast fishing rod.
     *
     * @return the texture location
     */
    public ResourceLocation getCastTextureLocation() {
        if (castTextureLocation == null) {
            this.castTextureLocation = getTextureLocation().withSuffix("_cast");
        }

        return castTextureLocation;
    }

}
