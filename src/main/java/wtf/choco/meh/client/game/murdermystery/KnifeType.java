package wtf.choco.meh.client.game.murdermystery;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Predicate;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.util.Components;

import static wtf.choco.meh.client.util.Components.emptyNonItalic;

public enum KnifeType implements Predicate<Component> {

    DEFAULT_IRON_SWORD(Items.IRON_SWORD, "Default Iron Sword"),
    TEN_THOUSAND_SPOONS(Items.LAPIS_LAZULI, "10,000 Spoons"),
    BASTED_TURKEY(Items.COOKED_CHICKEN, "Basted Turkey"),
    BIG_BONE(Items.BONE, "Big Bone"),
    BLAZE_ROD(Items.BLAZE_ROD, "Blaze Rod"),
    BLOODY_BRICK(Items.NETHER_BRICK, "Bloody Brick"),
    CAMPFIRE_LEFTOVERS(Items.CHARCOAL, "Campfire Leftovers"),
    CHEAPO_SWORD(Items.STONE_SWORD, "Cheapo Sword"),
    CHEWED_UP_BUSH(Items.DEAD_BUSH, "Chewed Up Bush"),
    DOUBLE_DEATH_SCYTHE(Items.DIAMOND_PICKAXE, "Double Death Scythe"),
    EARTHEN_DAGGER(Items.JUNGLE_SAPLING, "Earthen Dagger"),
    EASTER_BASKET(Items.OAK_BOAT, "Easter Basket"),
    FARMING_IMPLEMENT(Items.GOLDEN_HOE, "Farming Implement"),
    FRAGILE_PLANT(Items.SUGAR_CANE, "Fragile Plant"),
    FRESHLY_FROZEN_BAGUETTE(Items.BREAD, "Freshly Frozen Baguette"),
    FRISBEE(Items.MUSIC_DISC_BLOCKS, "Frisbee"),
    GLISTERING_MELON(Items.GLISTERING_MELON_SLICE, "Glistering Melon"),
    GOLD_DIGGER(Items.GOLDEN_PICKAXE, "Gold Digger"),
    GOLD_SPRAY_PAINTED_SHOVEL(Items.GOLDEN_SHOVEL, "Gold Spray Painted Shovel"),
    GRILLED_STEAK(Items.COOKED_BEEF, "Grilled Steak"),
    GRIMOIRE(Items.BOOK, "Grimoire"),
    ICE_SHARD(Items.PRISMARINE_SHARD, "Ice Shard"),
    JAGGED_KNIFE(Items.QUARTZ, "Jagged Knife"),
    MVP_DIAMOND_SWORD(Items.DIAMOND_SWORD, "MVP Diamond Sword"),
    MOUSE_TRAP(Items.NAME_TAG, "Mouse Trap"),
    ONLY_THE_BEST(Items.DIAMOND_SHOVEL, "Only the Best"),
    PRICKLY(Items.ROSE_BUSH, "Prickly"),
    PUMPKIN_PIE(Items.PUMPKIN_PIE, "Pumpkin Pie"),
    RUDOLPHS_FAVORITE_SNACK(Items.CARROT, "Rudolph's Favorite Snack"),
    RUDOLPHS_NOSE(Items.RED_DYE, "Rudolph's Nose"),
    SALMON(Items.SALMON, "Salmon"),
    SHEARS(Items.SHEARS, "Shears"),
    SHOVEL(Items.IRON_SHOVEL, "Shovel"),
    SHRED(Items.GOLDEN_AXE, "Shred"),
    SOMEWHAT_SHARP_ROCK(Items.FLINT, "Somewhat Sharp Rock"),
    SPARKLY_SNACK(Items.GOLDEN_CARROT, "Sparkly Snack"),
    STAKE(Items.WOODEN_SWORD, "Stake"),
    STICK(Items.STICK, "Stick"),
    STICK_WITH_A_HAT(Items.STONE_SWORD, "Stick with a Hat"),
    SWEET_TREAT(Items.COOKIE, "Sweet Treat"),
    THE_SCYTHE(Items.DIAMOND_HOE, "The Scythe"),
    TIMBER(Items.DIAMOND_AXE, "Timber"),
    UNFORTUNATELY_COLORED_JACKET(Items.LEATHER, "Unfortunately Colored Jacket"),
    VIP_GOLDEN_SWORD(Items.GOLDEN_SWORD, "VIP Golden Sword"),
    WOODEN_AXE(Items.WOODEN_AXE, "Wooden Axe");

    // These are knives I've made textures for... if I haven't made textures for them yet, I don't want to override them
    private static final Set<KnifeType> WHITELIST = EnumSet.of(
            KnifeType.THE_SCYTHE
    );

    private static final Set<KnifeType> BLACKLIST = EnumSet.of(
            KnifeType.DEFAULT_IRON_SWORD,
            KnifeType.BLAZE_ROD,
            KnifeType.GLISTERING_MELON,
            KnifeType.GRILLED_STEAK, // Maybe
            KnifeType.PUMPKIN_PIE,
            KnifeType.SALMON,
            KnifeType.SHEARS,
            KnifeType.SHOVEL,
            KnifeType.STICK,
            KnifeType.WOODEN_AXE
    );

    private static final String MODEL_PREFIX = "item/";
    private static final String TEXTURE_LOCATION_PREFIX = "item/knife/";

    private ResourceLocation itemModelLocation;
    private ResourceLocation modelLocation;
    private ResourceLocation textureLocation;

    private final ItemLike item;
    private final Component[] displayNames;

    private KnifeType(ItemLike item, Component... displayNames) {
        this.item = item;
        this.displayNames = displayNames;
    }

    private KnifeType(ItemLike item, String displayName) {
        this(
            item,
            // The cosmetic shop (lobby)
            emptyNonItalic().append(Component.literal(displayName).withStyle(ChatFormatting.GREEN)), // Owned
            emptyNonItalic().append(Component.literal(displayName).withStyle(ChatFormatting.RED)), // Unowned

            // In-game (all knives are referred to as 'Knife')
            emptyNonItalic().append(Component.literal("Knife").withStyle(ChatFormatting.GREEN))
        );
    }

    public ItemLike getItem() {
        return item;
    }

    public boolean shouldRetexture() {
        return WHITELIST.contains(this) && !BLACKLIST.contains(this);
    }

    @Override
    public boolean test(Component displayName) {
        return Components.anyMatch(displayNames, displayName);
    }

    /**
     * Get the item model location of the knife ({@literal assets/<mod>/items}).
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
     * Get the model location of the knife ({@literal assets/<mod>/models/item}).
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
     * Get the texture location of the knife.
     *
     * @return the texture location
     */
    public ResourceLocation getTextureLocation() {
        if (textureLocation == null) {
            this.textureLocation = MEHClient.key(name().toLowerCase()).withPrefix(TEXTURE_LOCATION_PREFIX);
        }

        return textureLocation;
    }

}
