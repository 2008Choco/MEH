package wtf.choco.meh.client.gui.contextualbar;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.contextualbar.ContextualBarRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.game.skyblock.SkyBlockPrettyHudFeature;

public final class SkillExperienceBarRenderer implements ContextualBarRenderer {

    private static final Identifier SPRITE_SKILL_EXPERIENCE_BAR_BACKGROUND = MEHClient.key("hud/skill_experience_bar_background");
    private static final Identifier SPRITE_SKILL_EXPERIENCE_BAR_PROGRESS = MEHClient.key("hud/skill_experience_bar_progress");

    private int color;

    private final SkyBlockPrettyHudFeature feature;

    public SkillExperienceBarRenderer(SkyBlockPrettyHudFeature feature) {
        this.feature = feature;
    }

    @Override
    public void renderBackground(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        int x = left(minecraft.getWindow());
        int y = top(minecraft.getWindow());

        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITE_SKILL_EXPERIENCE_BAR_BACKGROUND, x, y, WIDTH, HEIGHT, ARGB.opaque(color));

        int progressWidth = Mth.floor(feature.getCurrentSkillExperienceProgress() * (WIDTH + 1));
        if (progressWidth > 0) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITE_SKILL_EXPERIENCE_BAR_PROGRESS, WIDTH, HEIGHT, 0, 0, x, y, progressWidth, HEIGHT, ARGB.opaque(color));
        }
    }

    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) { }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public boolean shouldRenderExperienceLevel() {
        return false;
    }

}
