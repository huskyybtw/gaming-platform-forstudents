package pwr.isa.backend.Rating;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.isa.backend.Player.Player;
import pwr.isa.backend.Team.Team;

import java.util.List;

@RestController
@RequestMapping("api/v1/rating")
public class RatingControler {

    private final RatingService ratingService;

    public RatingControler(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    // Player rating endpoints
    @PatchMapping("/player/{userId}")
    public ResponseEntity<Player> updatePlayerRating(
            @PathVariable Long userId,
            @RequestParam Integer difference) {
        Player updatedPlayer = ratingService.updatePlayerRating(userId, difference);
        return ResponseEntity.ok(updatedPlayer);
    }

    @GetMapping("/player/{userId}")
    public ResponseEntity<Integer> getPlayerRating(@PathVariable Long userId) {
        Integer playerRating = ratingService.getPlayerRating(userId);
        return ResponseEntity.ok(playerRating);
    }

    @GetMapping("/player/best")
    public ResponseEntity<List<Player>> getBestPlayers(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        List<Player> bestPlayers = ratingService.getBestPlayers(limit, offset);
        return ResponseEntity.ok(bestPlayers);
    }

    // Team rating endpoints
    @PatchMapping("/team/{teamId}")
    public ResponseEntity<Team> updateTeamRating(
            @PathVariable Long teamId,
            @RequestParam Integer difference) {
        Team updatedTeam = ratingService.updateTeamRating(teamId, difference);
        return ResponseEntity.ok(updatedTeam);
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<Integer> getTeamRating(@PathVariable Long teamId) {
        Integer teamRating = ratingService.getTeamRating(teamId);
        return ResponseEntity.ok(teamRating);
    }

    @GetMapping("/team/best")
    public ResponseEntity<List<Team>> getBestTeams(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        List<Team> bestTeams = ratingService.getBestTeams(limit, offset);
        return ResponseEntity.ok(bestTeams);
    }
}
