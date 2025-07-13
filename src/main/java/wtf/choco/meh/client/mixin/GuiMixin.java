package wtf.choco.meh.client.mixin;

import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.contextualbar.ContextualBarRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import wtf.choco.meh.client.event.GuiEvents;
import wtf.choco.meh.client.mixinapi.GuiExtensions;

@Mixin(Gui.class)
public abstract class GuiMixin implements GuiExtensions {

    @Unique
    @Nullable
    private ContextualBarRenderer contextualBarRendererOverride;
    @Final
    @Shadow
    private Pair<Gui.ContextualInfo, ContextualBarRenderer> contextualInfoBar;
    @Final
    @Shadow
    private Map<Gui.ContextualInfo, Supplier<ContextualBarRenderer>> contextualInfoBarRenderers;

    @Final
    @Shadow
    private Component title;
    @Final
    @Shadow
    private Component subtitle;
    @Final
    @Shadow
    private int titleFadeInTime;
    @Final
    @Shadow
    private int titleStayTime;
    @Final
    @Shadow
    private int titleFadeOutTime;
    @Final
    @Shadow
    private int titleTime;

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

    @SuppressWarnings("unused") // graphics
    @Inject(method = "renderVehicleHealth(Lnet/minecraft/client/gui/GuiGraphics;)V", at = @At("HEAD"), cancellable = true)
    private void onRenderVehicleHealth(GuiGraphics graphics, CallbackInfo callback) {
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

    @Redirect(
            method = "renderHotbarAndDecorations(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/gui/Gui;nextContextualInfoState()Lnet/minecraft/client/gui/Gui$ContextualInfo;"
            )
    )
    @SuppressWarnings("unused") // gui
    private Gui.ContextualInfo redirectNextContextualInfoState(Gui gui) {
        Gui.ContextualInfo vanillaInfo = nextContextualInfoState();
        ContextualBarRenderer vanillaRenderer = contextualInfoBarRenderers.get(vanillaInfo).get();

        ContextualBarRenderer rendererOverride = GuiEvents.CONTEXTUAL_BAR_OVERRIDE.invoker().getContextualBarRenderer(vanillaInfo, vanillaRenderer);
        if (rendererOverride != vanillaRenderer) {
            this.contextualBarRendererOverride = rendererOverride;
            return Gui.ContextualInfo.EMPTY; // Use "empty" so vanilla doesn't render a bar. We're going to render our own
        }

        this.contextualBarRendererOverride = null;
        return vanillaInfo;
    }

    @Redirect(
            method = "renderHotbarAndDecorations(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V",
            at = @At(
                value = "INVOKE",
                target = "Lorg/apache/commons/lang3/tuple/Pair;getValue()Ljava/lang/Object;"
            ),
            require = 2
    )
    private Object redirectGetValue(Pair<?, ?> pair) {
        return (contextualBarRendererOverride != null) ? contextualBarRendererOverride : pair.getValue();
    }

    @Redirect(
            method = "renderHotbarAndDecorations(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/gui/contextualbar/ContextualBarRenderer;renderExperienceLevel(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;I)V"
            )
    )
    private void redirectRenderExperienceLevel(GuiGraphics graphics, Font font, int level) {
        ContextualBarRenderer currentRenderer = (contextualBarRendererOverride != null) ? contextualBarRendererOverride : contextualInfoBar.getValue();
        if (currentRenderer != null && currentRenderer.shouldRenderExperienceLevel()) {
            ContextualBarRenderer.renderExperienceLevel(graphics, font, level);
        }
    }

    @SuppressWarnings("unused") // deltaTracker
    @Inject(method = "renderTitle(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V", cancellable = true, at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/gui/Gui;getFont()Lnet/minecraft/client/gui/Font;",
        shift = At.Shift.BEFORE
    ))
    private void onRenderTitle(GuiGraphics graphics, DeltaTracker deltaTracker, CallbackInfo callback) {
        if (!GuiEvents.TITLE_RENDER.invoker().onTitleRender(title, subtitle, titleFadeInTime, titleStayTime, titleFadeOutTime, titleTime)) {
            this.clearTitles();
            callback.cancel();
        }
    }

    @Shadow
    public abstract Gui.ContextualInfo nextContextualInfoState();

    @Shadow
    public abstract void clearTitles();

}
