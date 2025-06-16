package wtf.choco.meh.client.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import wtf.choco.meh.client.mixinapi.GuiExtensions;

@Mixin(Gui.class)
public abstract class GuiMixin implements GuiExtensions {

    @Unique
    private static boolean hideHealthInformation;

    @Unique
    public void setHideHealthInformation(boolean hideHealthInformation) {
        GuiMixin.hideHealthInformation = hideHealthInformation;
    }

    @SuppressWarnings("unused") // graphics, player, y, armor, maxArmor, x
    @Inject(method = "renderArmor(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/entity/player/Player;IIII)V", at = @At("HEAD"), cancellable = true)
    private static void onRenderArmor(GuiGraphics graphics, Player player, int y, int armor, int maxArmor, int x, CallbackInfo callback) {
        if (hideHealthInformation) {
            callback.cancel();
        }
    }

    @SuppressWarnings("unused") // graphics, player, x, y, lines, regeneratingHeartIndex, maxHealth, lastHealth, health, absorption, blinking
    @Inject(method = "renderHearts(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/entity/player/Player;IIIIFIIIZ)V", at = @At("HEAD"), cancellable = true)
    private void onRenderHearts(GuiGraphics graphics, Player player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo callback) {
        if (hideHealthInformation) {
            callback.cancel();
        }
    }

    @SuppressWarnings("unused") // graphics, player, top, right, callback
    @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true)
    private void onRenderFood(GuiGraphics graphics, Player player, int top, int right, CallbackInfo callback) {
        if (hideHealthInformation) {
            callback.cancel();
        }
    }

}
