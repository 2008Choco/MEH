package wtf.choco.meh.client.party;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPartyInfoPacket;
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPartyInfoPacket;

public final class HypixelPartyService implements PartyService {

    private CompletableFuture<Party> activePartyQuery;

    public HypixelPartyService() {
        HypixelModAPI.getInstance().createHandler(ClientboundPartyInfoPacket.class, this::handleIncomingPartyInfo);
    }

    @Override
    public CompletableFuture<Party> getCurrentParty() {
        if (activePartyQuery != null) {
            return activePartyQuery;
        }

        CompletableFuture<Party> future = new CompletableFuture<>();
        this.activePartyQuery = future;
        HypixelModAPI.getInstance().sendPacket(new ServerboundPartyInfoPacket());
        return future.orTimeout(5, TimeUnit.SECONDS);
    }

    private void handleIncomingPartyInfo(ClientboundPartyInfoPacket packet) {
        if (activePartyQuery == null) {
            return;
        }

        if (packet.isInParty()) {
            Map<UUID, ClientboundPartyInfoPacket.PartyMember> members = packet.getMemberMap();
            Party party = new Party(packet.getLeader().get(), convert(members));

            CompletableFuture.runAsync(party::resolveMembers).whenComplete((ignore, e) -> {
                if (e != null) {
                    this.activePartyQuery.completeExceptionally(e);
                } else {
                    this.activePartyQuery.complete(party);
                }

                this.activePartyQuery = null;
            });
        } else {
            this.activePartyQuery.complete(null);
            this.activePartyQuery = null;
        }
    }

    private PartyRole convert(ClientboundPartyInfoPacket.PartyRole role) {
        return switch (role) {
            case LEADER -> PartyRole.LEADER;
            case MOD -> PartyRole.MOD;
            case MEMBER -> PartyRole.MEMBER;
            default -> throw new UnsupportedOperationException("ClientboundPartyInfoPacket.PartyRole." + role.name());
        };
    }

    private PartyMember convert(ClientboundPartyInfoPacket.PartyMember member) {
        return new PartyMember(member.getUuid(), convert(member.getRole()));
    }

    private Map<UUID, PartyMember> convert(Map<UUID, ClientboundPartyInfoPacket.PartyMember> members) {
        Map<UUID, PartyMember> convertedMembers = new HashMap<>(members.size());
        members.forEach((memberUUID, member) -> convertedMembers.put(memberUUID, convert(member)));
        return convertedMembers;
    }

}
