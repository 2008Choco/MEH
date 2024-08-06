package wtf.choco.meh.client.pksim.notification;

import wtf.choco.meh.client.event.PKSimEvents;
import wtf.choco.meh.client.pksim.PKSim3Feature;
import wtf.choco.meh.client.pksim.PKSimUtils;

public final class LevelUpTitleNotification extends TitleNotification<LevelUpMatchResult> {

    public LevelUpTitleNotification() {
        super(TitleNotificationType.LEVEL_UP, LevelUpMatchResult.class);
    }

    @Override
    protected LevelUpMatchResult extract(String title, String subtitle) {
        // Simpler pattern, do it first
        if (!PKSimUtils.isLevelUpTitle(title)) {
            return LevelUpMatchResult.nonMatching();
        }

        int level = PKSimUtils.extractLevelTitle(subtitle);
        if (level < 0) {
            return LevelUpMatchResult.nonMatching();
        }

        return new LevelUpMatchResult(level);
    }

    @Override
    public void handle(PKSim3Feature feature, LevelUpMatchResult match) {
        PKSimEvents.LEVEL_CHANGE.invoker().onLevelChange(feature.getLevel(), match.getLevel(), PKSimEvents.LevelChange.Reason.LEVEL_UP);

        int currentExperience = feature.getCurrentExperience();
        int requiredExperience = feature.getRequiredExperience();
        int newExperience = (currentExperience - requiredExperience);
        int newRequiredExperience = PKSimUtils.calculateRequiredExperienceForLevel(match.getLevel());
        PKSimEvents.EXPERIENCE_CHANGE.invoker().onExperienceChange(currentExperience, newExperience, requiredExperience, newRequiredExperience, PKSimEvents.ExperienceChange.Reason.LEVEL_UP);

        feature.setLevel(match.getLevel());
        feature.setCurrentExperience(newExperience);
        feature.setRequiredExperience(newRequiredExperience);
    }

}
