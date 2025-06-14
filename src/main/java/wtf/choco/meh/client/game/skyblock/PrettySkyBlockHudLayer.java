package wtf.choco.meh.client.game.skyblock;

import com.mojang.blaze3d.vertex.PoseStack;

import java.text.NumberFormat;

import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import wtf.choco.meh.client.MEHClient;

public final class PrettySkyBlockHudLayer implements IdentifiedLayer {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, "pretty_skyblock_hud");

    private static final ResourceLocation SPRITE_ARMOR_FULL = ResourceLocation.withDefaultNamespace("hud/armor_full"); // Vanilla sprite
    private static final ResourceLocation SPRITE_HEALTH_CONTAINER = ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, "hud/health_container");
    private static final ResourceLocation SPRITE_HEALTH_FULL = ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, "hud/health_full");
    private static final ResourceLocation SPRITE_MANA_CONTAINER = ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, "hud/mana_container");
    private static final ResourceLocation SPRITE_MANA_FULL = ResourceLocation.fromNamespaceAndPath(MEHClient.MOD_ID, "hud/mana_full");

    private static final NumberFormat FORMAT = NumberFormat.getIntegerInstance();

    private static final float TEXT_SCALE = 0.7F;

    private static final int HEALTH_BAR_ICON_SIZE = 9;
    private static final int HEALTH_BAR_WIDTH = 85;
    private static final int HEALTH_BAR_HEIGHT = 9;

    private static final int DEFENSE_ICON_SIZE = 9;

    private static final int MANA_BAR_ICON_SIZE = 9;
    private static final int MANA_BAR_WIDTH = 85;
    private static final int MANA_BAR_HEIGHT = 9;

    private final SkyBlockPrettyHudFeature feature;

    public PrettySkyBlockHudLayer(SkyBlockPrettyHudFeature feature) {
        this.feature = feature;
    }

    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        if (!feature.isEnabled()) {
            return;
        }

        // Health bar
        Font font = Minecraft.getInstance().font;
        int halfLineHeight = font.lineHeight / 2;

        int healthBarX = (graphics.guiWidth() / 2) - 94;
        int healthBarY = graphics.guiHeight() - 40;
        graphics.blitSprite(RenderType::guiTextured, SPRITE_HEALTH_CONTAINER, healthBarX, healthBarY, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        float healthProgress = Mth.clamp(((HEALTH_BAR_ICON_SIZE / 2.0F) + feature.getCurrentHealth()) / Math.max(feature.getMaxHealth(), 1), 0.0F, 1.0F);
        graphics.blitSprite(RenderType::guiTextured, SPRITE_HEALTH_FULL, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT, 0, 0, healthBarX, healthBarY, Mth.floor(healthProgress * HEALTH_BAR_WIDTH), HEALTH_BAR_HEIGHT);

        Component healthText = Component.literal(FORMAT.format(feature.getCurrentHealth()) + "/" + FORMAT.format(feature.getMaxHealth())).withStyle(ChatFormatting.WHITE);
        int healthTextWidth = font.width(healthText);
        int healthBarTextX = healthBarX + ((HEALTH_BAR_ICON_SIZE + HEALTH_BAR_WIDTH - healthTextWidth) / 2);
        int healthBarTextY = healthBarY + 1;

        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(healthBarTextX + (healthTextWidth / 2), healthBarTextY + halfLineHeight, 0);
        pose.scale(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);
        pose.translate(-healthBarTextX - (healthTextWidth / 2), -healthBarTextY - halfLineHeight, 0);
        graphics.drawString(font, healthText, healthBarTextX, healthBarTextY, 0xFFFFFFFF);
        pose.popPose();

        // Defense
        int defenseIconX = healthBarX;
        int defenseIconY = healthBarY - DEFENSE_ICON_SIZE - 2;
        graphics.blitSprite(RenderType::guiTextured, SPRITE_ARMOR_FULL, defenseIconX, defenseIconY, DEFENSE_ICON_SIZE, DEFENSE_ICON_SIZE);

        Component defenseText = Component.literal(FORMAT.format(feature.getCurrentDefense()));
        int defenseTextX = defenseIconX + DEFENSE_ICON_SIZE + 2;
        int defenseTextY = defenseIconY + 1;
        pose.pushPose();
        pose.translate(defenseTextX, defenseTextY + halfLineHeight, 0);
        pose.scale(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);
        pose.translate(-defenseTextX, -defenseTextY - halfLineHeight, 0);
        graphics.drawString(font, defenseText, defenseTextX, defenseTextY, 0xFFFFFFFF);
        pose.popPose();

        // Mana bar
        int manaBarX = (graphics.guiWidth() / 2) + 94 - MANA_BAR_WIDTH - 1;
        int manaBarY = graphics.guiHeight() - 40;
        graphics.blitSprite(RenderType::guiTextured, SPRITE_MANA_CONTAINER, manaBarX, manaBarY, MANA_BAR_WIDTH, MANA_BAR_HEIGHT);
        float manaProgress = Mth.clamp(((MANA_BAR_ICON_SIZE / 2.0F) + feature.getCurrentMana()) / Math.max(feature.getMaxMana(), 1), 0.0F, 1.0F);

        graphics.enableScissor(manaBarX + Mth.floor((1.0F - manaProgress) * MANA_BAR_WIDTH), manaBarY, manaBarX + MANA_BAR_WIDTH, manaBarY + HEALTH_BAR_HEIGHT);
        graphics.blitSprite(RenderType::guiTextured, SPRITE_MANA_FULL, MANA_BAR_WIDTH, MANA_BAR_HEIGHT, 0, 0, manaBarX, manaBarY, MANA_BAR_WIDTH, MANA_BAR_HEIGHT);
        graphics.disableScissor();

        Component manaText = Component.literal(FORMAT.format(feature.getCurrentMana()) + "/" + FORMAT.format(feature.getMaxMana())).withStyle(ChatFormatting.WHITE);
        int manaTextWidth = font.width(healthText);
        int manaBarTextX = manaBarX + ((MANA_BAR_WIDTH - manaTextWidth) / 2);
        int manaBarTextY = manaBarY + 1;

        pose.pushPose();
        pose.translate(manaBarTextX + (manaTextWidth / 2), manaBarTextY + halfLineHeight, 0);
        pose.scale(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);
        pose.translate(-manaBarTextX - (manaTextWidth / 2), -manaBarTextY - halfLineHeight, 0);
        graphics.drawString(font, manaText, manaBarTextX, manaBarTextY, 0xFFFFFFFF);
        pose.popPose();
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

}
