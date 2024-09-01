package wtf.choco.meh.client.pksim.render;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.pksim.PKSim3Feature;
import wtf.choco.meh.client.pksim.potion.PotionBuff;
import wtf.choco.meh.client.pksim.potion.PotionBuffType;

public final class PKSimHUDOverlay {

    private static final float FLICKER_PERIOD_TICKS = 10.0F;

    private final Map<PotionBuffType, PotionBuff> potionBuffs = new EnumMap<>(PotionBuffType.class);

    private final PKSim3Feature feature;

    public PKSimHUDOverlay(PKSim3Feature feature) {
        this.feature = feature;
    }

    public void render(GuiGraphics graphics, @SuppressWarnings("unused") DeltaTracker delta) {
        if (!feature.isEnabled() || !feature.isOnPKSim3()) {
            return;
        }

        if (!MEHClient.getConfig().isParkourSimulatorPotionOverlayEnabled()) {
            return;
        }

        boolean first = true;
        int offset = 2;
        final int height = 16;

        Minecraft client = Minecraft.getInstance();
        for (PotionBuffType potionType : PotionBuffType.values()) {
            PotionBuff buff = getPotionBuff(potionType);
            if (!buff.isActive()) {
                continue;
            }

            if (!first) {
                offset += 8;
                first = false;
            }

            graphics.renderFakeItem(new ItemStack(potionType.getIcon()), 2, offset);

            int remainingSeconds = buff.getRemainingTicks() / 20;
            int remainingMinutes = remainingSeconds / 60;
            remainingSeconds %= 60;
            String timeFormatted = "(" + String.format("%02d:%02d", remainingMinutes, remainingSeconds) + ")";

            int actualHeight = offset + (client.font.lineHeight / 2);
            graphics.drawString(client.font, potionType.getColoredName(), 20, actualHeight, 0xFFFFFFFF);

            int durationWidthOffset = 20 + client.font.width(potionType.getColoredName()) + client.font.width(" ");
            int durationColor = 0xFFFFFF;
            if (remainingSeconds < 10) {
                float progress = Mth.cos(((FLICKER_PERIOD_TICKS - (buff.getRemainingTicks() % FLICKER_PERIOD_TICKS)) / FLICKER_PERIOD_TICKS) * (float) Math.TAU);
                int gray = Mth.lerpInt(progress, 0xDF, 0xFF);
                durationColor = (gray << 16) | (gray << 8) | gray;
            }

            graphics.drawString(client.font, timeFormatted, durationWidthOffset, actualHeight, (0xFF << 24) | durationColor);
            offset += height;
        }
    }

    public void tick(@SuppressWarnings("unused") Minecraft client) {
        if (!feature.isEnabled() || !feature.isOnPKSim3()) {
            return;
        }

        for (PotionBuffType potionType : PotionBuffType.values()) {
            this.getPotionBuff(potionType).tick();
        }
    }

    public void setPotionBuffDuration(PotionBuffType type, int durationTicks) {
        this.getPotionBuff(type).setRemainingTicks(durationTicks);
    }

    public void clearPotionBuffDurations() {
        this.potionBuffs.values().forEach(buff -> buff.setRemainingTicks(0));
    }

    private PotionBuff getPotionBuff(PotionBuffType type) {
        return potionBuffs.computeIfAbsent(type, ignore -> new PotionBuff());
    }

}
