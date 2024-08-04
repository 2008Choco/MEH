package wtf.choco.meh.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;

public final class ClientScoreboardEvents {

    public static final Event<AddTeam> ADD_TEAM = EventFactory.createArrayBacked(AddTeam.class,
            listeners -> (scoreboard, teamName, displayName, prefix, suffix, visibility, collisionRule, color) -> {
                for (AddTeam event : listeners) {
                    event.onAddTeam(scoreboard, teamName, displayName, prefix, suffix, visibility, collisionRule, color);
                }
            }
    );

    public static final Event<UpdateTeam> UPDATE_TEAM = EventFactory.createArrayBacked(UpdateTeam.class,
            listeners -> (scoreboard, team, displayName, prefix, suffix, visibility, collisionRule, color) -> {
                for (UpdateTeam event : listeners) {
                    event.onUpdateTeam(scoreboard, team, displayName, prefix, suffix, visibility, collisionRule, color);
                }
            }
    );

    private ClientScoreboardEvents() { }

    @FunctionalInterface
    public interface AddTeam {

        public void onAddTeam(Scoreboard scoreboard, String teamName, Component displayName, Component prefix, Component suffix, Team.Visibility visibility, Team.CollisionRule collisionRule, ChatFormatting color);

    }

    @FunctionalInterface
    public interface UpdateTeam {

        public void onUpdateTeam(Scoreboard scoreboard, PlayerTeam team, Component displayName, Component prefix, Component suffix, Team.Visibility visibility, Team.CollisionRule collisionRule, ChatFormatting color);

    }

}
