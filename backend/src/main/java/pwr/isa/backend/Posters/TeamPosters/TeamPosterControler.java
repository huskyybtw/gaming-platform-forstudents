package pwr.isa.backend.Posters.TeamPosters;

import org.springframework.web.bind.annotation.*;
import pwr.isa.backend.Security.AuthSystem.Authorize;
import pwr.isa.backend.Security.AuthSystem.AuthorizeEveryOne;

@RestController
@RequestMapping("api/v1/posters/team")
public class TeamPosterControler {

    private final TeamPosterService teamPosterService;

    public TeamPosterControler(TeamPosterService teamPosterService) {
        this.teamPosterService = teamPosterService;
    }

    @GetMapping(path= "/")
    public Iterable<TeamPoster> readTeamPosters(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(required = false) boolean sortByRating) {
        return teamPosterService.getAllTeamPosters(limit, offset, sortByRating);
    }

    @GetMapping(path= "/{teamId}")
    public TeamPoster readTeamPoster(@PathVariable Long teamId) {
        return teamPosterService.getTeamPosterById(teamId);
    }

    // TODO AUTORYZACJA MUSI BYC TEAM CAPITAN A NIE MAM JAK WZIAC ID NA TEN MOMENT
    @PostMapping(path= "/")
    public TeamPoster createTeamPoster(@RequestBody TeamPoster teamPoster) {
        return teamPosterService.createTeamPoster(teamPoster);
    }

    @Authorize
    @PatchMapping(path= "/{teamId}")
    public TeamPoster updateTeamPoster(
            @PathVariable Long teamId,
            @RequestBody TeamPoster teamPoster) {
        return teamPosterService.updateTeamPoster(teamPoster, teamId);
    }

    @Authorize
    @DeleteMapping(path= "/{teamId}")
    public void deleteTeamPoster(@PathVariable Long teamId) {
        teamPosterService.deleteTeamPoster(teamId);
    }


}
