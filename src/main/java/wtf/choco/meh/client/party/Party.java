package wtf.choco.meh.client.party;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

public final class Party {

    private UUID leaderUUID;
    private final Map<UUID, PartyMember> members;
    private final Collection<PartyMember> membersView;

    public Party(UUID leaderUUID, Map<UUID, PartyMember> members) {
        this.leaderUUID = leaderUUID;
        this.members = new HashMap<>(members);
        this.membersView = Collections.unmodifiableCollection(this.members.values());
    }

    public void resolveMembers() {
        this.members.values().forEach(PartyMember::resolve);
    }

    public void setLeader(UUID leaderUUID) {
        this.leaderUUID = leaderUUID;
    }

    public UUID getLeader() {
        return leaderUUID;
    }

    public boolean addMember(UUID playerUUID, PartyRole role) {
        if (containsMember(playerUUID)) {
            return false;
        }

        this.members.put(playerUUID, new PartyMember(playerUUID, role));
        return true;
    }

    public boolean removeMember(UUID playerUUID) {
        return members.remove(playerUUID) != null;
    }

    public boolean containsMember(UUID playerUUID) {
        return members.containsKey(playerUUID);
    }

    public Collection<PartyMember> getMembers() {
        return membersView;
    }

    @Nullable
    public PartyRole getRole(UUID playerUUID) {
        PartyMember member = members.get(playerUUID);
        return (member != null) ? member.getRole() : null;
    }

    public boolean setRole(UUID playerUUID, PartyRole role) {
        PartyMember member = members.get(playerUUID);
        if (member == null) {
            return false;
        }

        member.setRole(role);
        return true;
    }

    public int getSize() {
        return members.size();
    }

}
