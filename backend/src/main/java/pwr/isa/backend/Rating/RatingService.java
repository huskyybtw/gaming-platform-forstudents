package pwr.isa.backend.Rating;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    private final TeamRatingRepository teamRatingRepository;
    private final PlayerRatingRepository playerRatingRepository;

    public RatingService(TeamRatingRepository teamRatingRepository, PlayerRatingRepository playerRatingRepository) {
        this.teamRatingRepository = teamRatingRepository;
        this.playerRatingRepository = playerRatingRepository;
    }

    // CRUD dla TeamRating
    public TeamRating createTeamRating(TeamRating teamRating) {
        return teamRatingRepository.save(teamRating);
    }

    public TeamRating getTeamRatingById(Long id) {
        return teamRatingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Team rating not found."));
    }

    public List<TeamRating> getAllTeamRatings() {
        return teamRatingRepository.findAll();
    }

    public TeamRating updateTeamRating(Long id, TeamRating updatedRating) {
        TeamRating existingRating = getTeamRatingById(id);
        existingRating.setRating(updatedRating.getRating());
        return teamRatingRepository.save(existingRating);
    }

    public void deleteTeamRating(Long id) {
        teamRatingRepository.deleteById(id);
    }

    // CRUD dla PlayerRating
    public PlayerRating createPlayerRating(PlayerRating playerRating) {
        return playerRatingRepository.save(playerRating);
    }

    public PlayerRating getPlayerRatingById(Long id) {
        return playerRatingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Player rating not found."));
    }

    public List<PlayerRating> getAllPlayerRatings() {
        return playerRatingRepository.findAll();
    }

    public PlayerRating updatePlayerRating(Long id, PlayerRating updatedRating) {
        PlayerRating existingRating = getPlayerRatingById(id);
        existingRating.setRating(updatedRating.getRating());
        return playerRatingRepository.save(existingRating);
    }

    public void deletePlayerRating(Long id) {
        playerRatingRepository.deleteById(id);
    }
}
