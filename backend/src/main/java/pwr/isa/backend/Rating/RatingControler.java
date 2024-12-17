package pwr.isa.backend.Rating;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/rating")
public class RatingControler {

    private final RatingService ratingService;

    public RatingControler(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    // Player rating endpoints
    @PostMapping("/player/{userId}")
    public ResponseEntity<PlayerRating> createOrUpdatePlayerRating(
            @PathVariable Long userId,
            @RequestParam Integer rating) {
        PlayerRating updatedRating = ratingService.createOrUpdatePlayerRating(userId, rating);
        return ResponseEntity.ok(updatedRating);
    }

    @GetMapping("/player/{userId}")
    public ResponseEntity<PlayerRating> getPlayerRating(@PathVariable Long userId) {
        PlayerRating playerRating = ratingService.getPlayerRating(userId);
        return ResponseEntity.ok(playerRating);
    }

    @DeleteMapping("/player/{userId}")
    public ResponseEntity<Void> deletePlayerRating(@PathVariable Long userId) {
        ratingService.deletePlayerRating(userId);
        return ResponseEntity.noContent().build();
    }

    // Team rating endpoints
    @PostMapping("/team/{teamId}")
    public ResponseEntity<TeamRating> createOrUpdateTeamRating(
            @PathVariable Long teamId,
            @RequestParam Integer rating) {
        TeamRating updatedRating = ratingService.createOrUpdateTeamRating(teamId, rating);
        return ResponseEntity.ok(updatedRating);
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<TeamRating> getTeamRating(@PathVariable Long teamId) {
        TeamRating teamRating = ratingService.getTeamRating(teamId);
        return ResponseEntity.ok(teamRating);
    }

    @DeleteMapping("/team/{teamId}")
    public ResponseEntity<Void> deleteTeamRating(@PathVariable Long teamId) {
        ratingService.deleteTeamRating(teamId);
        return ResponseEntity.noContent().build();
    }
}
