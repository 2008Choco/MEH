package wtf.choco.meh.client.party;

import java.util.concurrent.CompletableFuture;

public interface PartyService {

    public CompletableFuture<Party> getCurrentParty();

}
