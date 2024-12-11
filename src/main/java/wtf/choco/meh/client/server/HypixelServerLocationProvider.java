package wtf.choco.meh.client.server;

import java.util.Optional;

import net.hypixel.data.region.Environment;
import net.hypixel.data.type.GameType;
import net.hypixel.data.type.LobbyType;
import net.hypixel.data.type.ServerType;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundHelloPacket;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;

import wtf.choco.meh.client.event.HypixelServerEvents;

final class HypixelServerLocationProvider implements ServerLocationProvider {

    private HypixelEnvironment environment = HypixelEnvironment.PRODUCTION;
    private HypixelServerType serverType;
    private boolean lobby = false;
    private HypixelServerType lastServerType;
    private boolean lastLobby = false;

    HypixelServerLocationProvider() {
        HypixelModAPI.getInstance().subscribeToEventPacket(ClientboundLocationPacket.class);
        HypixelModAPI.getInstance().createHandler(ClientboundHelloPacket.class, this::onHelloPacket);
        HypixelModAPI.getInstance().createHandler(ClientboundLocationPacket.class, this::onLocationPacket);
    }

    @Override
    public HypixelEnvironment getEnvironment() {
        return environment;
    }

    @Override
    public Optional<HypixelServerType> getServerType() {
        return Optional.ofNullable(serverType);
    }

    @Override
    public boolean isLobby() {
        return lobby;
    }

    private void onHelloPacket(ClientboundHelloPacket packet) {
        this.environment = convert(packet.getEnvironment());
    }

    private void onLocationPacket(ClientboundLocationPacket packet) {
        this.lastServerType = this.serverType;
        this.serverType = packet.getServerType().map(this::convert).orElse(null);
        this.lastLobby = lobby;
        this.lobby = packet.getLobbyName().isPresent();

        HypixelServerEvents.SERVER_LOCATION_CHANGED.invoker().onLocationChange(serverType, lobby, lastServerType, lastLobby);
    }

    private HypixelEnvironment convert(Environment environment) {
        return switch (environment) {
            case PRODUCTION -> HypixelEnvironment.PRODUCTION;
            case BETA -> HypixelEnvironment.BETA;
            case TEST -> HypixelEnvironment.TEST;
            default -> throw new UnsupportedOperationException("Couldn't handle Environment." + environment.name() + ". MEH does not have a matching constant!");
        };
    }

    private HypixelServerType convert(ServerType serverType) {
        if (serverType instanceof LobbyType lobbyType) {
            return switch (lobbyType) {
                case MAIN -> HypixelServerType.MAIN_LOBBY;
                case TOURNAMENT -> HypixelServerType.TOURNAMENT_LOBBY;
                default -> HypixelServerType.UNKNOWN;
            };
        } else if (serverType instanceof GameType gameType) {
            return switch (gameType) {
                case ARCADE -> HypixelServerType.ARCADE;
                case ARENA -> HypixelServerType.UNKNOWN; // Legacy server type (Arena Brawl)
                case BATTLEGROUND -> HypixelServerType.WARLORDS;
                case BEDWARS -> HypixelServerType.BEDWARS;
                case BUILD_BATTLE -> HypixelServerType.BUILD_BATTLE;
                case DUELS -> HypixelServerType.DUELS;
                case GINGERBREAD -> HypixelServerType.TURBO_KART_RACERS;
                case HOUSING -> HypixelServerType.HOUSING;
                case LEGACY -> HypixelServerType.CLASSIC_GAMES;
                case MCGO -> HypixelServerType.COPS_AND_CRIMS;
                case MURDER_MYSTERY -> HypixelServerType.MURDER_MYSTERY;
                case PAINTBALL -> HypixelServerType.PAINTBALL;
                case PIT -> HypixelServerType.PIT;
                case PROTOTYPE -> HypixelServerType.PROTOTYPE;
                case QUAKECRAFT -> HypixelServerType.QUAKECRAFT;
                case REPLAY -> HypixelServerType.REPLAY;
                case SKYBLOCK -> HypixelServerType.SKYBLOCK;
                case SKYCLASH -> HypixelServerType.UNKNOWN; // Legacy server type (Sky Clash)
                case SKYWARS -> HypixelServerType.SKYWARS;
                case SMP -> HypixelServerType.SMP;
                case SPEED_UHC -> HypixelServerType.SPEED_UHC;
                case SUPER_SMASH -> HypixelServerType.SMASH_HEROES;
                case SURVIVAL_GAMES -> HypixelServerType.BLITZ_SURVIVAL_GAMES;
                case TNTGAMES -> HypixelServerType.TNT_GAMES;
                case TRUE_COMBAT -> HypixelServerType.UNKNOWN; // Legacy server type (Crazy Walls)
                case UHC -> HypixelServerType.UHC;
                case VAMPIREZ -> HypixelServerType.VAMPIREZ;
                case WALLS -> HypixelServerType.WALLS;
                case WALLS3 -> HypixelServerType.MEGA_WALLS;
                case WOOL_GAMES -> HypixelServerType.WOOL_GAMES;
                default -> HypixelServerType.UNKNOWN;
            };
        }

        return HypixelServerType.UNKNOWN;
    }

}
