package pwr.isa.backend.Team;

import java.util.List;

public interface TeamService {

    Team createTeam(Team team);

    Team getTeamById(Long id);

    List<Team> getAllTeams();

    Team updateTeam(Long id, Team updatedTeam);

    void deleteTeam(Long id);
}
