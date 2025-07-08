package wtf.choco.meh.client.gui;

import java.text.NumberFormat;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.Profiler;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.fishing.CatchType;
import wtf.choco.meh.client.fishing.FishingStatOverlayFeature;

public final class FishingStatsHudElement implements HudElement {

    public static final ResourceLocation ID = MEHClient.key("fishing_stats");

    private static final String TRANSLATION_KEY_FISH_CAUGHT = "gui.meh.fishing_stats.caught";
    private static final String TRANSLATION_KEY_FISHING_STATS_HEADER = "gui.meh.fishing_stats.header";

    private static final int HORIZONTAL_PADDING = 2;
    private static final int VERTICAL_PADDING = 2;
    private static final int HORIZONTAL_TEXT_PADDING = 4;
    private static final int VERTICAL_TEXT_PADDING = 4;
    private static final int VERTICAL_TEXT_SPACING = 1;

    private final FishingStatOverlayFeature feature;

    public FishingStatsHudElement(FishingStatOverlayFeature feature) {
        this.feature = feature;
    }

    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        if (!feature.isEnabled() || !feature.isDisplayConditionMet()) {
            return;
        }

        Profiler.get().push("fishingStatsHud");

        CatchType[] catchTypes = CatchType.values();
        Component[] lines = new Component[catchTypes.length + 1];

        Minecraft minecraft = Minecraft.getInstance();
        int textHeight = VERTICAL_PADDING + VERTICAL_TEXT_PADDING;
        int endHeight = textHeight + ((minecraft.font.lineHeight + VERTICAL_TEXT_SPACING) * lines.length) + VERTICAL_TEXT_PADDING - VERTICAL_TEXT_SPACING - 2;

        int longestLineWidth = 0;
        lines[0] = Component.translatable(TRANSLATION_KEY_FISHING_STATS_HEADER).withStyle(ChatFormatting.DARK_GRAY);
        for (int i = 0; i < catchTypes.length; i++) {
            CatchType catchType = catchTypes[i];
            int catches = feature.getCaught(catchType);
            Component text = Component.translatable(TRANSLATION_KEY_FISH_CAUGHT, catchType.getDisplayName()).withStyle(ChatFormatting.GRAY)
                    .append(" ")
                    .append(Component.literal(NumberFormat.getIntegerInstance().format(catches)).withStyle(catchType.getColor()));
            lines[i + 1] = text;
            longestLineWidth = Math.max(longestLineWidth, minecraft.font.width(text));
        }

        graphics.fill(
            HORIZONTAL_PADDING,
            VERTICAL_PADDING,
            HORIZONTAL_PADDING + (HORIZONTAL_TEXT_PADDING * 2) + longestLineWidth,
            endHeight,
            minecraft.options.getBackgroundColor(Integer.MIN_VALUE)
        );

        int heightPerLine = (minecraft.font.lineHeight + VERTICAL_TEXT_SPACING);
        for (Component line : lines) {
            graphics.drawString(minecraft.font, line, HORIZONTAL_PADDING + HORIZONTAL_TEXT_PADDING, textHeight, 0xFFFFFFFF);
            textHeight += heightPerLine;
        }

        Profiler.get().pop();
    }

}
