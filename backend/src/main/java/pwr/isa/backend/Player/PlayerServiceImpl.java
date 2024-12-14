package pwr.isa.backend.Player;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Player createPlayer(Player player) {
        // Sprawdź, czy nickname już istnieje
        if (playerRepository.findByNickname(player.getNickname()) != null) {
            throw new IllegalArgumentException("Player with this nickname already exists");
        }

        // Ustaw lastUpdate na aktualną datę
        player.setLastUpdate(Timestamp.from(Instant.now()));

        return playerRepository.save(player);
    }

    @Override
    public Player updatePlayer(Player player, Long id) {
        player.setID(id);
        if (!playerRepository.existsById(player.getID())) {
            throw new EntityNotFoundException("Player with ID " + player.getID() + " not found");
        }

        // Sprawdzamy czy nick jest unikalny (jeśli jest zmieniony)
        Player foundPlayer = playerRepository.findByNickname(player.getNickname());
        if (foundPlayer != null && !Objects.equals(foundPlayer.getID(), player.getID())) {
            throw new IllegalArgumentException("Player with this nickname already exists");
        }

        // Zaktualizuj lastUpdate
        player.setLastUpdate(Timestamp.from(Instant.now()));

        return playerRepository.save(player);
    }

    @Override
    public Player patchPlayer(Player player, Long id) {
        player.setID(id);
        Optional<Player> target = playerRepository.findById(player.getID());

        if (!target.isPresent()) {
            throw new EntityNotFoundException("Player with ID " + player.getID() + " not found");
        }

        Player existingPlayer = target.get();

        // Iterujemy po polach Player
        for (Field field : Player.class.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object sourceValue = field.get(player);

                // Nie chcemy nadpisywać ID
                if ("ID".equalsIgnoreCase(field.getName())) {
                    continue;
                }

                // Obsługa nickname
                if ("nickname".equalsIgnoreCase(field.getName()) && sourceValue != null) {
                    String newNickname = (String) sourceValue;
                    if (playerRepository.findByNickname(newNickname) != null &&
                            !Objects.equals(existingPlayer.getNickname(), newNickname)) {
                        throw new IllegalArgumentException("Player with this nickname already exists");
                    }
                    field.set(existingPlayer, sourceValue);
                    continue;
                }

                // jeśli sourceValue == null, to nie nadpisujemy pola.
                if (sourceValue != null) {
                    field.set(existingPlayer, sourceValue);
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            }
        }

        // Zaktualizuj lastUpdate po wprowadzeniu zmian
        existingPlayer.setLastUpdate(Timestamp.from(Instant.now()));

        return playerRepository.save(existingPlayer);
    }

    @Override
    public void deletePlayer(Long id) {
        if (!playerRepository.existsById(id)) {
            throw new EntityNotFoundException("Player with ID " + id + " not found");
        }
        playerRepository.deleteById(id);
    }

    @Override
    public Player getPlayerById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Player with ID " + id + " not found"));
    }

    @Override
    public Iterable<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public boolean exists(Long id) {
        return playerRepository.existsById(id);
    }
}
