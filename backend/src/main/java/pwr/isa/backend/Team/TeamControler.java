package pwr.isa.backend.Team;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.isa.backend.Security.AuthSystem.Authorize;
import pwr.isa.backend.Security.AuthSystem.AuthorizeEveryOne;

import java.util.List;

@RestController
@RequestMapping("api/v1/teams")
public class TeamControler {

    private final TeamService teamService;

    public TeamControler(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/")
    public ResponseEntity<List<TeamDTO>> getAllTeams(
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "teamName") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDirection
    ) {
        return new ResponseEntity<>(teamService.getAllTeams(limit, offset, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Long id) {
        return new ResponseEntity<>(teamService.getTeamById(id), HttpStatus.OK);
    }

    @AuthorizeEveryOne
    @PostMapping("/")
    public ResponseEntity<TeamDTO> createTeam(@RequestBody Team team) {
        TeamDTO createdTeam = teamService.createTeam(team);
        return new ResponseEntity<>(createdTeam, HttpStatus.CREATED);
    }

    @Authorize
    @PostMapping("/manage/{userId}/{teamId}")
    public ResponseEntity<TeamDTO> addPlayerToTeam(
            @PathVariable Long teamId,
            @PathVariable Long userId) {
        TeamDTO team = teamService.addPlayerToTeam(teamId, userId);
        return new ResponseEntity<>(team, HttpStatus.OK);
    }

    // TODO Do zastanowienia jak zrobic autoryzajce bo jak narazie tylko capitan moze usuwac
    @Authorize
    @DeleteMapping("/manage/{userId}/{teamId}")
    public ResponseEntity<TeamDTO> removePlayerFromTeam(
            @PathVariable Long teamId,
            @PathVariable Long userId) {
        TeamDTO team = teamService.removePlayerFromTeam(teamId, userId);
        return new ResponseEntity<>(team, HttpStatus.NO_CONTENT);
    }

    @Authorize
    @PutMapping("/{id}")
    public ResponseEntity<TeamDTO> updateTeam(
            @PathVariable Long id,
            @RequestBody Team updatedTeam) {
        TeamDTO team = teamService.updateTeam(id, updatedTeam);
        return new ResponseEntity<>(team,HttpStatus.OK); // 200
    }


    @Authorize
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
