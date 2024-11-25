package pwr.isa.backend.Player;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Player createPlayer(Player player) {
        if (playerRepository.findByNickname(player.getNickname()) != null) {
            throw new IllegalArgumentException("Player with this nickname already exists");
        }
        return playerRepository.save(player);
    }

    @Override
    public Player getPlayerById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Player with ID " + id + " not found"));
    }

    @Override
    public Player updatePlayer(Player player, Long id) {
        player.setUserId(id);
        if (!playerRepository.existsById(id)) {
            throw new EntityNotFoundException("Player with ID " + id + " not found");
        }
        return playerRepository.save(player);
    }

    @Override
    public void deletePlayer(Long id) {
        if (!playerRepository.existsById(id)) {
            throw new EntityNotFoundException("Player with ID " + id + " not found");
        }
        playerRepository.deleteById(id);
    }

    @Override
    public Iterable<Player> getAllPlayers() {
        return playerRepository.findAll();
    }
}
