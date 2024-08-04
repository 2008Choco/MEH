package wtf.choco.meh.client.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import wtf.choco.meh.client.event.ClientScoreboardEvents;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    private static final int METHOD_ADD = 0;
    private static final int METHOD_UPDATE = 2;

    @Shadow
    private Scoreboard scoreboard;

    @SuppressWarnings("unused")
    @Inject(method = "handleSetPlayerTeamPacket(Lnet/minecraft/network/protocol/game/ClientboundSetPlayerTeamPacket;)V", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/world/scores/Scoreboard;addPlayerTeam(Ljava/lang/String;)Lnet/minecraft/world/scores/PlayerTeam;",
        shift = At.Shift.BEFORE
    ))
    private void onHandleSetPlayerTeamPacketAddTeam(ClientboundSetPlayerTeamPacket packet, CallbackInfo callback) {
        if (((ClientboundSetPlayerTeamPacketAccessor) packet).getMethod() != METHOD_ADD) {
            return;
        }

        String teamName = packet.getName();

        // Ignore creation of teams that already exist
        if (scoreboard.getPlayerTeam(teamName) != null) {
            return;
        }

        Component displayName = null;
        Component prefix = null;
        Component suffix = null;
        Team.Visibility visibility = null;
        Team.CollisionRule collision = null;
        ChatFormatting color = null;

        ClientboundSetPlayerTeamPacket.Parameters parameters = packet.getParameters().orElse(null);
        if (parameters != null) {
            // Probably won't ever be null but we're going to be safe here
            displayName = parameters.getDisplayName();
            prefix = parameters.getPlayerPrefix();
            suffix = parameters.getPlayerSuffix();
            visibility = Team.Visibility.byName(parameters.getNametagVisibility());
            collision = Team.CollisionRule.byName(parameters.getCollisionRule());
            color = parameters.getColor();
        }

        ClientScoreboardEvents.ADD_TEAM.invoker().onAddTeam(scoreboard, teamName, displayName, prefix, suffix, visibility, collision, color);
    }

    @SuppressWarnings("unused")
    @Inject(method = "handleSetPlayerTeamPacket(Lnet/minecraft/network/protocol/game/ClientboundSetPlayerTeamPacket;)V", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/network/protocol/game/ClientboundSetPlayerTeamPacket;getParameters()Ljava/util/Optional;",
        shift = At.Shift.BEFORE
    ))
    private void onHandleSetPlayerTeamPacketUpdateTeam(ClientboundSetPlayerTeamPacket packet, CallbackInfo callback) {
        if (((ClientboundSetPlayerTeamPacketAccessor) packet).getMethod() != METHOD_UPDATE) {
            return;
        }

        String teamName = packet.getName();

        // Ignore updating of teams that do not exist
        PlayerTeam team = scoreboard.getPlayerTeam(teamName);
        if (team == null) {
            return;
        }

        Component displayName = null;
        Component prefix = null;
        Component suffix = null;
        Team.Visibility visibility = null;
        Team.CollisionRule collision = null;
        ChatFormatting color = null;

        ClientboundSetPlayerTeamPacket.Parameters parameters = packet.getParameters().orElse(null);
        if (parameters != null) {
            // Probably won't ever be null but we're going to be safe here
            displayName = parameters.getDisplayName();
            prefix = parameters.getPlayerPrefix();
            suffix = parameters.getPlayerSuffix();
            visibility = Team.Visibility.byName(parameters.getNametagVisibility());
            collision = Team.CollisionRule.byName(parameters.getCollisionRule());
            color = parameters.getColor();
        }

        ClientScoreboardEvents.UPDATE_TEAM.invoker().onUpdateTeam(scoreboard, team, displayName, prefix, suffix, visibility, collision, color);
    }

}
