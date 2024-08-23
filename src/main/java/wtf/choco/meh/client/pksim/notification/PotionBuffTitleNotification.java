package wtf.choco.meh.client.pksim.notification;

import wtf.choco.meh.client.pksim.PKSim3Feature;
import wtf.choco.meh.client.pksim.PKSimUtils;
import wtf.choco.meh.client.pksim.potion.PotionBuffType;

public final class PotionBuffTitleNotification extends TitleNotification<PotionBuffMatchResult> {

    public PotionBuffTitleNotification() {
        super(TitleNotificationType.POTION_BUFF, PotionBuffMatchResult.class);
    }

    @Override
    protected PotionBuffMatchResult extract(String title, String subtitle) {
        PotionBuffType type = PKSimUtils.extractPotionBuffTitle(title);
        if (type == null) {
            return PotionBuffMatchResult.nonMatching();
        }

        int durationSeconds = PKSimUtils.extractPotionBuffSubtitle(subtitle);
        if (durationSeconds < 0) {
            return PotionBuffMatchResult.nonMatching();
        }

        return new PotionBuffMatchResult(type, durationSeconds);
    }

    @Override
    protected void handle(PKSim3Feature feature, PotionBuffMatchResult match) {
        feature.getHUDOverlay().setPotionBuffDuration(match.getType(), match.getDurationSeconds() * 20);
    }

}
