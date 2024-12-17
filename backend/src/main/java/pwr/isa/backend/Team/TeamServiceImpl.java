package pwr.isa.backend.Team;

import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.springframework.stereotype.Service;
import pwr.isa.backend.User.User;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }
//poprawka teamcapitan


    @Override
    public Team createTeam(Team team) {
        if (teamRepository.findByTeamName(team.getTeamName()).isPresent()) {
            throw new IllegalArgumentException("Team with this name already exists");
        }
        if (team.getTeamCapitan() == null) {
            throw new IllegalArgumentException("Team must have a captain");
        }

        return teamRepository.save(team);
    }


//    @Override
//    public Team createTeam(Team team) {
//        if (teamRepository.findByTeamName(team.getTeamName()).isPresent()) {
//            throw new IllegalArgumentException("Team name already exists.");
//        }
//        return teamRepository.save(team);
//    }

    @Override
    public Team getTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Team not found."));
    }

    @Override
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @Override
    public Team updateTeam(Long id, Team updatedTeam) {
        Team team = getTeamById(id);
        team.setTeamName(updatedTeam.getTeamName());
        team.setUserIds(updatedTeam.getUserIds());
        team.setTeamCapitan(updatedTeam.getTeamCapitan());
        team.setDescription(updatedTeam.getDescription());
        return teamRepository.save(team);
    }

    @Override
    public void deleteTeam(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new IllegalArgumentException("Team not found.");
        }
        teamRepository.deleteById(id);
    }
}