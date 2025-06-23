package wtf.choco.meh.client.feature;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.mixin.AbstractSoundInstanceAccessor;

public final class QuietThunderFeature extends Feature {

    public QuietThunderFeature(MEHClient mod) {
        super(mod);
    }

    @Override
    protected boolean isFeatureEnabled(MEHConfig config) {
        return config.isQuietThunder();
    }

    @Override
    protected void registerListeners() {
        wtf.choco.meh.client.event.SoundEvents.PLAY.register(this::onPlayThunderSound);
    }

    private void onPlayThunderSound(SoundInstance soundInstance) {
        if (!isEnabled() || !soundInstance.getLocation().equals(SoundEvents.LIGHTNING_BOLT_THUNDER.location())) {
            return;
        }

        AbstractSoundInstanceAccessor soundInstanceAccessor = (AbstractSoundInstanceAccessor) soundInstance;
        Vec3 position = Minecraft.getInstance().player.position();
        soundInstanceAccessor.setX(position.x());
        soundInstanceAccessor.setY(position.y());
        soundInstanceAccessor.setZ(position.z());
        soundInstanceAccessor.setVolume(0.25F);
    }

}
