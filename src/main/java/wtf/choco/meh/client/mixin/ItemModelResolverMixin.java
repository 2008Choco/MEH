package wtf.choco.meh.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import wtf.choco.meh.client.model.DynamicModelOverride;
import wtf.choco.meh.client.model.DynamicModelOverrides;

@Mixin(ItemModelResolver.class)
public class ItemModelResolverMixin {

    @Redirect(
        method = "appendItemLayers(Lnet/minecraft/client/renderer/item/ItemStackRenderState;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"
        )
    )
    public Object redirectGet(ItemStack itemStack, DataComponentType<?> dataComponentType, @Local Level level, @Local LivingEntity livingEntity) {
        for (DynamicModelOverride override : DynamicModelOverrides.get(itemStack.getItem())) {
            if (!override.shouldOverride(itemStack, level, livingEntity)) {
                continue;
            }

            return override.getModelLocation();
        }

        return itemStack.get(dataComponentType);
    }

}
