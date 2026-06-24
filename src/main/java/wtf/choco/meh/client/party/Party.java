package wtf.choco.meh.client.party;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Party {

    private final Map<UUID, PartyMember> members;
    private final Collection<PartyMember> membersView;

    public Party(Map<UUID, PartyMember> members) {
        this.members = new HashMap<>(members);
        this.membersView = Collections.unmodifiableCollection(this.members.values());
    }

    public void resolveMembers() {
        this.members.values().forEach(PartyMember::resolve);
    }

    public Collection<PartyMember> getMembers() {
        return membersView;
    }

    public int getSize() {
        return members.size();
    }

}
