package wtf.choco.meh.client.fishing;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import wtf.choco.meh.client.model.ModelOverride;

final class FishingRodModelOverride implements ModelOverride {

    private static final ResourceLocation CAST_PROPERTY = ResourceLocation.withDefaultNamespace("cast");

    private final RetexturedFishingRodsFeature feature;
    private final FishingRodType fishingRodType;
    private final boolean cast;

    FishingRodModelOverride(RetexturedFishingRodsFeature feature, FishingRodType fishingRodType, boolean cast) {
        this.feature = feature;
        this.fishingRodType = fishingRodType;
        this.cast = cast;
    }

    @Override
    public boolean shouldOverride(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
        if (!feature.isEnabled()) {
            return false;
        }

        return fishingRodType.matchesDisplayName(itemStack.getHoverName()) && (!cast || isRodCast(itemStack, level, entity));
    }

    @Override
    public ResourceLocation getModelLocation() {
        return cast ? fishingRodType.getCastModelLocation() : fishingRodType.getModelLocation();
    }

    @SuppressWarnings("deprecation") // ItemPropertyFunction
    private boolean isRodCast(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
        ItemPropertyFunction property = ItemProperties.getProperty(itemStack, CAST_PROPERTY);
        return property != null && property.call(itemStack, level, entity, 0) >= 1.0F;
    }

}
