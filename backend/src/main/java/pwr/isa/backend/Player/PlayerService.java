package pwr.isa.backend.Player;

public interface PlayerService {
    Player createPlayer(Player player);
    Player getPlayerById(Long id);
    Player updatePlayer(Player player, Long id);
    Player patchPlayer(Player player, Long id);
    void deletePlayer(Long id);
    Iterable<Player> getAllPlayers();
    boolean exists(Long id);
}
