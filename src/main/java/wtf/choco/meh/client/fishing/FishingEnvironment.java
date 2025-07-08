package wtf.choco.meh.client.fishing;

import wtf.choco.meh.client.util.Translatable;

public enum FishingEnvironment implements Translatable {

    WATER,
    LAVA,
    ICE;

    private String descriptionKey;

    @Override
    public String getDescriptionKey() {
        if (descriptionKey == null) {
            this.descriptionKey = "meh.hypixel.fishing.environment." + name().toLowerCase();
        }

        return descriptionKey;
    }

}
