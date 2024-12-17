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

    @Override
    public TeamDTO createTeam(Team team) {
        validateTeamName(team.getTeamName(), null);
        validateTeamCaptain(team.getTeamCaptain());
// na poczatek team nie ma uzytkownikow wiec list[-]
        Team savedTeam = teamRepository.save(team);
        return buildTeamDTO(savedTeam, new ArrayList<>());
    }

    @Override
    public TeamDTO updateTeam(Long id, Team updatedTeam) {
        // Pobieramy istniejący zespół lub wyjątek
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + id));

        // Walidacaj
        validateTeamName(updatedTeam.getTeamName(), id);
        validateTeamCaptain(updatedTeam.getTeamCaptain());

        // Aktualizujemy dane
        existingTeam.setTeamName(updatedTeam.getTeamName());
        existingTeam.setDescription(updatedTeam.getDescription());
        existingTeam.setTeamCaptain(updatedTeam.getTeamCaptain());

        // Zapisujemy zaktualizowany zespół
        Team savedTeam = teamRepository.save(existingTeam);

        // Pobieramy aktualną listę użytkowników z zespołu
        List<Long> users = teamUsersRepository.findUsersByTeamId(id);

        // Budujemy i zwracamy TeamDTO
        return buildTeamDTO(savedTeam, users);
    }


    /*
        Implementacja TODO:
        - Walidacja max 5 osób w zespole.
        - Założenie: Dodajemy jednego użytkownika na raz. (narazie)
        - Sprawdzamy czy użytkownik istnieje i czy nie jest już w zespole.
        - Po dodaniu zwracamy TeamDTO.

    */
    @Override
    public TeamDTO addPlayerToTeam(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + teamId));

        if (!userService.exists(userId)) {
            throw new IllegalArgumentException("User with id: " + userId + " does not exist");
        }

        // Sprawdzamy ilu użytkowników jest w zespole
        List<Long> currentUsers = teamUsersRepository.findUsersByTeamId(teamId);
        if (currentUsers.contains(userId)) {
            throw new IllegalStateException("User with id: " + userId + " is already in the team");
        }

        if (currentUsers.size() >= 5) {
            throw new IllegalStateException("Team cannot have more than 5 players");
        }

        // Dodajemy użytkownika do zespołu
        teamUsersRepository.addTeamUser(teamId, userId);

        // Odświeżamy listę
        currentUsers = teamUsersRepository.findUsersByTeamId(teamId);

        return buildTeamDTO(team, currentUsers);
    }

    @Override
    public TeamDTO removePlayerFromTeam(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + teamId));

        List<Long> currentUsers = teamUsersRepository.findUsersByTeamId(teamId);
        if (!currentUsers.contains(userId)) {
            throw new IllegalStateException("User with id: " + userId + " is not in the team");
        }

        // Usuwamy użytkownika z zespołu
        teamUsersRepository.deleteTeamUser(teamId, userId);

        // Odświeżamy listę użytkowników
        currentUsers = teamUsersRepository.findUsersByTeamId(teamId);

        return buildTeamDTO(team, currentUsers);
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
        return buildTeamDTO(team, users);
    }

    @Override
    public List<TeamDTO> getAllTeams() {
        Iterable<Team> teams = teamRepository.findAll();
        List<TeamDTO> teamDTOS = new ArrayList<>();

        for (Team team : teams) {
            List<Long> users = teamUsersRepository.findUsersByTeamId(team.getId());
            teamDTOS.add(buildTeamDTO(team, users));
        }

        return teamDTOS;
    }

    private TeamDTO buildTeamDTO(Team team, List<Long> users) {
        return TeamDTO.builder()
                .team(team)
                .users(users)
                .build();
    }

    void validateTeamName(String teamName, Long id) {
        if (teamName == null || teamName.isBlank()) {
            throw new IllegalArgumentException("Team name cannot be null or empty");
        }

        boolean exists = teamRepository.findByTeamName(teamName).isPresent();
        if (exists) {
            // Sprawdzamy czy to nie jest ten sam team przy aktualizacji
            if (id != null) {
                Team team = teamRepository.findByTeamName(teamName)
                        .orElseThrow(() -> new EntityNotFoundException("Team not found with name: " + teamName));
                if (!team.getId().equals(id)) {
                    throw new IllegalArgumentException("A team with this name already exists");
                }
            } else {
                // tworzenie nowego teamu i nazwa już istnieje
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
