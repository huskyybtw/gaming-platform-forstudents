package pwr.isa.backend.Rating;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pwr.isa.backend.Player.Player;
import pwr.isa.backend.Player.PlayerRepository;
import pwr.isa.backend.Team.Team;
import pwr.isa.backend.Team.TeamRepository;

@Service
@Transactional
public class RatingServiceImpl implements RatingService {

    private final PlayerRatingRepository playerRatingRepository;
    private final TeamRatingRepository teamRatingRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public RatingServiceImpl(PlayerRatingRepository playerRatingRepository,
                             TeamRatingRepository teamRatingRepository,
                             PlayerRepository playerRepository,
                             TeamRepository teamRepository) {
        this.playerRatingRepository = playerRatingRepository;
        this.teamRatingRepository = teamRatingRepository;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    // PLAYER RATING METHODS
    @Override
    public PlayerRating createOrUpdatePlayerRating(Long playerId, Integer rating) {
        validatePlayerId(playerId);
        validateRating(rating);

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + playerId));

        PlayerRating playerRating = playerRatingRepository.findByPlayerId(playerId)
                .orElse(PlayerRating.builder().player(player).build());

        playerRating.setRating(rating);
        return playerRatingRepository.save(playerRating);
    }

    @Override
    public PlayerRating getPlayerRating(Long playerId) {
        validatePlayerId(playerId);

        return playerRatingRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Rating not found for player with id: " + playerId));
    }

    @Override
    public void deletePlayerRating(Long playerId) {
        validatePlayerId(playerId);

        PlayerRating rating = playerRatingRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Rating not found for player with id: " + playerId));
        playerRatingRepository.delete(rating);
    }

    // TEAM RATING METHODS
    @Override
    public TeamRating createOrUpdateTeamRating(Long teamId, Integer rating) {
        validateTeamId(teamId);
        validateRating(rating);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + teamId));

        TeamRating teamRating = teamRatingRepository.findByTeamId(teamId)
                .orElse(TeamRating.builder().team(team).build());

        teamRating.setRating(rating);
        return teamRatingRepository.save(teamRating);
    }

    @Override
    public TeamRating getTeamRating(Long teamId) {
        validateTeamId(teamId);

        return teamRatingRepository.findByTeamId(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Rating not found for team with id: " + teamId));
    }

    @Override
    public void deleteTeamRating(Long teamId) {
        validateTeamId(teamId);

        TeamRating rating = teamRatingRepository.findByTeamId(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Rating not found for team with id: " + teamId));
        teamRatingRepository.delete(rating);
    }

    // VALIDATION METHODS
    private void validateRating(Integer rating) {
        if (rating == null) {
            throw new IllegalArgumentException("Rating cannot be null");
        }
        if (rating < 1 || rating > 10) {
            throw new IllegalArgumentException("Rating must be between 1 and 10");
        }
    }

    private void validatePlayerId(Long playerId) {
        if (playerId == null || playerId <= 0) {
            throw new IllegalArgumentException("Player ID must be greater than zero");
        }
    }

    private void validateTeamId(Long teamId) {
        if (teamId == null || teamId <= 0) {
            throw new IllegalArgumentException("Team ID must be greater than zero");
        }
    }
}
