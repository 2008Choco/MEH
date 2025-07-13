package wtf.choco.meh.client.fishing;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mojang.authlib.properties.Property;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;

import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.Nullable;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.util.ProfileTextures;
import wtf.choco.meh.client.util.Translatable;

public enum MythicalFishType implements Translatable {

    EMBER_OF_HELIOS("Ember of Helios", 25, FishRarity.COMMON, ProfileTextures.TEXTURE_URL_HELIOS),
    DUST_OF_SELENE("Dust of Selene", 25, FishRarity.COMMON, ProfileTextures.TEXTURE_URL_SELENE),
    SHADOW_OF_NYX("Shadow of Nyx", 38, FishRarity.UNCOMMON, ProfileTextures.TEXTURE_URL_NYX),
    HEART_OF_APHRODITE("Heart of Aphrodite", 38, FishRarity.UNCOMMON, ProfileTextures.TEXTURE_URL_APHRODITE),
    SPARK_OF_ZEUS("Spark of Zeus", 50, FishRarity.RARE, ProfileTextures.TEXTURE_URL_ZEUS),
    SPIRIT_OF_DEMETER("Spirit of Demeter", 50, FishRarity.RARE, ProfileTextures.TEXTURE_URL_DEMETER),
    AUTOMATON_OF_DAEDALUS("Automaton of Daedalus", 63, FishRarity.ULTRA_RARE, ProfileTextures.TEXTURE_URL_DAEDALUS, FishingEnvironment.WATER),
    WRATH_OF_HADES("Wrath of Hades", 63, FishRarity.ULTRA_RARE, ProfileTextures.TEXTURE_URL_HADES, FishingEnvironment.LAVA);

    private static final Map<String, MythicalFishType> BY_NAME = new HashMap<>();
    private static final Map<String, MythicalFishType> BY_TEXTURE_URL = new HashMap<>();

    static {
        for (MythicalFishType type : MythicalFishType.values()) {
            BY_NAME.put(type.getName().toLowerCase(), type);
            BY_TEXTURE_URL.put(type.getTextureUrl(), type);
        }
    }

    private String descriptionKey;

    private final String name;
    private final int maxHealth;
    private final FishRarity rarity;
    private final String textureUrl;
    private final Set<FishingEnvironment> environments;

    private MythicalFishType(String name, int maxHealth, FishRarity rarity, String textureUrl, Set<FishingEnvironment> environments) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.rarity = rarity;
        this.textureUrl = textureUrl;
        this.environments = ImmutableSet.copyOf(environments);
    }

    private MythicalFishType(String name, int maxHealth, FishRarity rarity, String textureUrl, FishingEnvironment environment) {
        this(name, maxHealth, rarity, textureUrl, EnumSet.of(environment));
    }

    private MythicalFishType(String name, int maxHealth, FishRarity rarity, String textureUrl) {
        this(name, maxHealth, rarity, textureUrl, EnumSet.allOf(FishingEnvironment.class));
    }

    public String getName() {
        return name;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public FishRarity getRarity() {
        return rarity;
    }

    public String getTextureUrl() {
        return textureUrl;
    }

    public Set<FishingEnvironment> getEnvironments() {
        return environments;
    }

    @Override
    public String getDescriptionKey() {
        if (descriptionKey == null) {
            this.descriptionKey = "meh.hypixel.fishing.mythical_fish." + name().toLowerCase();
        }

        return descriptionKey;
    }

    @Nullable
    public static MythicalFishType getByName(String name) {
        return BY_NAME.get(name.toLowerCase());
    }

    @Nullable
    public static MythicalFishType getByItemStack(ItemStack itemStack) {
        ResolvableProfile profile = itemStack.get(DataComponents.PROFILE);
        if (profile == null) {
            MEHClient.LOGGER.info("Couldn't find minecraft:profile component on itemStack!");
            return null;
        }

        for (Property property : profile.properties().values()) {
            if (!property.name().equals("textures")) {
                MEHClient.LOGGER.info("Property was not \"textures\". Ignoring!");
                continue;
            }

            JsonElement textureJson;
            try {
                String textureValue = property.value();
                String decodedTextureJson = new String(Base64.decodeBase64(textureValue));
                textureJson = JsonParser.parseString(decodedTextureJson);
            } catch (JsonSyntaxException e) {
                MEHClient.LOGGER.error("Failed to parse texture value JSON!", e);
                continue;
            }

            if (textureJson.isJsonObject()) {
                JsonObject texturesJson = textureJson.getAsJsonObject().getAsJsonObject("textures");
                if (texturesJson == null) {
                    MEHClient.LOGGER.warn("Expected \"textures\" in JSON \"{}\" but couldn't find it!", textureJson.toString());
                    continue;
                }

                JsonObject skinJson = texturesJson.getAsJsonObject("SKIN");
                if (skinJson == null) {
                    MEHClient.LOGGER.warn("Expected \"textures.SKIN\" in JSON \"{}\" but couldn't find it!", textureJson.toString());
                    continue;
                }

                JsonElement urlJson = skinJson.get("url");
                if (urlJson == null || !urlJson.isJsonPrimitive()) {
                    MEHClient.LOGGER.warn("Expected \"textures.SKIN.url\" in JSON \"{}\" but couldn't find it!", textureJson.toString());
                    continue;
                }

                return BY_TEXTURE_URL.get(urlJson.getAsString());
            } else {
                MEHClient.LOGGER.warn("Expected JsonObject, received {}", textureJson.getClass().getSimpleName());
            }
        }

        return null;
    }

}
