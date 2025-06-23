package wtf.choco.meh.client.mixin;

import net.minecraft.client.resources.sounds.AbstractSoundInstance;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractSoundInstance.class)
public interface AbstractSoundInstanceAccessor {

    @Accessor
    public void setVolume(float volume);

    @Accessor
    public void setX(double x);

    @Accessor
    public void setY(double y);

    @Accessor
    public void setZ(double z);

}
