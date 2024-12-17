package pwr.isa.backend.Team;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pwr.isa.backend.Team.TeamUsers.TeamUsersRepository;
import pwr.isa.backend.User.UserService;

import java.util.ArrayList;
import java.util.List;

/*
    TODO Obiekt team nie ma w sobie pola z użytkownikami
    Można zmienic by zawsze returnowac TeamDTO
    Można tez tak zostawic do rozmyślenia
 */

/*
    TODO Pouwzgledniać transakcje
*/
@Service
@Transactional
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamUsersRepository teamUsersRepository;
    private final UserService userService;

    public TeamServiceImpl(TeamRepository teamRepository,
                           TeamUsersRepository teamUsersRepository,
                           UserService userService) {
        this.teamRepository = teamRepository;
        this.userService = userService;
        this.teamUsersRepository = teamUsersRepository;
    }


    @Transactional
    @Override
    public Team createTeam(Team team) {
        validateTeamName(team.getTeamName(), null);
        validateTeamCaptain(team.getTeamCaptain());

        Team createdTeam = teamRepository.save(team);
        teamUsersRepository.addTeamUser(createdTeam.getId(), createdTeam.getTeamCaptain());
        return createdTeam;
    }

    @Override
    public Team updateTeam(Long id, Team updatedTeam) {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + id));

        validateTeamName(updatedTeam.getTeamName(), null);
        validateTeamCaptain(updatedTeam.getTeamCaptain());

        return teamRepository.save(updatedTeam);
    }

    /*
        TODO team ma max 5 osob walidacja
        do przemyslenia co robimy jesli chcemy na raz dodac wiecej niz jedna osobe
        analogicznie z usuwaniem
    */
    @Override
    public Team addPlayerToTeam(Long teamId, Long userId) {

        return null;
    }

    @Override
    public Team removePlayerFromTeam(Long teamId, Long userId) {

        return null;
    }

    @Override
    public void deleteTeam(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new EntityNotFoundException("Team not found with id: " + id);
        }
        teamRepository.deleteById(id);
    }

    @Override
    public TeamDTO getTeamById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + id));
        List<Long> users = teamUsersRepository.findUsersByTeamId(id);

        return TeamDTO.builder()
                .team(team)
                .users(users)
                .build();
    }

    @Override
    public List<TeamDTO> getAllTeams() {
        Iterable<Team> teams = teamRepository.findAll();
        List<TeamDTO> teamDTOS = new ArrayList<>();

        for (Team team : teams) {
            List<Long> users = teamUsersRepository.findUsersByTeamId(team.getId());
            teamDTOS.add(TeamDTO.builder()
                    .team(team)
                    .users(users)
                    .build());
        }

        return teamDTOS;
    }

    void validateTeamName(String teamName, Long id) {
        if (teamName == null || teamName.isBlank()) {
            throw new IllegalArgumentException("Team name cannot be null or empty");
        }

        boolean exists = teamRepository.findByTeamName(teamName).isPresent();
        if (exists) {
            throw new IllegalArgumentException("A team with this name already exists");
        }

        if (id != null) {
            Team team = teamRepository.findByTeamName(teamName)
                    .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + id));
            if (!team.getId().equals(id)) {
                throw new IllegalArgumentException("A team with this name already exists");
            }
        }
    }

    void validateTeamCaptain(Long teamCaptain) {
        if (teamCaptain == null) {
            throw new IllegalArgumentException("Team captain cannot be null");
        }

        if (!userService.exists(teamCaptain)) {
            throw new IllegalArgumentException("User with id: " + teamCaptain + " does not exist");
        }
    }
}
