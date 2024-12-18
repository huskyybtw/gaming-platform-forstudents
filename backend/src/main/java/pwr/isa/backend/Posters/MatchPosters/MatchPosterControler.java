package pwr.isa.backend.Posters.MatchPosters;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/posters/match")
public class MatchPosterControler {

    private final MatchPosterService matchPosterService;

    public MatchPosterControler(MatchPosterService matchPosterService) {
        this.matchPosterService = matchPosterService;
    }

    // Get all match posters with pagination
    @GetMapping("/")
    public List<MatchPosterDTO> getAllMatchPosters(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        return matchPosterService.getAllMatchPosters(limit, offset);
    }

    // Get a single match poster by ID
    @GetMapping("/{posterId}")
    public MatchPosterDTO getMatchPosterById(@PathVariable Long posterId) {
        return matchPosterService.getMatchPosterById(posterId);
    }

    // Create a new match poster
    @PostMapping("/")
    public MatchPosterDTO createMatchPoster(
            @RequestBody MatchPoster matchPoster,
            @RequestParam(required = false) Long teamId) {
        return matchPosterService.createMatchPoster(matchPoster, teamId);
    }

    // Start a match for the given poster ID
    @PostMapping("/start/{posterId}")
    public MatchPosterDTO startMatch(@PathVariable Long posterId) {
        return matchPosterService.startMatch(posterId);
    }

    // Join a match poster as a user
    @PostMapping("/{posterId}/join/{userId}")
    public MatchPosterDTO joinMatchPoster(
            @PathVariable Long posterId,
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "100") int team) {
        return matchPosterService.joinMatchPoster(posterId, userId, team);
    }

    // Leave a match poster
    @PostMapping("/{posterId}/leave/{userId}")
    public MatchPosterDTO leaveMatchPoster(
            @PathVariable Long posterId,
            @PathVariable Long userId) {
        return matchPosterService.leaveMatchPoster(posterId, userId);
    }

    // Join a match poster as a team
    @PostMapping("/{posterId}/joinTeam/{teamId}")
    public MatchPosterDTO joinTeam(
            @PathVariable Long posterId,
            @PathVariable Long teamId) {
        return matchPosterService.joinAsTeam(posterId, teamId);
    }

    @PutMapping("/{posterId}")
    public MatchPosterDTO updateMatchPoster(
            @PathVariable Long posterId,
            @RequestBody MatchPoster matchPoster) {
        return matchPosterService.updateMatchPoster(posterId, matchPoster);
    }

    // Delete a match poster by ID
    @DeleteMapping("/{posterId}")
    public void deleteMatchPoster(@PathVariable Long posterId) {
        matchPosterService.deleteMatchPoster(posterId);
    }
}
