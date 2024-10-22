package wtf.choco.meh.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import wtf.choco.meh.client.model.CustomModelOverrides;
import wtf.choco.meh.client.model.ModelOverride;

@Mixin(BakedOverrides.class)
public class BakedOverridesMixin {

    @Inject(
        method = "findOverride(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/world/entity/LivingEntity;I)Lnet/minecraft/client/resources/model/BakedModel;",
        at = @At("HEAD"),
        cancellable = true
    )
    @SuppressWarnings("unused")
    public void onFindOverride(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int contextData, CallbackInfoReturnable<BakedModel> callback) {
        for (ModelOverride override : CustomModelOverrides.get(itemStack.getItem())) {
            if (!override.shouldOverride(itemStack, level, entity)) {
                continue;
            }

            ModelManager modelManager = Minecraft.getInstance().getModelManager();
            BakedModel model = modelManager.getModel(override.getModelLocation());

            callback.setReturnValue(model != null ? model : modelManager.getMissingModel());
        }
    }

}
