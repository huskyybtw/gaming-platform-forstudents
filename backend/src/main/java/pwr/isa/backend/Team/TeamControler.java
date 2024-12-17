package pwr.isa.backend.Team;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/teams")
public class TeamControler {

    private final TeamService teamService;

    public TeamControler(TeamService teamService) {
        this.teamService = teamService;
    }

    // GET: Pobierz wszystkie zespoły
    @GetMapping("/")
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        List<TeamDTO> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    // GET: Pobierz zespół po ID
    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Long id) {
        TeamDTO team = teamService.getTeamById(id);
        return ResponseEntity.ok(team);
    }

    // POST: Utwórz nowy zespół
    @PostMapping("/")
    public ResponseEntity<TeamDTO> createTeam(@RequestBody Team team) {
        TeamDTO createdTeam = teamService.createTeam(team);
        return new ResponseEntity<>(createdTeam, HttpStatus.CREATED);
    }

    @PostMapping("/{teamId}/players/{userId}")
    public ResponseEntity<TeamDTO> addPlayerToTeam(
            @PathVariable Long teamId,
            @PathVariable Long userId) {
        TeamDTO team = teamService.addPlayerToTeam(teamId, userId);
        return ResponseEntity.ok(team);
    }

    @DeleteMapping("/{teamId}/players/{userId}")
    public ResponseEntity<TeamDTO> removePlayerFromTeam(
            @PathVariable Long teamId,
            @PathVariable Long userId) {
        TeamDTO team = teamService.removePlayerFromTeam(teamId, userId);
        return ResponseEntity.ok(team);
    }

    // PUT: Pełna aktualizacja zespołu
    @PutMapping("/{id}")
    public ResponseEntity<TeamDTO> updateTeam(
            @PathVariable Long id,
            @RequestBody Team updatedTeam) {
        TeamDTO team = teamService.updateTeam(id, updatedTeam);
        return ResponseEntity.ok(team); // 200
    }


    // DELETE: Usuń zespół
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}
