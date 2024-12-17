package pwr.isa.backend.Player;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import pwr.isa.backend.User.UserRepository;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service //
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;
    public PlayerServiceImpl(PlayerRepository playerRepository, UserRepository userRepository) {
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Player createPlayer(Player player) {
        // Sprawdź, czy nickname już istnieje
        if (playerRepository.findByNickname(player.getNickname()) != null) {
            throw new IllegalArgumentException("Player with this nickname already exists");
        }

        // Sprawdź, czy userId jest podany i czy istnieje taki użytkownik
        if (player.getUserId() == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        boolean userExists = userRepository.existsById(player.getUserId());
        if (!userExists) {
            throw new EntityNotFoundException("User with ID " + player.getUserId() + " not found");
        }

        // Sprawdź, czy ten userId nie jest już przypisany do innego gracza
        Player playerWithSameUser = playerRepository.findByUserId(player.getUserId());
        if (playerWithSameUser != null) {
            throw new IllegalArgumentException("This user is already assigned to another player");
        }

        // Ustaw lastUpdate na aktualną datę
        player.setLastUpdate(Timestamp.from(Instant.now()));

        return playerRepository.save(player);
    }



//    @Override
//    public Player createPlayer(Player player) {
//        // Sprawdź, czy nickname już istnieje
//        if (playerRepository.findByNickname(player.getNickname()) != null) {
//            throw new IllegalArgumentException("Player with this nickname already exists");
//        }
//
//        // Sprawdź czy userId jest podany
//        Long userId = player.getUserId();
//        if (userId == null) {
//            throw new IllegalArgumentException("UserId cannot be null");
//        }
//
//        // Sprawdź, czy użytkownik o takim ID istnieje
//        boolean userExists = userRepository.existsById(userId);
//        if (!userExists) {
//            throw new EntityNotFoundException("User with ID " + userId + " not found");
//        }
//
//        // Ustaw lastUpdate na aktualną datę
//        player.setLastUpdate(Timestamp.from(Instant.now()));
//
//        return playerRepository.save(player);
//    }


    @Override
    public Player updatePlayer(Player player, Long id) {
        // czy rekord istnieje
        Player existingPlayer = playerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Player with ID " + id + " not found"));

        // czy pole 'user' pozostaje niezmienne
        if (!Objects.equals(existingPlayer.getUserId(), player.getUserId())) {
            throw new IllegalArgumentException("User cannot be changed");
        }

        // walidacja 'nickname'
        if (player.getNickname() == null || player.getNickname().isBlank()) {
            throw new IllegalArgumentException("Nickname cannot be null or empty");// nie potrzebne chyba???
        }
        Player foundPlayer = playerRepository.findByNickname(player.getNickname());
        if (foundPlayer != null && !Objects.equals(foundPlayer.getID(), id)) {
            throw new IllegalArgumentException("Player with this nickname already exists");
        }

        // Ustaw pola z przesłanego obiektu
        existingPlayer.setNickname(player.getNickname()); // Przypisz nickname
        existingPlayer.setOpgg(player.getOpgg());         // Można nadpisać opgg
        existingPlayer.setDescription(player.getDescription()); // Nadpisz description

        //Aktualizacja znacznika czasu
        existingPlayer.setLastUpdate(Timestamp.from(Instant.now()));

        // Zapisz zaktualizowany rekord do bazy danych
        return playerRepository.save(existingPlayer);
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

                // jeśli sourceValue == null, to nie nadpisujemy
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
