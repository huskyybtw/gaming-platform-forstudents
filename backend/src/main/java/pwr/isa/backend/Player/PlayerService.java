package pwr.isa.backend.Player;

import pwr.isa.backend.Consumer.DTO.LeagueDTO;

import java.util.List;

public interface PlayerService {
    Player createPlayer(Player player);
    Player getPlayerById(Long userId);
    Player updatePlayer(Player player, Long userId);
    Player patchPlayer(Player player, Long userId);
    Player refreshPlayer(Long userId);
    void deletePlayer(Long userId);

    boolean existsByUserId(Long userId);

    List<LeagueDTO> getPlayerRank(Long userId);

    List<Player> getAllPlayers(int limit, int offset, String sortBy, String sortDirection);
}
