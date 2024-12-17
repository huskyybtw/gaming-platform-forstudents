package pwr.isa.backend.Player;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import pwr.isa.backend.User.UserRepository;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

/*
    TODO PRZETESTOWAC CZY NAPEWNO DZIALAJA WSZYSTKIE FUNKCJE
*/
@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository, UserRepository userRepository) {
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Player createPlayer(Player player) {
        boolean userExists = userRepository.existsById(player.getUserId());
        Player playerWithSameUser = playerRepository.findByUserId(player.getUserId());

        if (player.getUserId() == null || !userExists || playerWithSameUser != null) {
            throw new IllegalArgumentException("User id is not valid or already assigned to another player");
        }

        if (playerRepository.findByNickname(player.getNickname()) != null) {
            throw new IllegalArgumentException("Player with this nickname already exists");
        }

        player.setLastUpdate(Timestamp.from(Instant.now()));

        return playerRepository.save(player);
    }

    @Override
    public Player updatePlayer(Player player, Long userId) {
        // czy rekord istnieje
        Player existingPlayer = playerRepository.findByUserId(userId);

        if (existingPlayer == null) {
            throw new EntityNotFoundException("Player with user ID " + userId + " not found");
        }

        // czy pole 'user' pozostaje niezmienne
        if (!Objects.equals(existingPlayer.getUserId(), player.getUserId())) {
            throw new IllegalArgumentException("User cannot be changed");
        }

        // walidacja 'nickname'
        if (player.getNickname() == null || player.getNickname().isBlank()) {
            throw new IllegalArgumentException("Nickname cannot be null or empty");// nie potrzebne chyba???
        }

        Player foundPlayer = playerRepository.findByNickname(player.getNickname());
        if (foundPlayer != null && !Objects.equals(foundPlayer.getId(), userId)) {
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
    public Player patchPlayer(Player player, Long userId) {
        Player target = playerRepository.findByUserId(userId);

        if (target == null) {
            throw new EntityNotFoundException("Player with user ID " + userId + " not found");
        }

        for (Field field : Player.class.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object sourceValue = field.get(player);

                if (sourceValue != null && !field.getName().equals("id") && !field.getName().equals("userId")) {
                    if (field.getName().equals("nickname")) {
                        String newNickname = (String) sourceValue;

                        if (playerRepository.findByNickname(newNickname) != null && !newNickname.equals(target.getNickname())) {
                            throw new IllegalArgumentException("Player with this nickname already exists");
                        }
                    }
                    field.set(target, sourceValue);
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            }
        }

        target.setLastUpdate(Timestamp.from(Instant.now()));
        return playerRepository.save(target);
    }


    @Override
    public void deletePlayer(Long userId) {
        if (!existsByUserId(userId)) {
            throw new EntityNotFoundException("Player with ID " + userId + " not found");
        }
        playerRepository.deleteById(userId);
    }

    @Override
    public Player getPlayerById(Long userId) {
        Player foundPlayer = playerRepository.findByUserId(userId);
        if(foundPlayer == null) {
            throw new EntityNotFoundException("Player with ID " + userId + " not found");
        }
        return foundPlayer;
    }

    @Override
    public Iterable<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public boolean existsByUserId(Long userId) {
        Player foundPlayer = playerRepository.findByUserId(userId);
        return foundPlayer != null;
    }
}
