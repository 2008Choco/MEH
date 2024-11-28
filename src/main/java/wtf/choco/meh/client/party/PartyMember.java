package wtf.choco.meh.client.party;

import java.util.UUID;

import net.minecraft.client.multiplayer.PlayerInfo;

import org.jetbrains.annotations.Blocking;

import wtf.choco.meh.client.util.PlayerInfoCache;

public final class PartyMember implements Comparable<PartyMember> {

    private boolean resolved = false;
    private PlayerInfo playerInfo;

    private final UUID playerUUID;
    private PartyRole role;

    public PartyMember(UUID playerUUID, PartyRole role) {
        this.playerUUID = playerUUID;
        this.role = role;
    }

    @Blocking
    public void resolve() {
        if (resolved) {
            return;
        }

        this.playerInfo = PlayerInfoCache.getPlayerInfo(playerUUID);
        this.resolved = true;
    }

    public PlayerInfo getPlayerInfo() {
        if (!resolved) {
            this.resolve();
        }

        return playerInfo;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setRole(PartyRole role) {
        this.role = role;
    }

    public PartyRole getRole() {
        return role;
    }

    @Override
    public int compareTo(PartyMember other) {
        return role.compareTo(other.getRole());
    }

}
