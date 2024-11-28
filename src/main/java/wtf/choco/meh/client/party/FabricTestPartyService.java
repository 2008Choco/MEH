package wtf.choco.meh.client.party;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;

public final class FabricTestPartyService implements PartyService {

    private static final UUID UUID_2008CHOCO = UUID.fromString("73c62196-2af7-463d-8be1-a7a2270f4696");
    private static final UUID UUID_HYPIXEL = UUID.fromString("f7c77d99-9f15-4a66-a87d-c4a51ef30d19");
    private static final UUID UUID_MD_5 = UUID.fromString("af74a02d-19cb-445b-b07f-6866a861f783");

    private Party party;
    private final Supplier<Party> partySupplier;

    public FabricTestPartyService() {
        this.partySupplier = () -> {
            Minecraft minecraft = Minecraft.getInstance();
            UUID playerUUID = minecraft.player.getUUID();

            Map<UUID, PartyMember> members = new HashMap<>();
            this.addMember(members, playerUUID, PartyRole.LEADER);
            this.addMember(members, UUID_2008CHOCO, PartyRole.MOD);
            this.addMember(members, UUID_HYPIXEL, PartyRole.MEMBER);
            this.addMember(members, UUID_MD_5, PartyRole.MEMBER);

            return new Party(playerUUID, members);
        };
    }

    private void addMember(Map<UUID, PartyMember> members, UUID memberUUID, PartyRole role) {
        if (members.containsKey(memberUUID)) {
            return;
        }

        members.put(memberUUID, new PartyMember(memberUUID, role));
    }

    @Override
    public CompletableFuture<Party> getCurrentParty() {
        if (party == null) {
            return CompletableFuture.supplyAsync(partySupplier).thenApply(party -> {
                party.resolveMembers();
                this.party = party;
                return party;
            });
        }

        return CompletableFuture.completedFuture(party);
    }

}
