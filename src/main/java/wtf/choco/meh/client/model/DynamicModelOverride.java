package wtf.choco.meh.client.model;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

/**
 * Represents an object detailing how a model should be overridden in the client.
 * <p>
 * Note that this type, when registered, is invoked VERY frequently and multiple times per frame.
 * Please be cautious with implementations of this class to be as efficient as possible!
 */
public interface DynamicModelOverride {

    /**
     * Check whether or not this model override should apply.
     *
     * @param itemStack the item stack to have its model overridden
     * @param level the level
     * @param entity the entity holding the item (or null if none)
     *
     * @return true if should override and apply the {@link #getModelLocation()}, false otherwise
     */
    public boolean shouldOverride(ItemStack itemStack, @Nullable Level level, @Nullable ItemOwner entity);

    /**
     * Get the {@link Identifier} pointing to the model file to use for the item if
     * {@link #shouldOverride(ItemStack, Level, ItemOwner)} returns true.
     * <p>
     * The returned location should point towards a model that has been loaded by the game and can
     * be recognized by the {@link ModelManager}. If a custom model is to be used (not associated with
     * an existing item), it must be registered via a {@link ModelLoadingPlugin}.
     *
     * @return the override model location
     */
    public Identifier getModelLocation();

}
