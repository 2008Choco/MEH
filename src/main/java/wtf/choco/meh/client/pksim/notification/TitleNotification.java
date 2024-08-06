package wtf.choco.meh.client.pksim.notification;

import org.jetbrains.annotations.Nullable;

import wtf.choco.meh.client.pksim.PKSim3Feature;

public abstract class TitleNotification<T extends MatchResult> {

    private final TitleNotificationType type;
    private final Class<T> matchResultClass;

    public TitleNotification(TitleNotificationType type, Class<T> matchResultClass) {
        this.type = type;
        this.matchResultClass = matchResultClass;
    }

    public TitleNotificationType getType() {
        return type;
    }

    public final T match(@Nullable String title, @Nullable String subtitle) {
        if (title == null) {
            title = "";
        }

        if (subtitle == null) {
            subtitle = "";
        }

        return extract(title, subtitle);
    }

    protected abstract T extract(String title, String subtitle);

    public final void handleMatch(PKSim3Feature feature, MatchResult result) {
        if (!matchResultClass.isInstance(result)) {
            throw new IllegalArgumentException("Don't know how to handle MatchResult of type " + result.getClass().getName() + ". Expected " + matchResultClass.getName());
        }

        this.handle(feature, matchResultClass.cast(result));
    }

    protected abstract void handle(PKSim3Feature feature, T match);

}
