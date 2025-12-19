package wtf.choco.meh.client.game.murdermystery;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import wtf.choco.meh.client.feature.Features;
import wtf.choco.meh.client.model.DynamicModelOverride;

final class KnifeModelOverride implements DynamicModelOverride {

    private final KnifeType knifeType;

    KnifeModelOverride(KnifeType knifeType) {
        this.knifeType = knifeType;
    }

    @Override
    public boolean shouldOverride(ItemStack itemStack, @Nullable Level level, @Nullable ItemOwner itemOwner) {
        if (!Features.RETEXTURED_KNIVES.isEnabled()) {
            return false;
        }

        /*
         * If the knife is thrown, it's put on an armour stand, but Murder Mystery doesn't actually give the item its name.
         * All we have to work with is the item's type, so we'll take what we can get!
         */
        if (itemOwner != null) {
            LivingEntity itemOwnerEntity = itemOwner.asLivingEntity();
            if (itemOwnerEntity != null && itemOwnerEntity.getType() == EntityType.ARMOR_STAND && itemStack.is(knifeType.getItem().asItem())) {
                return true;
            }
        }

        // In all other circumstances, test the name
        return knifeType.test(itemStack.getHoverName());
    }

    @Override
    public Identifier getModelLocation() {
        return knifeType.getItemModelLocation();
    }

}
