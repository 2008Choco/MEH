package wtf.choco.meh.client.fishing;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import wtf.choco.meh.client.feature.Features;
import wtf.choco.meh.client.model.DynamicModelOverride;

final class FishingRodModelOverride implements DynamicModelOverride {

    private final FishingRodType fishingRodType;

    FishingRodModelOverride(FishingRodType fishingRodType) {
        this.fishingRodType = fishingRodType;
    }

    @Override
    public boolean shouldOverride(ItemStack itemStack, @Nullable Level level, @Nullable ItemOwner itemOwner) {
        return Features.RETEXTURED_FISHING_RODS.isEnabled() && fishingRodType.test(itemStack.getHoverName());
    }

    @Override
    public Identifier getModelLocation() {
        return fishingRodType.getItemModelLocation();
    }

}
