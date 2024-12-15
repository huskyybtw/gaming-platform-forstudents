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
    public ResponseEntity<List<Team>> getAllTeams() {
        List<Team> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams); // 200
    }

    // GET: Pobierz zespół po ID
    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
        Team team = teamService.getTeamById(id);
        return ResponseEntity.ok(team); // 200
    }

    // POST: Utwórz nowy zespół
    @PostMapping("/")
    public ResponseEntity<Team> createTeam(@RequestBody Team team) {
        Team createdTeam = teamService.createTeam(team);
        return new ResponseEntity<>(createdTeam, HttpStatus.CREATED); // 201
    }

    // PUT: Pełna aktualizacja zespołu
    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeam(
            @PathVariable Long id,
            @RequestBody Team updatedTeam) {
        Team team = teamService.updateTeam(id, updatedTeam);
        return ResponseEntity.ok(team); // 200
    }

    // PATCH: Częściowa aktualizacja zespołu
    @PatchMapping("/{id}")
    public ResponseEntity<Team> patchTeam(
            @PathVariable Long id,
            @RequestBody Team updatedTeam) {
        Team team = teamService.patchTeam(id, updatedTeam);
        return ResponseEntity.ok(team); // 200
    }

    // DELETE: Usuń zespół
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
