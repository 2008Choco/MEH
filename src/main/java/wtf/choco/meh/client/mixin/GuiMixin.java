package wtf.choco.meh.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.LayeredDraw;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import wtf.choco.meh.client.event.GuiEvents;

@Mixin(Gui.class)
public class GuiMixin {

    @Inject(
        method = "<init>(Lnet/minecraft/client/Minecraft;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/LayeredDraw;add(Lnet/minecraft/client/gui/LayeredDraw;Ljava/util/function/BooleanSupplier;)Lnet/minecraft/client/gui/LayeredDraw;",
            shift = At.Shift.BEFORE,
            by = 1
        )
    )
    @SuppressWarnings("unused")
    private void onConstructor(Minecraft minecraft, CallbackInfo callback, @Local(ordinal = 0) LayeredDraw firstLayer, @Local(ordinal = 1) LayeredDraw secondLayer) {
        GuiEvents.ADD_HUD_DRAW_LAYER.invoker().onAddHudDrawLayer(minecraft, firstLayer, secondLayer);
    }

}
