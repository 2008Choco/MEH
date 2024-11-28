package wtf.choco.meh.client.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.yggdrasil.ProfileResult;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;

import org.jetbrains.annotations.Blocking;

import wtf.choco.meh.client.MEHClient;

public final class PlayerInfoCache {

    private static final LoadingCache<UUID, PlayerInfo> CACHED_PLAYER_INFO = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build(new CacheLoader<>() {

                @Override
                public PlayerInfo load(UUID playerUUID) throws Exception {
                    Minecraft minecraft = Minecraft.getInstance();

                    // Try to get it from the server first so we don't have to make a session service query
                    PlayerInfo playerInfo = minecraft.player.connection.getPlayerInfo(playerUUID);
                    if (playerInfo != null) {
                        MEHClient.LOGGER.info("Found " + playerUUID + "'s info from the server!");
                        return playerInfo;
                    }

                    // Now we have to do a session query
                    MEHClient.LOGGER.info("Querying for " + playerUUID + "'s info from session servers!");
                    ProfileResult result = Minecraft.getInstance().getMinecraftSessionService().fetchProfile(playerUUID, true);
                    return new PlayerInfo(result.profile(), false);
                }

            });

    private PlayerInfoCache() { }

    @Blocking
    public static PlayerInfo getPlayerInfo(UUID playerUUID) {
        return CACHED_PLAYER_INFO.getUnchecked(playerUUID);
    }

}
