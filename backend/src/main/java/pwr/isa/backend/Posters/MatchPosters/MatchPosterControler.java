package pwr.isa.backend.Posters.MatchPosters;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.isa.backend.Security.AuthSystem.Authorize;
import pwr.isa.backend.Security.AuthSystem.AuthorizeEveryOne;

import java.util.List;

@RestController
@RequestMapping("api/v1/posters/match")
public class MatchPosterControler {

    private final MatchPosterService matchPosterService;

    public MatchPosterControler(MatchPosterService matchPosterService) {
        this.matchPosterService = matchPosterService;
    }

    @GetMapping("/")
    public List<MatchPosterDTO> getAllMatchPosters(
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "created_at") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDirection) {
        return matchPosterService.getAllMatchPosters(limit, offset, sortBy, sortDirection);
    }

    @GetMapping("/{posterId}")
    public MatchPosterDTO getMatchPosterById(@PathVariable Long posterId) {
        return matchPosterService.getMatchPosterById(posterId);
    }

    @AuthorizeEveryOne
    @PostMapping("/")
    public ResponseEntity<MatchPosterDTO> createMatchPoster(
            @RequestBody MatchPoster matchPoster,
            @RequestParam(required = false) Long teamId) {
        return new ResponseEntity<>( matchPosterService.createMatchPoster(matchPoster, teamId), HttpStatus.CREATED);
    }

    @Authorize
    @PostMapping("/start/{posterId}")
    public ResponseEntity<MatchPosterDTO> startMatch(@PathVariable Long posterId) {
        return new ResponseEntity<>( matchPosterService.startMatch(posterId),HttpStatus.CREATED);
    }

    @Authorize
    @PostMapping("/{posterId}/join/{userId}")
    public MatchPosterDTO joinMatchPoster(
            @PathVariable Long posterId,
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "100") int team) {
        return matchPosterService.joinMatchPoster(posterId, userId, team);
    }

    // TODO - AUTORYZACJA DLA TYCH TRZECH METOD
    @PostMapping("/{posterId}/leave/{userId}")
    public MatchPosterDTO leaveMatchPoster(
            @PathVariable Long posterId,
            @PathVariable Long userId) {
        return matchPosterService.leaveMatchPoster(posterId, userId);
    }

    @PostMapping("/{posterId}/joinTeam/{teamId}")
    public MatchPosterDTO joinTeam(
            @PathVariable Long posterId,
            @PathVariable Long teamId) {
        return matchPosterService.joinAsTeam(posterId, teamId);
    }

    @Authorize
    @PutMapping("/{posterId}")
    public MatchPosterDTO updateMatchPoster(
            @PathVariable Long posterId,
            @RequestBody MatchPoster matchPoster) {
        return matchPosterService.updateMatchPoster(posterId, matchPoster);
    }

    @Authorize
    @DeleteMapping("/{posterId}")
    public ResponseEntity<Void> deleteMatchPoster(@PathVariable Long posterId) {
        matchPosterService.deleteMatchPoster(posterId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
