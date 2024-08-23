package wtf.choco.meh.client.pksim.notification;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.Nullable;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.event.ClientTitleEvents;
import wtf.choco.meh.client.pksim.PKSim3Feature;

public final class TitleNotificationHandler {

    private static final List<TitleNotification<? extends MatchResult>> ALL_NOTIFICATIONS = Arrays.asList(
            new ParkourCompletionTitleNotification(),
            new LevelUpTitleNotification(),
            new PotionBuffTitleNotification()
    );

    private final IntSet unhandleableHashes = new IntOpenHashSet();

    private final MEHClient mod;
    private final PKSim3Feature feature;

    public TitleNotificationHandler(MEHClient mod, PKSim3Feature feature) {
        this.mod = mod;
        this.feature = feature;

        ClientTitleEvents.TITLE_RENDER.register(this::onRenderTitle);
    }

    @SuppressWarnings("unused")
    private boolean onRenderTitle(Component title, @Nullable Component subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks, int titleTicks) {
        if (!mod.isConnectedToHypixel() || !feature.isEnabled() || !feature.isOnPKSim3()) {
            return true;
        }

        /*
         * To prevent spamming RegEx pattern matching over titles we already know aren't ever going to match,
         * we'll remember the hash of title-subtitle combinations so we can skip them ahead of time. A simple
         * hash code calculation is going to be far more performant than doing pattern matching every single
         * frame.
         */
        int titleSubtitleHash = combinationHash(title, subtitle);
        if (unhandleableHashes.contains(titleSubtitleHash)) {
            return true;
        }

        String titleText = ChatFormatting.stripFormatting(title.visit(Optional::ofNullable).orElse(""));
        String subtitleText = (subtitle != null ? ChatFormatting.stripFormatting(subtitle.visit(Optional::ofNullable).orElse("")) : "");

        for (TitleNotification<? extends MatchResult> notification : ALL_NOTIFICATIONS) {
            MatchResult result = notification.match(titleText, subtitleText);
            if (result.matches()) {
                notification.handleMatch(feature, result);
                return false;
            }
        }

        this.unhandleableHashes.add(titleSubtitleHash);
        return true;
    }

    private int combinationHash(@Nullable Component first, @Nullable Component second) {
        int result = 1;
        result = 31 * result + (first != null ? first.hashCode() : 0);
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }

}
