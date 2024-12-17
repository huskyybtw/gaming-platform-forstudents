package pwr.isa.backend.Team;

import java.util.List;

public interface TeamService {
    TeamDTO createTeam(Team team);
    TeamDTO updateTeam(Long id, Team updatedTeam);

    TeamDTO addPlayerToTeam(Long teamId, Long userId);
    TeamDTO removePlayerFromTeam(Long teamId, Long userId);

    void deleteTeam(Long id);
    TeamDTO getTeamById(Long id);
    List<TeamDTO> getAllTeams();
}
