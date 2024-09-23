package wtf.choco.meh.client.model;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;

import java.util.List;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

/**
 * Allows access to overriding item models based on dynamic properties.
 * <p>
 * Use {@link #register(ItemLike, ModelOverride)} to register a custom item model override.
 */
public final class CustomModelOverrides {

    private static final ListMultimap<Item, ModelOverride> OVERRIDES_BY_ITEM_TYPE = MultimapBuilder.hashKeys().arrayListValues().build();

    private CustomModelOverrides() { }

    /**
     * Register a {@link ModelOverride} for the given item.
     * <p>
     * Multiple model overrides may be registered per item. Priority is dictated by registration order.
     * Overrides that are registered earlier are prioritized over other model overrides.
     *
     * @param item the item for which to register a model override
     * @param override the override to register
     */
    public static void register(ItemLike item, ModelOverride override) {
        OVERRIDES_BY_ITEM_TYPE.put(item.asItem(), override);
    }

    /**
     * Get a {@link List} of all registered {@link ModelOverride ModelOverrides} for the given item.
     *
     * @param item the item whose model overrides to get
     * @return a list of all registered model overrides, or an empty list if none
     */
    public static List<ModelOverride> get(ItemLike item) {
        return OVERRIDES_BY_ITEM_TYPE.get(item.asItem());
    }

}
