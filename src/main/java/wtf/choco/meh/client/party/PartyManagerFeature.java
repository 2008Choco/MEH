package wtf.choco.meh.client.party;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.event.HypixelServerEvents;
import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.gui.PartyListHudElement;

public final class PartyManagerFeature extends Feature {

    private Party party;
    private CompletableFuture<Void> partyRefreshFuture = null;

    private final PartyListHudElement partyList;

    public PartyManagerFeature(MEHClient mod) {
        super(mod);
        this.partyList = new PartyListHudElement(this);
    }

    @Override
    protected boolean isFeatureEnabled(MEHConfig config) {
        return config.getPartyManager().isEnabled();
    }

    @Override
    protected void registerListeners() {
        HudElementRegistry.addLast(PartyListHudElement.ID, partyList);

        HypixelServerEvents.PARTY_DISBANDED.register((reason, disbander) -> refreshParty());
        HypixelServerEvents.PARTY_JOINED.register(partyLeader -> refreshParty());
        HypixelServerEvents.PARTY_KICKED.register(kicker -> refreshParty());
        HypixelServerEvents.PARTY_LEFT.register(this::refreshParty);
        HypixelServerEvents.PARTY_MEMBER_BARGED.register(user -> refreshParty()); // Don't need to listen for PARTY_BARGED because this event covers that case
        HypixelServerEvents.PARTY_MEMBER_DEMOTED.register((demoted, demoter, role) -> refreshParty());
        HypixelServerEvents.PARTY_MEMBER_KICKED.register(user -> refreshParty());
        HypixelServerEvents.PARTY_MEMBER_LEFT.register(user -> refreshParty());
        HypixelServerEvents.PARTY_MEMBER_PROMOTED.register((promoted, promoter, role) -> refreshParty());
        HypixelServerEvents.PARTY_TRANSFERED.register((newPartyLeader, transferrer) -> refreshParty());
        HypixelServerEvents.PARTY_USER_JOINED.register(user -> refreshParty());
        HypixelServerEvents.PARTY_USER_YOINKED.register((yoinked, yoinker) -> refreshParty());
    }

    public boolean deleteCachedParty() {
        boolean deleted = (party != null);
        this.party = null;
        return deleted;
    }

    public CompletableFuture<Void> refreshParty() {
        if (partyRefreshFuture == null) {
            this.partyRefreshFuture = getMod().getHypixelServerState().getPartyService().getCurrentParty().handle((party, e) -> {
                this.party = party;
                this.partyRefreshFuture = null;
                return null;
            });
        }

        return partyRefreshFuture;
    }

    public Party getParty() {
        return party;
    }

}
