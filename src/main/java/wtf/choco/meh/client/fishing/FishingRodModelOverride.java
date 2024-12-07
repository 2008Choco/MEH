package wtf.choco.meh.client.fishing;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import wtf.choco.meh.client.model.DynamicModelOverride;

final class FishingRodModelOverride implements DynamicModelOverride {

    private final RetexturedFishingRodsFeature feature;
    private final FishingRodType fishingRodType;

    FishingRodModelOverride(RetexturedFishingRodsFeature feature, FishingRodType fishingRodType) {
        this.feature = feature;
        this.fishingRodType = fishingRodType;
    }

    @Override
    public boolean shouldOverride(ItemStack itemStack, @Nullable Level level, @Nullable LivingEntity entity) {
        return feature.isEnabled() && fishingRodType.matchesDisplayName(itemStack.getHoverName());
    }

    @Override
    public ResourceLocation getModelLocation() {
        return fishingRodType.getModelLocation();
    }

}
