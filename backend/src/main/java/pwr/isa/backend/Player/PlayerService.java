package pwr.isa.backend.Player; //

public interface PlayerService {
    Player createPlayer(Player player);
    Player getPlayerById(Long userId);
    Player updatePlayer(Player player, Long userId);
    Player patchPlayer(Player player, Long userId);
    void deletePlayer(Long userId);

    Iterable<Player> getAllPlayers();
    boolean existsByUserId(Long userId);
}
