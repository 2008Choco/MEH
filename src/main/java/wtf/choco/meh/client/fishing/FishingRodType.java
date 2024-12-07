package wtf.choco.meh.client.fishing;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import wtf.choco.meh.client.MEHClient;

/**
 * A type of fishing rod used in main lobby fishing on Hypixel.
 */
public enum FishingRodType {

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

    private static final String TEXTURE_LOCATION_PREFIX = "item/fishing_rod/";

    private ResourceLocation modelLocation;
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

    /**
     * Check whether or not the given display name {@link Component} matches the expected display
     * name of this fishing rod type.
     *
     * @param displayName the item display name to compare against
     *
     * @return true if the display name matches this fishing rod type, false otherwise
     */
    public boolean matchesDisplayName(Component displayName) {
        boolean dev = FabricLoader.getInstance().isDevelopmentEnvironment();

        for (Component rodDisplayName : displayNames) {
            /*
             * It's difficult to get coloured items in single player "vanilla", so we're going
             * to just ignore colour codes when in a development environment as a quick band-aid patch.
             */
            if ((dev && displayName.getString().equals(rodDisplayName.getString())) || rodDisplayName.equals(displayName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get the model location of the fishing rod.
     *
     * @return the model location
     */
    public ResourceLocation getModelLocation() {
        if (modelLocation == null) {
            this.modelLocation = ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, name().toLowerCase());
        }

        return modelLocation;
    }

    /**
     * Get the texture location of the uncast fishing rod.
     *
     * @return the texture location
     */
    public ResourceLocation getTextureLocation() {
        if (textureLocation == null) {
            this.textureLocation = ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, name().toLowerCase()).withPrefix(TEXTURE_LOCATION_PREFIX);
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

    private static MutableComponent emptyNonItalic() {
        return Component.empty().withStyle(style -> style.withItalic(false));
    }

}
