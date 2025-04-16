package wtf.choco.meh.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import wtf.choco.meh.client.feature.Features;

@Mixin(LightningBolt.class)
public class LightningBoltMixin {

    @Redirect(
        method = "tick()V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V",
            ordinal = 0
        )
    )
    public void redirectPlayLocalSound(Level level, double x, double y, double z, SoundEvent sound, SoundSource source, float volume, float pitch, boolean delay) {
        if (Features.QUIET_THUNDER.isEnabled()) {
            Vec3 position = Minecraft.getInstance().player.position();
            x = position.x;
            y = position.y;
            z = position.z;
            volume = 0.25F;
        }

        level.playLocalSound(x, y, z, sound, source, volume, pitch, delay);
    }

}
