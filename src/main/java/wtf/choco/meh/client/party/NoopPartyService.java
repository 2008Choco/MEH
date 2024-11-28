package wtf.choco.meh.client.party;

import java.util.concurrent.CompletableFuture;

public final class NoopPartyService implements PartyService {

    @Override
    public CompletableFuture<Party> getCurrentParty() {
        return CompletableFuture.completedFuture(null);
    }

}
