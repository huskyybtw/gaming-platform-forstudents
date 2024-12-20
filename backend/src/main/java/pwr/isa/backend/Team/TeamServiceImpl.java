package pwr.isa.backend.Team;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pwr.isa.backend.Posters.MatchPosters.MatchPoster;
import pwr.isa.backend.Posters.MatchPosters.MatchPosterService;
import pwr.isa.backend.Posters.TeamPosters.TeamPosterRepository;
import pwr.isa.backend.Posters.TeamPosters.TeamPosterService;
import pwr.isa.backend.Team.TeamUsers.TeamUsersRepository;
import pwr.isa.backend.User.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/*
    * TODO do przetestowania
*/
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamUsersRepository teamUsersRepository;
    private final TeamPosterService teamPosterService;
    private final MatchPosterService matchPosterService;
    private final UserService userService;

    public TeamServiceImpl(TeamRepository teamRepository,
                           TeamUsersRepository teamUsersRepository, TeamPosterService teamPosterService, MatchPosterService matchPosterService,
                           UserService userService) {
        this.teamRepository = teamRepository;
        this.teamPosterService = teamPosterService;
        this.matchPosterService = matchPosterService;
        this.userService = userService;
        this.teamUsersRepository = teamUsersRepository;
    }


    @Transactional
    @Override
    public TeamDTO createTeam(Team team) {
        validateTeamName(team.getTeamName(), null);
        validateTeamCaptain(team.getTeamCaptain());

        Team createdTeam = teamRepository.save(team);
        teamUsersRepository.addTeamUser(createdTeam.getId(), createdTeam.getTeamCaptain());
        return buildTeamDTO(createdTeam, new ArrayList<>());
    }

    @Transactional
    @Override
    public TeamDTO updateTeam(Long id, Team updatedTeam) {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + id));

        validateTeamName(updatedTeam.getTeamName(), id);
        validateTeamCaptain(updatedTeam.getTeamCaptain());

        existingTeam.setTeamName(updatedTeam.getTeamName());
        existingTeam.setDescription(updatedTeam.getDescription());
        existingTeam.setTeamCaptain(updatedTeam.getTeamCaptain());

        Team savedTeam = teamRepository.save(existingTeam);

        List<Long> users = teamUsersRepository.findUsersByTeamId(id);

        return buildTeamDTO(savedTeam, users);
    }

    @Transactional
    @Override
    public TeamDTO addPlayerToTeam(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + teamId));

        if (!userService.exists(userId)) {
            throw new IllegalArgumentException("User with id: " + userId + " does not exist");
        }

        List<Long> currentUsers = teamUsersRepository.findUsersByTeamId(teamId);
        if (currentUsers.contains(userId)) {
            throw new IllegalArgumentException("User with id: " + userId + " is already in the team");
        }

        teamUsersRepository.addTeamUser(teamId, userId);
        currentUsers.add(userId);

        return buildTeamDTO(team, currentUsers);
    }

    @Transactional
    @Override
    public TeamDTO removePlayerFromTeam(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + teamId));

        List<Long> currentUsers = teamUsersRepository.findUsersByTeamId(teamId);
        if (!currentUsers.contains(userId)) {
            throw new IllegalArgumentException("User with id: " + userId + " is not in the team");
        }

        teamUsersRepository.deleteTeamUser(teamId, userId);
        currentUsers.remove(userId);

        return buildTeamDTO(team, currentUsers);
    }

    @Transactional
    @Override
    public void deleteTeam(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new EntityNotFoundException("Team not found with id: " + id);
        }
        Team team = teamRepository.findById(id).get();
        team.setArchived(true);
        teamRepository.save(team);
        teamPosterService.deleteTeamPoster(id);
        teamUsersRepository.deleteAllByTeamId(id);
    }

    @Transactional
    @Override
    public TeamDTO getTeamById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + id));
        List<Long> users = teamUsersRepository.findUsersByTeamId(id);
        return buildTeamDTO(team, users);
    }

    @Override
    public List<TeamDTO> getAllTeams(int limit, int offset, String sortBy, String sortDirection) {
        String sortColumn = switch (sortBy.toLowerCase()) {
            case "rating" -> "rating";
            default -> "team_name";
        };

        List<Team> teams = sortDirection.equalsIgnoreCase("DESC")
                ? teamRepository.findAllSortedDesc(limit, offset, sortColumn)
                : teamRepository.findAllSortedAsc(limit, offset, sortColumn);

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
                .users(users != null ? users : new ArrayList<>())
                .build();
    }

    void validateTeamName(String teamName, Long id) {
        if (teamName == null || teamName.isBlank()) {
            throw new IllegalArgumentException("Team name cannot be null or empty");
        }

        Optional<Team> team = teamRepository.findByTeamName(teamName);
        if (team.isPresent()) {
            if (id == null || !team.get().getId().equals(id)) {
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
