package pwr.isa.backend.Rating;

public interface RatingService {

    // PLAYER RATING
    PlayerRating createOrUpdatePlayerRating(Long userId, Integer rating);
    PlayerRating getPlayerRating(Long userId);
    void deletePlayerRating(Long userId);

    // TEAM RATING
    TeamRating createOrUpdateTeamRating(Long teamId, Integer rating);
    TeamRating getTeamRating(Long teamId);
    void deleteTeamRating(Long teamId);
}
