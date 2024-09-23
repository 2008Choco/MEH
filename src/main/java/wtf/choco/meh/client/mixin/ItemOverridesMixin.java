package wtf.choco.meh.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.model.CustomModelOverrides;
import wtf.choco.meh.client.model.ModelOverride;

@Mixin(ItemOverrides.class)
public class ItemOverridesMixin {

    @Inject(
        method = "resolve(Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/world/entity/LivingEntity;I)Lnet/minecraft/client/resources/model/BakedModel;",
        at = @At("HEAD"),
        cancellable = true
    )
    @SuppressWarnings("unused")
    public void onResolve(BakedModel originalModel, ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int contextData, CallbackInfoReturnable<BakedModel> callback) {
        for (ModelOverride override : CustomModelOverrides.get(itemStack.getItem())) {
            if (!override.shouldOverride(itemStack, level, entity)) {
                continue;
            }

            ModelManager modelManager = Minecraft.getInstance().getModelManager();
            BakedModel model = modelManager.getModel(override.getModelLocation());

            // TODO: REMOVE
            if (Screen.hasControlDown()) {
                MEHClient.LOGGER.info(override.getModelLocation() + " for item " + itemStack.getItem() + " (found? " + model + ")");
            }

            callback.setReturnValue(model != null ? model : modelManager.getMissingModel());
        }
    }

}
