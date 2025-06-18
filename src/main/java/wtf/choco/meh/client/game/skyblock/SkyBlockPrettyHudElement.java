package wtf.choco.meh.client.game.skyblock;

import java.text.NumberFormat;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.Profiler;

import org.joml.Matrix3x2fStack;

import wtf.choco.meh.client.MEHClient;

public final class SkyBlockPrettyHudElement implements HudElement {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, "pretty_skyblock_hud");

    private static final ResourceLocation SPRITE_ARMOR_FULL = ResourceLocation.withDefaultNamespace("hud/armor_full"); // Vanilla sprite
    private static final ResourceLocation SPRITE_HEALTH_CONTAINER = ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, "hud/health_container");
    private static final ResourceLocation SPRITE_HEALTH_FULL = ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, "hud/health_full");
    private static final ResourceLocation SPRITE_MANA_CONTAINER = ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, "hud/mana_container");
    private static final ResourceLocation SPRITE_MANA_FULL = ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, "hud/mana_full");

    private static final NumberFormat FORMAT = NumberFormat.getIntegerInstance();

    private static final float TEXT_SCALE = 0.7F;
    private static final int OFFSET_FROM_BOTTOM = 40;
    private static final int DISTANCE_FROM_CENTER_OF_HOTBAR = 94;

    private static final int HEALTH_BAR_ICON_SIZE = 9;
    private static final int HEALTH_BAR_WIDTH = 85;
    private static final int HEALTH_BAR_HEIGHT = 9;

    private static final int DEFENSE_ICON_SIZE = 9;

    private static final int MANA_BAR_ICON_SIZE = 9;
    private static final int MANA_BAR_WIDTH = 85;
    private static final int MANA_BAR_HEIGHT = 9;

    private final SkyBlockPrettyHudFeature feature;

    public SkyBlockPrettyHudElement(SkyBlockPrettyHudFeature feature) {
        this.feature = feature;
    }

    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        if (!feature.isEnabled()) {
            return;
        }

        int halfWidth = graphics.guiWidth() / 2;
        int barY = graphics.guiHeight() - OFFSET_FROM_BOTTOM;
        int healthBarX = halfWidth - DISTANCE_FROM_CENTER_OF_HOTBAR;
        int defenseX = healthBarX;
        int defenseY = barY - DEFENSE_ICON_SIZE - 2;
        int manaBarX = halfWidth + DISTANCE_FROM_CENTER_OF_HOTBAR - MANA_BAR_WIDTH - 1;

        Profiler.get().push("healthBar");
        this.renderHealthBar(graphics, healthBarX, barY);
        Profiler.get().popPush("defenseInformation");
        this.renderDefenseInformation(graphics, defenseX, defenseY);
        Profiler.get().popPush("manaBar");
        this.renderManaBar(graphics, manaBarX, barY);
        Profiler.get().pop();
    }

    private void renderHealthBar(GuiGraphics graphics, int x, int y) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITE_HEALTH_CONTAINER, x, y, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        float healthProgress = Mth.clamp(((HEALTH_BAR_ICON_SIZE / 2.0F) + feature.getCurrentHealth()) / Math.max(feature.getMaxHealth(), 1), 0.0F, 1.0F);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITE_HEALTH_FULL, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT, 0, 0, x, y, Mth.floor(healthProgress * HEALTH_BAR_WIDTH), HEALTH_BAR_HEIGHT);

        Component healthText = Component.literal(FORMAT.format(feature.getCurrentHealth()) + "/" + FORMAT.format(feature.getMaxHealth())).withStyle(ChatFormatting.WHITE);
        int healthBarTextX = x + ((HEALTH_BAR_ICON_SIZE + HEALTH_BAR_WIDTH) / 2);
        int healthBarTextY = y + 1;

        this.renderScaledText(graphics, healthText, healthBarTextX, healthBarTextY, TEXT_SCALE, true);
    }

    private void renderDefenseInformation(GuiGraphics graphics, int x, int y) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITE_ARMOR_FULL, x, y, DEFENSE_ICON_SIZE, DEFENSE_ICON_SIZE);

        Component defenseText = Component.literal(FORMAT.format(feature.getCurrentDefense()));
        int defenseTextX = x + DEFENSE_ICON_SIZE + 2;
        int defenseTextY = y + 1;

        this.renderScaledText(graphics, defenseText, defenseTextX, defenseTextY, TEXT_SCALE, false);
    }

    private void renderManaBar(GuiGraphics graphics, int x, int y) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITE_MANA_CONTAINER, x, y, MANA_BAR_WIDTH, MANA_BAR_HEIGHT);
        float manaProgress = Mth.clamp(((MANA_BAR_ICON_SIZE / 2.0F) + feature.getCurrentMana()) / Math.max(feature.getMaxMana(), 1), 0.0F, 1.0F);

        // We have to use scissor here because the filled mana bar texture needs to fill from right to left, which we can't stretch in reverse...
        // So instead we can just render the whole filled sprite and expand the scissor start area leftward as we need it
        graphics.enableScissor(x + Mth.floor((1.0F - manaProgress) * MANA_BAR_WIDTH), y, x + MANA_BAR_WIDTH, y + HEALTH_BAR_HEIGHT);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITE_MANA_FULL, MANA_BAR_WIDTH, MANA_BAR_HEIGHT, 0, 0, x, y, MANA_BAR_WIDTH, MANA_BAR_HEIGHT);
        graphics.disableScissor();

        Component manaText = Component.literal(FORMAT.format(feature.getCurrentMana()) + "/" + FORMAT.format(feature.getMaxMana())).withStyle(ChatFormatting.WHITE);
        int manaBarTextX = x + (MANA_BAR_WIDTH / 2);
        int manaBarTextY = y + 1;

        this.renderScaledText(graphics, manaText, manaBarTextX, manaBarTextY, TEXT_SCALE, true);
    }

    private void renderScaledText(GuiGraphics graphics, Component text, int x, int y, float scale, boolean centered) {
        Font font = Minecraft.getInstance().font;
        int halfLineHeight = (font.lineHeight / 2);
        float xScaleOffset = 0.0F;

        if (centered) {
            float halfWidth = (font.width(text) / 2.0F);
            xScaleOffset = halfWidth;
            x -= halfWidth;
        }

        Matrix3x2fStack pose = graphics.pose();
        pose.pushMatrix();
        pose.translate(x + xScaleOffset, y + halfLineHeight);
        pose.scale(scale);
        pose.translate(-x - xScaleOffset, -y - halfLineHeight);
        graphics.drawString(font, text, x, y, 0xFFFFFFFF);
        pose.popMatrix();
    }

}
