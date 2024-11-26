package wtf.choco.meh.client.server;

import java.util.Optional;

public interface ServerLocationProvider {

    public HypixelEnvironment getEnvironment();

    public Optional<HypixelServerType> getServerType();

    public boolean isLobby();

}
