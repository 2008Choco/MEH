package wtf.choco.meh.client.mixinapi;

public interface MEHContextualBarRenderer {

    public default boolean shouldExtractExperienceLevel() {
        return true;
    }

}
