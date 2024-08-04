package wtf.choco.meh.client.mixin;

import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientboundSetPlayerTeamPacket.class)
public interface ClientboundSetPlayerTeamPacketAccessor {

    @Accessor
    public int getMethod();

}
