package pwr.isa.backend.Posters.MatchPosters;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pwr.isa.backend.GameSystem.GameService;
import pwr.isa.backend.GameSystem.MatchParticipants.MatchParticipant;
import pwr.isa.backend.GameSystem.MatchParticipants.MatchParticipantsRepository;
import pwr.isa.backend.Team.TeamDTO;
import pwr.isa.backend.Team.TeamService;
import pwr.isa.backend.Team.TeamUsers.TeamUsersRepository;
import pwr.isa.backend.User.UserService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/*
    Service class for MatchPoster
    TODO - PRZETESTOWAC
 */

@Transactional
@Service
public class MatchPosterServiceImpl implements MatchPosterService{
    private final MatchPosterRepository matchPosterRepository;
    private final MatchParticipantsRepository matchParticipantRepository;
    private final GameService gameService;
    private final UserService userService;
    private final TeamService teamService;
    private final TeamUsersRepository teamUsersRepository;

    public MatchPosterServiceImpl(MatchPosterRepository matchPosterRepository,
                                  MatchParticipantsRepository matchParticipantRepository,
                                  @Lazy GameService gameService,
                                  UserService userService, TeamService teamService, TeamUsersRepository teamUsersRepository) {
        this.matchPosterRepository = matchPosterRepository;
        this.matchParticipantRepository = matchParticipantRepository;
        this.gameService = gameService;
        this.userService = userService;
        this.teamService = teamService;
        this.teamUsersRepository = teamUsersRepository;
    }

    @Override
    public MatchPosterDTO createMatchPoster(MatchPoster matchPoster, Long teamId) {
        matchPoster.setId(null);

        if (!userService.exists(matchPoster.getOwnerId())) {
            throw new IllegalArgumentException("Owner does not exist");
        }

        matchPoster.setCreatedAt(new Date());
        matchPoster.setUpdatedAt(new Date());

        if (teamId != null) {
            List<Long> teamMembers = teamUsersRepository.findUsersByTeamId(teamId);
            for (Long userId : teamMembers) {
                matchParticipantRepository.addMatchParticipant(matchPoster.getId(), userId, teamId, 100);
            }
            matchParticipantRepository.addMatchParticipant(matchPoster.getId(), matchPoster.getOwnerId(), teamId, 100);
        } else {
            matchParticipantRepository.addMatchParticipant(matchPoster.getId(), matchPoster.getOwnerId(), null, 100);
        }

        matchPosterRepository.save(matchPoster);
        return buildMatchPosterDTO(matchPoster.getId());
    }

    @Override
    public MatchPosterDTO updateMatchPoster(Long posterId, MatchPoster matchPoster) {
        MatchPoster found = matchPosterRepository.findById(matchPoster.getId())
                .orElseThrow(() -> new IllegalArgumentException("Match poster not found"));

        if (!userService.exists(matchPoster.getOwnerId())) {
            throw new IllegalArgumentException("Owner needs to exist");
        }

        if (matchPoster.getRanked() == null) {
            matchPoster.setRanked(found.getRanked());
        }
        matchPoster.setCreatedAt(found.getCreatedAt());
        matchPoster.setUpdatedAt(new Date());
        matchPosterRepository.save(matchPoster);
        return buildMatchPosterDTO(posterId);
    }

    @Transactional
    @Override
    public MatchPosterDTO joinMatchPoster(Long posterId, Long userId, int team) {
        MatchPoster foundMatchPoster = matchPosterRepository.findById(posterId)
                .orElseThrow(() -> new IllegalArgumentException("Match poster not found"));

        List<MatchParticipant> participantList = validatePlayer(posterId, userId);


        if (checkTeamSize(participantList, team) >= 5) {
            throw new IllegalArgumentException("Team is full");
        }

        matchParticipantRepository.addMatchParticipant(posterId, userId, null, team);
        foundMatchPoster.setUpdatedAt(new Date());
        matchPosterRepository.save(foundMatchPoster);

        return buildMatchPosterDTO(posterId);
    }

    @Transactional
    @Override
    public MatchPosterDTO joinAsTeam(Long posterId, Long teamId) {
        TeamDTO teamDTO = teamService.getTeamById(teamId);
        List<Long> teamMembers = teamDTO.getUsers();

        if(teamDTO == null || teamDTO.getUsers() == null || teamDTO.getUsers().size() < 5) {
            throw new IllegalArgumentException("Invalid Team");
        }

        MatchPoster foundMatchPoster = matchPosterRepository.findById(posterId)
                .orElseThrow(() -> new IllegalArgumentException("Match poster not found"));

        List<MatchParticipant> participantList = matchParticipantRepository.findMatchParticipantsByMatchId(posterId);

        int team100 = checkTeamSize(participantList, 100);
        int team200 = checkTeamSize(participantList, 200);

        if (team100 != 0 || team200 != 0) {
            throw new IllegalArgumentException("Teams are already full");
        }

        int assignedTeam = (team100 == 0) ? 100 : 200;

        for (Long userId : teamMembers) {
            matchParticipantRepository.addMatchParticipant(posterId, userId, teamId, assignedTeam);
        }

        return buildMatchPosterDTO(posterId);
    }

    @Transactional
    @Override
    public MatchPosterDTO leaveMatchPoster(Long posterId, Long userId) {
        matchPosterRepository.findById(posterId)
                .orElseThrow(() -> new IllegalArgumentException("Match poster not found"));

        matchParticipantRepository.deleteMatchParticipant(posterId, userId);

        return buildMatchPosterDTO(posterId);
    }

    @Transactional
    @Override
    public void deleteMatchPoster(Long posterId) {
        MatchPoster foundMatchPoster = matchPosterRepository.findById(posterId)
                .orElseThrow(() -> new IllegalArgumentException("Match poster not found"));

        matchParticipantRepository.deleteAllByMatchId(posterId);
        matchPosterRepository.deleteById(posterId);
    }

    @Override
    public List<MatchPosterDTO> getAllMatchPosters(int limit, int offset) {
        List<MatchPoster> matchPosters = matchPosterRepository.findMatchPostersByDueDate(limit, offset);
        List<MatchPosterDTO> matchPosterDTOS = new ArrayList<>();

        for (MatchPoster matchPoster : matchPosters) {
            matchPosterDTOS.add(buildMatchPosterDTO(matchPoster.getId()));
        }
        return matchPosterDTOS;
    }

    @Override
    public MatchPosterDTO getMatchPosterById(Long posterId) {
        matchPosterRepository.findById(posterId)
                .orElseThrow(() -> new IllegalArgumentException("Match poster not found"));
        return buildMatchPosterDTO(posterId);
    }

    @Override
    public MatchPosterDTO startMatch(Long posterId) {
        MatchPoster found = matchPosterRepository.findById(posterId)
                .orElseThrow(() -> new IllegalArgumentException("Match poster not found"));
        gameService.startGame(posterId);
        found.setArchived(true);
        return buildMatchPosterDTO(posterId);
    }

    @Override
    public MatchPosterDTO retriveMatchPoster(Long posterId) {
        MatchPoster found = matchPosterRepository.findById(posterId)
                .orElseThrow(() -> new IllegalArgumentException("Match poster not found"));

        Date dueDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
        found.setDueDate(dueDate);
        found.setArchived(false);
        return buildMatchPosterDTO(posterId);
    }

    private List<MatchParticipant> validatePlayer(Long matchId, Long userId) {
        List<MatchParticipant> players = matchParticipantRepository.findMatchParticipantsByMatchId(matchId);

        for (MatchParticipant player : players) {
            if (player.getUserId().equals(userId)) {
                throw new IllegalArgumentException("User already joined the match");
            }
        }

        if(!userService.exists(userId)){
            throw new IllegalArgumentException("User does not exist");
        }

        return players;
    }

    private int checkTeamSize(List<MatchParticipant> players, int team) {
        int teamSize = 0;
        for (MatchParticipant player : players) {
            if (player.getRiot_team_number() == team) {
                teamSize++;
            }
        }
        return teamSize;
    }

    private MatchPosterDTO buildMatchPosterDTO(Long posterId) {
        Optional<MatchPoster> matchPoster = matchPosterRepository.findById(posterId);
        List<MatchParticipant> matchParticipants = matchParticipantRepository.findMatchParticipantsByMatchId(posterId);

        return MatchPosterDTO.builder()
                .matchPoster(matchPoster.get())
                .participants(matchParticipants)
                .build();
    }
}
