package wtf.choco.meh.client.mixin;

import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import wtf.choco.meh.client.MEHClient;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {

    @SuppressWarnings("unused") // instance
    @Redirect(
        method = "sendUnattendedCommand(Ljava/lang/String;Lnet/minecraft/client/gui/screens/Screen;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;verifyCommand(Ljava/lang/String;)Lnet/minecraft/client/multiplayer/ClientPacketListener$CommandCheckResult;"
        ),
        require = 1
    )
    private ClientPacketListener.CommandCheckResult onVerifyCommand(ClientPacketListener instance, String command) {
        ClientPacketListener.CommandCheckResult result = verifyCommand(command);
        if (!MEHClient.getConfig().isTrustAllCommands() && MEHClient.getInstance().getHypixelServerState().isConnectedToHypixel()) {
            return result;
        }

        if (result == ClientPacketListener.CommandCheckResult.PERMISSIONS_REQUIRED || result == ClientPacketListener.CommandCheckResult.PARSE_ERRORS) {
            result = ClientPacketListener.CommandCheckResult.NO_ISSUES;
        }
        return result;
    }

    @Shadow
    protected abstract ClientPacketListener.CommandCheckResult verifyCommand(String command);

}
