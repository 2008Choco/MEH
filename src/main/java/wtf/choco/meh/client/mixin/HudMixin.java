package wtf.choco.meh.client.mixin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.gui.contextualbar.ContextualBar;
import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import wtf.choco.meh.client.event.GuiEvents;
import wtf.choco.meh.client.mixinapi.HudExtensions;

@Mixin(Hud.class)
public abstract class HudMixin implements HudExtensions {

    @Shadow
    private Pair<Hud.ContextualInfo, ContextualBar> contextualInfoBar;
    @Final
    @Shadow
    private Map<Hud.ContextualInfo, Supplier<ContextualBar>> contextualInfoBars;

    @Unique
    private final Map<Hud.ContextualInfo, Supplier<ContextualBar>> meh_customContextualInfoBars = new HashMap<>();
    @Unique
    private static boolean meh_hideHealthInformation;

    @Unique
    @Override
    public void setHideHealthInformation(boolean hideHealthInformation) {
        HudMixin.meh_hideHealthInformation = hideHealthInformation;
    }

    @Inject(
            method = "<init>(Lnet/minecraft/client/Minecraft;)V",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/gui/Hud;resetTitleTimes()V"
            ),
            require = 1
    )
    private void onInit(Minecraft minecraft, CallbackInfo callback) {
        GuiEvents.REGISTER_CONTEXTUAL_INFO.invoker().onRegisterContextualInfo(meh_customContextualInfoBars::put);
    }

    @SuppressWarnings("unused") // graphics, player, y, armor, maxArmor, x
    @Inject(method = "extractArmor(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/world/entity/player/Player;IIII)V", at = @At("HEAD"), cancellable = true)
    private static void onExtractArmor(GuiGraphicsExtractor graphics, Player player, int yLineBase, int numHealthRows, int healthRowHeight, int xLeft, CallbackInfo callback) {
        if (meh_hideHealthInformation) {
            callback.cancel();
        }
    }

    @SuppressWarnings("unused")
    @Inject(method = "extractHearts(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/world/entity/player/Player;IIIIFIIIZ)V", at = @At("HEAD"), cancellable = true)
    private void onExtractHearts(GuiGraphicsExtractor graphics, Player player, int xLeft, int yLineBase, int healthRowHeight, int heartOffsetIndex, float maxHealth, int currentHealth, int oldHealth, int absorption, boolean blink, CallbackInfo callback) {
        if (meh_hideHealthInformation) {
            callback.cancel();
        }
    }

    @SuppressWarnings("unused")
    @Inject(method = "extractVehicleHealth(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V", at = @At("HEAD"), cancellable = true)
    private void onExtractVehicleHealth(GuiGraphicsExtractor graphics, CallbackInfo callback) {
        if (meh_hideHealthInformation) {
            callback.cancel();
        }
    }

    @SuppressWarnings("unused")
    @Inject(method = "extractFood", at = @At("HEAD"), cancellable = true)
    private void onExtractFood(GuiGraphicsExtractor graphics, Player player, int yLineBase, int xRight, CallbackInfo callback) {
        if (meh_hideHealthInformation) {
            callback.cancel();
        }
    }

    @ModifyReturnValue(method = "nextContextualInfoState", at = @At("RETURN"))
    private Hud.ContextualInfo onNextContextualInfoState(Hud.ContextualInfo original) {
        return GuiEvents.CONTEXTUAL_BAR_OVERRIDE.invoker().getContextualBarOverride(original);
    }

    @Redirect(
            method = "extractHotbarAndDecorations(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V",
            at = @At(
                value = "INVOKE",
                target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"
            ),
            require = 1
    )
    private Object redirectGet(Map<?, ?> instance, Object key) {
        // Try to get a custom contextual info bar first
        return meh_customContextualInfoBars.getOrDefault(key, contextualInfoBars.get(key));
    }

    @Redirect(
            method = "extractHotbarAndDecorations(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/gui/contextualbar/ContextualBar;extractExperienceLevel(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/gui/Font;I)V"
            )
    )
    private void redirectExtractExperienceLevel(GuiGraphicsExtractor graphics, Font font, int experienceLevel) {
        ContextualBar contextualBar = contextualInfoBar.getSecond();
        if (contextualBar != null && contextualBar.shouldExtractExperienceLevel()) {
            ContextualBar.extractExperienceLevel(graphics, font, experienceLevel);
        }
    }

}
