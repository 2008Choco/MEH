package wtf.choco.meh.client.model.property;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.registry.MEHRegistries;

/**
 * @param featureId
 *
 * @deprecated unused, but might be useful later!
 */
public record ItemModelPropertyMEHFeatureEnabled(ResourceLocation featureId) implements ConditionalItemModelProperty {

    public static final MapCodec<ItemModelPropertyMEHFeatureEnabled> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(ResourceLocation.CODEC.fieldOf("feature").forGetter(ItemModelPropertyMEHFeatureEnabled::featureId))
            .apply(instance, ItemModelPropertyMEHFeatureEnabled::new)
    );

    @Override
    public boolean get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int i, ItemDisplayContext context) {
        return MEHRegistries.FEATURE.getOptional(featureId).map(Feature::isEnabled).orElse(false);
    }

    @Override
    public MapCodec<? extends ConditionalItemModelProperty> type() {
        return MAP_CODEC;
    }

}
