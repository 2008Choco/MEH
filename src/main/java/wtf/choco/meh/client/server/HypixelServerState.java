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

import wtf.choco.meh.client.scoreboard.HypixelScoreboard;

/**
 * Represents state information about the currently connected server with respect to Hypixel.
 */
public final class HypixelServerState {

    // [*.]hypixel.net
    private static final Pattern PATTERN_HYPIXEL_IP = Pattern.compile("^(?:\\w+\\.)?hypixel\\.net$", Pattern.CASE_INSENSITIVE);

    private boolean connectedToHypixel = false;
    private HypixelScoreboard scoreboard = null;
    private ServerLocationProvider serverLocationProvider;

    /**
     * Initialize this instance and register listeners required for it to function.
     */
    public void initialize() {
        ClientPlayConnectionEvents.JOIN.register(this::onJoinServer);
        ClientPlayConnectionEvents.DISCONNECT.register(this::onDisconnectFromServer);

        if (FabricLoader.getInstance().isModLoaded("hypixel-mod-api")) {
            this.serverLocationProvider = new HypixelServerLocationProvider();
        } else {
            this.serverLocationProvider = new HypixelScoreboardServerLocationProvider(this);
        }
    }

    @SuppressWarnings("unused")
    private void onJoinServer(ClientPacketListener handler, PacketSender sender, Minecraft minecraft) {
        ServerData server = minecraft.getCurrentServer();
        this.connectedToHypixel = (server != null) && PATTERN_HYPIXEL_IP.matcher(server.ip).matches();
        if (connectedToHypixel) {
            this.setScoreboard(new HypixelScoreboard(), scoreboard -> scoreboard.setAutoRefreshInterval(20));
        }
    }

    @SuppressWarnings("unused")
    private void onDisconnectFromServer(ClientPacketListener handler, Minecraft minecraft) {
        this.connectedToHypixel = false;
        this.setScoreboard(null);
    }

    /**
     * Check whether or not the client is currently connected to the Hypixel server (or any of its
     * environments).
     *
     * @return true if connected to Hypixel, false otherwise
     */
    public boolean isConnectedToHypixel() {
        return connectedToHypixel || FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    private void setScoreboard(@NotNull HypixelScoreboard scoreboard, @NotNull Consumer<HypixelScoreboard> action) {
        Preconditions.checkArgument(scoreboard != null, "scoreboard must not be null");
        this.setScoreboard(scoreboard);
        action.accept(scoreboard);
    }

    private void setScoreboard(@Nullable HypixelScoreboard scoreboard) {
        if (this.scoreboard != null) {
            this.scoreboard.dispose();
        }

        this.scoreboard = scoreboard;
    }

    /**
     * Get the {@link HypixelScoreboard} instance.
     * <p>
     * If the client is not connected to Hypixel (according to {@link #isConnectedToHypixel()}), then
     * this method will return {@code null}.
     *
     * @return the scoreboard, or null if not connected to Hypixel
     */
    @Nullable
    public HypixelScoreboard getScoreboard() {
        return scoreboard;
    }

    /**
     * Get the {@link ServerLocationProvider} to get information about what type of server the client is
     * currently connected to (if connected to Hypixel).
     *
     * @return the server location provider
     *
     * @throws IllegalStateException if not connected to Hypixel
     */
    public ServerLocationProvider getServerLocationProvider() {
        Preconditions.checkState(connectedToHypixel, "Cannot get the server location provider while not connected to Hypixel");
        return serverLocationProvider;
    }

}
