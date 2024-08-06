package wtf.choco.meh.client.pksim.notification;

import it.unimi.dsi.fastutil.ints.IntIntPair;

import wtf.choco.meh.client.event.PKSimEvents;
import wtf.choco.meh.client.event.PKSimEvents.CoinChange.Reason;
import wtf.choco.meh.client.pksim.PKSim3Feature;
import wtf.choco.meh.client.pksim.PKSimUtils;

public final class ParkourCompletionTitleNotification extends TitleNotification<ParkourCompletionMatchResult> {

    public ParkourCompletionTitleNotification() {
        super(TitleNotificationType.PARKOUR_COMPLETION, ParkourCompletionMatchResult.class);
    }

    @Override
    protected ParkourCompletionMatchResult extract(String title, String subtitle) {
        // Simpler pattern, do it first
        int coins = PKSimUtils.extractCoinsTitle(subtitle);
        if (coins < 0) {
            return ParkourCompletionMatchResult.nonMatching();
        }

        IntIntPair experience = PKSimUtils.extractExperienceTitle(title);
        if (experience == null) {
            return ParkourCompletionMatchResult.nonMatching();
        }

        return new ParkourCompletionMatchResult(experience.firstInt(), experience.secondInt(), coins);
    }

    @Override
    public void handle(PKSim3Feature feature, ParkourCompletionMatchResult match) {
        PKSimEvents.PARKOUR_COMPLETE.invoker().onParkourComplete();
        PKSimEvents.EXPERIENCE_CHANGE.invoker().onExperienceChange(feature.getCurrentExperience(), match.getCurrentExperience(), feature.getRequiredExperience(), match.getRequiredExperience(), PKSimEvents.ExperienceChange.Reason.PARKOUR_COMPLETION);
        PKSimEvents.COIN_CHANGE.invoker().onCoinChange(feature.getCoins(), match.getCoins(), Reason.PARKOUR_COMPLETION);

        feature.setCurrentExperience(match.getCurrentExperience());
        feature.setRequiredExperience(match.getRequiredExperience());
        feature.setCoins(match.getCoins());
    }

}
