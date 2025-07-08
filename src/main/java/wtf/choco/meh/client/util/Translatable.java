package wtf.choco.meh.client.util;

import net.minecraft.network.chat.Component;

public interface Translatable {

    public String getDescriptionKey();

    public default Component getDisplayName() {
        return Component.translatable(getDescriptionKey());
    }

}
