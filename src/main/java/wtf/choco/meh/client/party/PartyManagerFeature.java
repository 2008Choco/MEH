package wtf.choco.meh.client.party;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import wtf.choco.meh.client.MEHClient;
import wtf.choco.meh.client.chat.extractor.UserData;
import wtf.choco.meh.client.config.MEHConfig;
import wtf.choco.meh.client.event.HypixelServerEvents;
import wtf.choco.meh.client.feature.Feature;
import wtf.choco.meh.client.keybind.MEHKeybinds;
import wtf.choco.meh.client.screen.widgets.PartyListHudElement;

public final class PartyManagerFeature extends Feature {

    private static final int DEFAULT_PARTY_EXPIRATION_SECONDS = 60;

    private Party party;
    private CompletableFuture<Void> partyRefreshFuture = null;

    private final Deque<PartyInvitation> partyInvitations = new ArrayDeque<>();

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

        HypixelServerEvents.PARTY_INVITED.register(this::onReceivePartyInvite);
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTickEnd);
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

    private void onReceivePartyInvite(UserData inviter) {
        this.flushPartyInvitations();
        long expireTimestamp = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(DEFAULT_PARTY_EXPIRATION_SECONDS);
        this.partyInvitations.addFirst(new PartyInvitation(inviter.username(), expireTimestamp));
    }

    private void onClientTickEnd(Minecraft minecraft) {
        while (MEHKeybinds.PARTY_INVITE_ACCEPT.consumeClick()) {
            this.flushPartyInvitations();

            PartyInvitation mostRecentInvitation = partyInvitations.pollFirst();
            if (mostRecentInvitation == null) {
                minecraft.player.displayClientMessage(Component.translatable("meh.party_manager.invitation.empty").withStyle(ChatFormatting.RED), false);
                continue;
            }

            mostRecentInvitation.accept(minecraft);
            this.partyInvitations.clear();
        }

        while (MEHKeybinds.PARTY_INVITE_DECLINE.consumeClick()) {
            this.flushPartyInvitations();

            PartyInvitation mostRecentInvitation = partyInvitations.pollFirst();
            if (mostRecentInvitation == null) {
                minecraft.player.displayClientMessage(Component.translatable("meh.party_manager.invitation.empty").withStyle(ChatFormatting.RED), false);
                continue;
            }

            Component message = Component.translatable("meh.party_manager.invitation.decline", mostRecentInvitation.username()).withStyle(ChatFormatting.RED);
            minecraft.player.displayClientMessage(message, false);
        }
    }

    private void flushPartyInvitations() {
        long now = System.currentTimeMillis();
        boolean removed = false;
        do {
            PartyInvitation invitation = partyInvitations.peekLast();
            if (invitation == null) {
                break;
            }

            removed = invitation.isExpired(now);
            if (removed) {
                this.partyInvitations.removeLast();
            }
        } while (removed);
    }

    private record PartyInvitation(String username, long expireTimestamp) {

        public boolean isExpired(long now) {
            return now >= expireTimestamp;
        }

        public void accept(Minecraft minecraft) {
            minecraft.player.connection.sendCommand("party accept " + username());
        }

    }

}
