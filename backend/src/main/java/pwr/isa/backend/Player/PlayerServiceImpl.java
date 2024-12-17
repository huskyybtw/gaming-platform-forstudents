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

        validateNickname(player.getNickname(), null);

        updateLastUpdateTimestamp(player);

        return playerRepository.save(player);
    }

    @Override
    public Player updatePlayer(Player player, Long userId) {
        Player existingPlayer = playerRepository.findByUserId(userId);

        if (existingPlayer == null) {
            throw new EntityNotFoundException("Player with user ID " + userId + " not found");
        }

        if (!Objects.equals(existingPlayer.getUserId(), player.getUserId())) {
            throw new IllegalArgumentException("User cannot be changed");
        }

        validateNickname(player.getNickname(), userId);

        existingPlayer.setNickname(player.getNickname());
        existingPlayer.setOpgg(player.getOpgg());
        existingPlayer.setDescription(player.getDescription());

        updateLastUpdateTimestamp(existingPlayer);

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

                        validateNickname(newNickname, target.getId());
                    }
                    field.set(target, sourceValue);
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            }
        }

        updateLastUpdateTimestamp(target);
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

    private void validateNickname(String nickname, Long excludedPlayerId) {
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("Nickname cannot be null or empty");
        }

        Player foundPlayer = playerRepository.findByNickname(nickname);
        if (foundPlayer != null && !Objects.equals(foundPlayer.getId(), excludedPlayerId)) {
            throw new IllegalArgumentException("Player with this nickname already exists");
        }
    }

    private void updateLastUpdateTimestamp(Player player) {
        player.setLastUpdate(Timestamp.from(Instant.now()));
    }
}

