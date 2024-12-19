package pwr.isa.backend.Player;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pwr.isa.backend.Consumer.DTO.LeagueDTO;
import pwr.isa.backend.Consumer.DTO.PlayerDetailsDTO;
import pwr.isa.backend.Consumer.RiotService;
import pwr.isa.backend.User.UserService;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/*
    TODO PRZETESTOWAC CZY NAPEWNO DZIALAJA WSZYSTKIE FUNKCJE
*/
@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final UserService userService;
    private final RiotService riotService;

    public PlayerServiceImpl(PlayerRepository playerRepository,
                             UserService userRepository,
                             RiotService riotService) {
        this.playerRepository = playerRepository;
        this.userService = userRepository;
        this.riotService = riotService;
    }

    @Transactional
    @Override
    public Player createPlayer(Player player) {
        boolean userExists = userService.exists(player.getUserId());
        Player playerWithSameUser = playerRepository.findByUserId(player.getUserId());
        if (player.getUserId() == null || !userExists || playerWithSameUser != null) {
            throw new IllegalArgumentException("User id is not valid or already assigned to another player");
        }

        validateNickname(player.getNickname(), null);
        validateTagLine(player.getTagLine(), null);

        updateLastUpdateTimestamp(player);
        playerRepository.save(player);
        return refreshPlayer(player.getUserId());
    }

    @Override
    public Player refreshPlayer(Long userId) {
        Player player = playerRepository.findByUserId(userId);
        PlayerDetailsDTO playerDetailsDTO =  riotService.getPlayerDTO(player.getNickname(), player.getTagLine());

        player.setPuuid(playerDetailsDTO.getPuuid());
        player.setSummonerid(playerDetailsDTO.getSummonerid());
        player.setAccountId(playerDetailsDTO.getAccountId());
        player.setProfileIconId(playerDetailsDTO.getProfileIconId());
        player.setSummonerLevel(playerDetailsDTO.getSummonerLevel());

        updateLastUpdateTimestamp(player);
        return playerRepository.save(player);
    }


    @Transactional
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
        validateTagLine(player.getTagLine(), userId);

        existingPlayer.setNickname(player.getNickname());
        existingPlayer.setTagLine(player.getTagLine());
        existingPlayer.setOpgg(player.getOpgg());
        existingPlayer.setDescription(player.getDescription());
        updateLastUpdateTimestamp(existingPlayer);
        playerRepository.save(existingPlayer);

        return refreshPlayer(existingPlayer.getUserId());
    }

    @Transactional
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

                        validateNickname(newNickname, target.getUserId());
                    }

                    if (field.getName().equals("tagLine")) {
                        String newTagLine = (String) sourceValue;

                        validateTagLine(newTagLine, target.getUserId());
                    }
                    field.set(target, sourceValue);
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            }
        }
        updateLastUpdateTimestamp(target);
        playerRepository.save(target);
        return refreshPlayer(target.getUserId());
    }

    @Transactional
    @Override
    public List<LeagueDTO> getPlayerRank(Long userId) {
        return riotService.getLeagueDTO(playerRepository.findByUserId(userId).getSummonerid());
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
    public Iterable<Player> getAllPlayers(int limit, int offset, boolean sortByRating) {
        if (sortByRating) {
            return playerRepository.findBestPlayers(limit, offset);
        }
        return playerRepository.findAll();
    }

    @Override
    public boolean existsByUserId(Long userId) {
        Player foundPlayer = playerRepository.findByUserId(userId);
        return foundPlayer != null;
    }

    private void validateTagLine(String tagLine, Long excludedPlayerId) {
        if (tagLine == null || tagLine.isBlank()) {
            throw new IllegalArgumentException("Tag line cannot be null or empty");
        }

        if (tagLine.length() < 3 || tagLine.length() > 5) {
            throw new IllegalArgumentException("Tag line must be between 3 and 5 characters long");
        }

        if (!tagLine.matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("Tag line must contain only alphanumeric characters");
        }

        Player foundPlayer = playerRepository.findByTagLine(tagLine);
        if (foundPlayer != null && !Objects.equals(foundPlayer.getUserId(), excludedPlayerId)) {
            throw new IllegalArgumentException("Player with this nickname already exists");
        }
    }

    private void validateNickname(String nickname, Long excludedPlayerId) {
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("Nickname cannot be null or empty");
        }

        Player foundPlayer = playerRepository.findByNickname(nickname);
        if (foundPlayer != null && !Objects.equals(foundPlayer.getUserId(), excludedPlayerId)) {
            throw new IllegalArgumentException("Player with this nickname already exists");
        }
    }

    private void updateLastUpdateTimestamp(Player player) {
        player.setLastUpdate(Timestamp.from(Instant.now()));
    }
}

