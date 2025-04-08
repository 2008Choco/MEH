package wtf.choco.meh.client.server;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

import wtf.choco.meh.client.event.HypixelServerEvents;
import wtf.choco.meh.client.event.MEHEvents;
import wtf.choco.meh.client.scoreboard.HypixelScoreboard;

final class HypixelScoreboardServerLocationProvider implements ServerLocationProvider {

    // 1/2/3  M1B
    private static final Pattern PATTERN_SCOREBOARD_SERVER_ID = Pattern.compile("\\d+\\/\\d+\\/\\d+\\s+(?<server>[\\w\\d]+)");

    private HypixelServerType serverType = null;
    private boolean lobby = false;
    private HypixelServerType lastServerType = null;
    private boolean lastLobby = false;

    private boolean sentServerLocationChangeEvent = false;

    private final HypixelServerState serverState;

    HypixelScoreboardServerLocationProvider(HypixelServerState serverState) {
        this.serverState = serverState;

        ClientPlayConnectionEvents.JOIN.register(this::onJoinServer);
        MEHEvents.HYPIXEL_SCOREBOARD_REFRESH.register(this::onRefreshScoreboard);
    }

    private void onRefreshScoreboard(HypixelScoreboard scoreboard) {
        String title = ChatFormatting.stripFormatting(scoreboard.getTitle());
        this.serverType = HypixelServerType.getByScoreboardTitle(title);

        if (scoreboard.getMaxLine() >= 1) {
            String serverInfoText = ChatFormatting.stripFormatting(scoreboard.getLine(1));
            if (serverInfoText != null) {
                Matcher matcher = PATTERN_SCOREBOARD_SERVER_ID.matcher(serverInfoText);
                this.lobby = matcher.find() && matcher.group("server").startsWith("L");
            } else {
                this.lobby = false;
            }
        } else {
            this.lobby = false;
        }

        if (!sentServerLocationChangeEvent) {
            HypixelServerEvents.SERVER_LOCATION_CHANGED.invoker().onLocationChange(serverType, lobby, lastServerType, lastLobby);
            this.sentServerLocationChangeEvent = true;
        }
    }

    @SuppressWarnings("unused")
    private void onJoinServer(ClientPacketListener handler, PacketSender sender, Minecraft minecraft) {
        this.lastServerType = this.serverType;
        this.serverType = null;
        this.lastLobby = this.lobby;
        this.sentServerLocationChangeEvent = false;
    }

    @Override
    public HypixelEnvironment getEnvironment() {
        return HypixelEnvironment.PRODUCTION;
    }

    @Override
    public Optional<HypixelServerType> getServerType() {
        if (!serverState.isConnectedToHypixel()) {
            return Optional.empty();
        }

        return Optional.ofNullable(serverType);
    }

    @Override
    public boolean isLobby() {
        return lobby;
    }

}
