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
import wtf.choco.meh.client.fishing.FishingState;

public final class FishingStatsHudElement implements HudElement {

    public static final ResourceLocation ID = MEHClient.key("fishing_stats");

    private static final String TRANSLATION_KEY_FISH_CAUGHT = "gui.meh.fishing_stats.caught";
    private static final String TRANSLATION_KEY_FISHING_STATS_HEADER = "gui.meh.fishing_stats.header";
    private static final String TRANSLATION_KEY_FISHING_STATS_INCOMPLETE = "gui.meh.fishing_stats.incomplete";
    private static final String TRANSLATION_KEY_FISHING_STATS_DOCK_MASTER = "gui.meh.fishing_stats.dock_master";

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

        FishingState fishingState = MEHClient.getInstance().getHypixelServerState().getFishingState();
        Component[] lines = fishingState.hasSpokenToDockMaster() ? getStatsLines(fishingState) : getNoStatsLines();

        Minecraft minecraft = Minecraft.getInstance();
        int linesHeight = (minecraft.font.lineHeight * lines.length) + (VERTICAL_TEXT_SPACING * (lines.length - 1));

        int longestLineWidth = 0;
        for (Component line : lines) {
            longestLineWidth = Math.max(longestLineWidth, minecraft.font.width(line));
        }

        int textHeight = VERTICAL_PADDING + VERTICAL_TEXT_PADDING;
        graphics.fill(
            HORIZONTAL_PADDING,
            VERTICAL_PADDING,
            HORIZONTAL_PADDING + (HORIZONTAL_TEXT_PADDING * 2) + longestLineWidth,
            textHeight + linesHeight + VERTICAL_TEXT_PADDING,
            minecraft.options.getBackgroundColor(Integer.MIN_VALUE)
        );

        int heightPerLine = (minecraft.font.lineHeight + VERTICAL_TEXT_SPACING);
        for (Component line : lines) {
            graphics.drawString(minecraft.font, line, HORIZONTAL_PADDING + HORIZONTAL_TEXT_PADDING, textHeight, 0xFFFFFFFF);
            textHeight += heightPerLine;
        }

        Profiler.get().pop();
    }

    private Component[] getNoStatsLines() {
        Component[] lines = new Component[2];
        lines[0] = Component.translatable(TRANSLATION_KEY_FISHING_STATS_HEADER);
        lines[1] = Component.translatable(TRANSLATION_KEY_FISHING_STATS_INCOMPLETE, Component.translatable(TRANSLATION_KEY_FISHING_STATS_DOCK_MASTER).withStyle(ChatFormatting.GREEN)).withStyle(ChatFormatting.GRAY);
        return lines;
    }

    private Component[] getStatsLines(FishingState fishingState) {
        CatchType[] catchTypes = CatchType.values();
        Component[] lines = new Component[catchTypes.length + 1];

        lines[0] = Component.translatable(TRANSLATION_KEY_FISHING_STATS_HEADER).withStyle(ChatFormatting.DARK_GRAY);
        for (int i = 0; i < catchTypes.length; i++) {
            CatchType catchType = catchTypes[i];
            int catches = fishingState.getCaughtCount(catchType);
            Component text = Component.translatable(TRANSLATION_KEY_FISH_CAUGHT, catchType.getDisplayName()).withStyle(ChatFormatting.GRAY)
                    .append(" ")
                    .append(Component.literal(NumberFormat.getIntegerInstance().format(catches)).withStyle(catchType.getColor()));
            lines[i + 1] = text;
        }

        return lines;
    }

}
