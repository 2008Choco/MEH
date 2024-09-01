package wtf.choco.meh.client.server;

import java.util.function.Consumer;
import java.util.regex.Pattern;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ServerData;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.include.com.google.common.base.Preconditions;

import wtf.choco.meh.client.event.MEHEvents;
import wtf.choco.meh.client.scoreboard.HypixelScoreboard;

public final class HypixelServerState {

    // [*.]hypixel.net
    private static final Pattern PATTERN_HYPIXEL_IP = Pattern.compile("^(?:\\w+\\.)?hypixel\\.net$", Pattern.CASE_INSENSITIVE);

    private boolean connectedToHypixel = false;
    private HypixelScoreboard hypixelScoreboard = null;
    private HypixelServerType hypixelServerType = HypixelServerType.UNKNOWN;

    public void initialize() {
        ClientPlayConnectionEvents.JOIN.register(this::onJoinServer);
        ClientPlayConnectionEvents.DISCONNECT.register(this::onDisconnectFromServer);
        MEHEvents.HYPIXEL_SCOREBOARD_REFRESH.register(this::onRefreshScoreboard);
    }

    @SuppressWarnings("unused")
    private void onJoinServer(ClientPacketListener handler, PacketSender sender, Minecraft minecraft) {
        ServerData server = minecraft.getCurrentServer();
        this.connectedToHypixel = (server != null) && PATTERN_HYPIXEL_IP.matcher(server.ip).matches();
        if (connectedToHypixel) {
            this.setHypixelScoreboard(new HypixelScoreboard(), scoreboard -> scoreboard.setAutoRefreshInterval(20));
        }
    }

    @SuppressWarnings("unused")
    private void onDisconnectFromServer(ClientPacketListener handler, Minecraft minecraft) {
        this.connectedToHypixel = false;
        this.setHypixelScoreboard(null);
        this.hypixelServerType = HypixelServerType.UNKNOWN;
    }

    private void onRefreshScoreboard(HypixelScoreboard scoreboard) {
        this.hypixelServerType = HypixelServerType.getByScoreboardTitle(scoreboard.getTitle());
    }

    public boolean isConnectedToHypixel() {
        return connectedToHypixel || FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    private void setHypixelScoreboard(@NotNull HypixelScoreboard scoreboard, @NotNull Consumer<HypixelScoreboard> action) {
        Preconditions.checkArgument(scoreboard != null, "scoreboard must not be null");
        this.setHypixelScoreboard(scoreboard);
        action.accept(scoreboard);
    }

    private void setHypixelScoreboard(@Nullable HypixelScoreboard scoreboard) {
        if (hypixelScoreboard != null) {
            this.hypixelScoreboard.dispose();
        }

        this.hypixelScoreboard = scoreboard;
    }

    @Nullable
    public HypixelScoreboard getHypixelScoreboard() {
        return hypixelScoreboard;
    }

    public HypixelServerType getHypixelServerType() {
        return hypixelServerType;
    }

}
