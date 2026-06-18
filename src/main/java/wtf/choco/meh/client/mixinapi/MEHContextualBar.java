package wtf.choco.meh.client.mixinapi;

public interface MEHContextualBar {

    public default boolean shouldExtractExperienceLevel() {
        return true;
    }

}
