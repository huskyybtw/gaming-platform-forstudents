package pwr.isa.backend.Rating;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ratings")
public class RatingControler {

    private final RatingService ratingService;

    public RatingControler(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    // Endpoints dla TeamRating
    @PostMapping("/teams")
    public ResponseEntity<TeamRating> createTeamRating(@RequestBody TeamRating teamRating) {
        return new ResponseEntity<>(ratingService.createTeamRating(teamRating), HttpStatus.CREATED);
    }

    @GetMapping("/teams/{id}")
    public ResponseEntity<TeamRating> getTeamRating(@PathVariable Long id) {
        return ResponseEntity.ok(ratingService.getTeamRatingById(id));
    }

    @GetMapping("/teams")
    public ResponseEntity<List<TeamRating>> getAllTeamRatings() {
        return ResponseEntity.ok(ratingService.getAllTeamRatings());
    }

    @PutMapping("/teams/{id}")
    public ResponseEntity<TeamRating> updateTeamRating(@PathVariable Long id, @RequestBody TeamRating updatedRating) {
        return ResponseEntity.ok(ratingService.updateTeamRating(id, updatedRating));
    }
    @PatchMapping("/players/{id}")
    public ResponseEntity<PlayerRating> patchPlayerRating(@PathVariable Long id, @RequestBody PlayerRating partialUpdate) {
        PlayerRating updatedRating = ratingService.patchPlayerRating(id, partialUpdate);
        return ResponseEntity.ok(updatedRating);
    }

    @DeleteMapping("/teams/{id}")
    public ResponseEntity<Void> deleteTeamRating(@PathVariable Long id) {
        ratingService.deleteTeamRating(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoints dla PlayerRating
    @PostMapping("/players")
    public ResponseEntity<PlayerRating> createPlayerRating(@RequestBody PlayerRating playerRating) {
        return new ResponseEntity<>(ratingService.createPlayerRating(playerRating), HttpStatus.CREATED);
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<PlayerRating> getPlayerRating(@PathVariable Long id) {
        return ResponseEntity.ok(ratingService.getPlayerRatingById(id));
    }

    @GetMapping("/players")
    public ResponseEntity<List<PlayerRating>> getAllPlayerRatings() {
        return ResponseEntity.ok(ratingService.getAllPlayerRatings());
    }

    @PutMapping("/players/{id}")
    public ResponseEntity<PlayerRating> updatePlayerRating(@PathVariable Long id, @RequestBody PlayerRating updatedRating) {
        return ResponseEntity.ok(ratingService.updatePlayerRating(id, updatedRating));
    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity<Void> deletePlayerRating(@PathVariable Long id) {
        ratingService.deletePlayerRating(id);
        return ResponseEntity.noContent().build();
    }
}
