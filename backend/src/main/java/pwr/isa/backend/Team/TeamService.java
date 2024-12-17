package pwr.isa.backend.Team;

import java.util.List;

public interface TeamService {
    Team createTeam(Team team);
    Team updateTeam(Long id, Team updatedTeam);

    Team addPlayerToTeam(Long teamId, Long userId);
    Team removePlayerFromTeam(Long teamId, Long userId);

    void deleteTeam(Long id);
    TeamDTO getTeamById(Long id);
    List<TeamDTO> getAllTeams();
}

