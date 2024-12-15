package pwr.isa.backend.Team;

import java.util.List;

public interface TeamService {
    Team createTeam(Team team);
    Team updateTeam(Long id, Team updatedTeam);
    Team patchTeam(Long id, Team updatedTeam); // Dodana metoda dla PATCH
    void deleteTeam(Long id);
    Team getTeamById(Long id);
    List<Team> getAllTeams();
}

