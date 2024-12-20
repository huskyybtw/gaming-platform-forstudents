package pwr.isa.backend.Posters.TeamPosters;

import org.springframework.web.bind.annotation.*;
import pwr.isa.backend.Security.AuthSystem.Authorize;
import pwr.isa.backend.Security.AuthSystem.AuthorizeEveryOne;

import java.util.List;

@RestController
@RequestMapping("api/v1/posters/team")
public class TeamPosterControler {

    private final TeamPosterService teamPosterService;

    public TeamPosterControler(TeamPosterService teamPosterService) {
        this.teamPosterService = teamPosterService;
    }

    @GetMapping(path= "/")
    public List<TeamPoster> readTeamPosters(
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "created_at") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDirection) {
        return teamPosterService.getAllTeamPosters(limit, offset, sortBy, sortDirection);
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
