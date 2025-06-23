package wtf.choco.meh.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.resources.sounds.SoundInstance;

public final class SoundEvents {

    public static final Event<Play> PLAY = EventFactory.createArrayBacked(Play.class,
            listeners -> soundInstance -> {
                for (Play event : listeners) {
                    event.onSoundPlay(soundInstance);
                }
            }
    );

    private SoundEvents() { }

    @FunctionalInterface
    public static interface Play {

        public void onSoundPlay(SoundInstance soundInstance);

    }

}
