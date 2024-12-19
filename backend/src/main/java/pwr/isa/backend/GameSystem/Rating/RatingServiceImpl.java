package pwr.isa.backend.GameSystem.Rating;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pwr.isa.backend.Player.Player;
import pwr.isa.backend.Player.PlayerRepository;
import pwr.isa.backend.Team.Team;
import pwr.isa.backend.Team.TeamRepository;

import java.util.List;

@Service
@Transactional
public class RatingServiceImpl implements RatingService {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    private static final int DEFAULT_LIMIT = 100;
    private static final int DEFAULT_OFFSET = 0;

    public RatingServiceImpl(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    // PLAYER RATING METHODS
    @Override
    public Player updatePlayerRating(Long userId, Integer difference) {

        Player player = validatePlayer(userId);
        player.setRating(player.getRating() + difference);

        return playerRepository.save(player);
    }

    @Override
    public Integer getPlayerRating(Long userId) {
        Player player = validatePlayer(userId);

        return player.getRating();
    }

    @Override
    public List<Player> getBestPlayers(int limit, int offset) {
        if(limit < 0 || offset < 0) {
            limit = DEFAULT_LIMIT;
            offset = DEFAULT_OFFSET;
        }
        return playerRepository.findBestPlayers(limit, offset);
    }

    // TEAM RATING METHODS

    // TODO AKTUALNIE TO NIEZBYT DZIALA TAKZE DO PRZEMYSLENIA
    @Override
    public Team updateTeamRating(Long teamId, Integer difference) {
        Team team = validateTeam(teamId);
        team.setRating(team.getRating() + difference);
        return teamRepository.save(team);
    }

    @Override
    public Integer getTeamRating(Long teamId) {
        return validateTeam(teamId).getRating();
    }

    @Override
    public List<Team> getBestTeams(int limit, int offset) {
        if (limit < 0 || offset < 0) {
            limit = DEFAULT_LIMIT;
            offset = DEFAULT_OFFSET;
        }

        return teamRepository.findBestTeams(limit, offset);
    }


    // Validate methods
    private Player validatePlayer(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }

        Player foundplayer = playerRepository.findByUserId(userId);

        if (foundplayer == null) {
            throw new EntityNotFoundException("Player not found with id: " + userId);
        }

        return foundplayer;
    }

    private Team validateTeam(Long teamId) {
        if (teamId == null) {
            throw new IllegalArgumentException("TeamId cannot be null");
        }
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + teamId));
    }
}
