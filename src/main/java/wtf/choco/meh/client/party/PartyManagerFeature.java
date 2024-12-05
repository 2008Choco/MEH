package wtf.choco.meh.client.party;

import java.util.concurrent.CompletableFuture;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.event.GuiEvents;
import wtf.choco.meh.client.event.HypixelServerEvents;
import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.screen.widgets.PartyListWidget;

public final class PartyManagerFeature extends Feature {

    private Party party;
    private CompletableFuture<Void> partyRefreshFuture = null;

    private final PartyListWidget partyList;

    public PartyManagerFeature(MEHClient mod) {
        super(mod, MEHConfig::getPartyManager);

        this.partyList = new PartyListWidget(this);
    }

    @Override
    protected void registerListeners() {
        GuiEvents.ADD_HUD_DRAW_LAYER.register((minecraft, layers) -> {
            layers[0].add(this::renderPartyList);
        });

        HypixelServerEvents.PARTY_DISBAND.register((disbanderRank, disbanderUsername, reason) -> refreshParty());
        HypixelServerEvents.PARTY_MEMBER_DEMOTE.register((rank, username, demoterRank, demoterUsername, role) -> refreshParty());
        HypixelServerEvents.PARTY_MEMBER_JOIN.register((rank, username) -> refreshParty());
        HypixelServerEvents.PARTY_MEMBER_LEAVE.register((rank, username, kicked) -> refreshParty());
        HypixelServerEvents.PARTY_MEMBER_PROMOTE.register((rank, username, promoterRank, promoterUsername, role) -> refreshParty());
        HypixelServerEvents.PARTY_MEMBER_YOINK.register((rank, username, yoinkerRank, yoinkerUsername) -> refreshParty());
        HypixelServerEvents.PARTY_JOIN.register((partyLeaderRank, partyLeaderUsername) -> refreshParty());
        HypixelServerEvents.PARTY_KICKED.register((kickerRank, kickerUsername) -> refreshParty());
        HypixelServerEvents.PARTY_LEAVE.register(this::refreshParty);
        HypixelServerEvents.PARTY_TRANSFER.register((rank, username, transferrerRank, transferrerUsername) -> refreshParty());
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

    @SuppressWarnings("unused")
    private void renderPartyList(GuiGraphics graphics, DeltaTracker delta) {
        Minecraft minecraft = Minecraft.getInstance();
        if (isEnabled() && !minecraft.gui.getDebugOverlay().showDebugScreen()) {
            this.partyList.render(graphics);
        }
    }

}
