package wtf.choco.meh.client.mixinapi;

public interface MEHContextualBarRenderer {

    public default boolean shouldRenderExperienceLevel() {
        return true;
    }

}
