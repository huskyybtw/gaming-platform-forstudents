package pwr.isa.backend.GameSystem;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pwr.isa.backend.GameSystem.MatchParticipants.MatchParticipant;
import pwr.isa.backend.GameSystem.MatchParticipants.MatchParticipantsRepository;
import pwr.isa.backend.Player.Player;
import pwr.isa.backend.Player.PlayerRepository;
import pwr.isa.backend.Posters.MatchPosters.MatchPosterRepository;
import pwr.isa.backend.Posters.MatchPosters.MatchPosterService;
import pwr.isa.backend.Consumer.DTO.LiveMatchDTO;
import pwr.isa.backend.Consumer.DTO.LiveMatchStatus;
import pwr.isa.backend.Consumer.DTO.MatchDetailsDTO;
import pwr.isa.backend.Consumer.RiotService;
import pwr.isa.backend.GameSystem.Rating.RatingService;

import java.util.*;

/*
    * Sevice for managing games
    *  TODO PRZETESTOWAC
    *  TODO SORTOWANIA PO NAJSTARSZYCH MECZACH
 */

@Transactional
@Service
public class GameServiceImpl implements GameService {

    private final GameHistoryRepository gameHistoryRepository;
    private final MatchParticipantsRepository matchParticipantsRepository;
    private final MatchPosterRepository matchPosterRepository;
    private final PlayerRepository playerRepository;
    private final MatchPosterService matchPosterService;
    private final RiotService riotService;
    private final RatingService ratingService;
    private final HashMap<GameHistory, Player> onGoingGames = new HashMap<>();
    private final HashSet<GameHistory> startedGames = new HashSet<>();

    public GameServiceImpl(GameHistoryRepository gameHistoryRepository,
                           MatchParticipantsRepository matchParticipantsRepository,
                           MatchPosterRepository matchPosterRepository,
                           PlayerRepository playerRepository,
                           RiotService riotService, RatingService ratingService,
                           MatchPosterService matchPosterService) {
        this.gameHistoryRepository = gameHistoryRepository;
        this.matchParticipantsRepository = matchParticipantsRepository;
        this.matchPosterRepository = matchPosterRepository;
        this.playerRepository = playerRepository;
        this.riotService = riotService;
        this.ratingService = ratingService;
        this.matchPosterService = matchPosterService;
    }

    @Override
    public Iterable<GameHistoryDTO> getAllGameHistories(int limit, int offset) {

        List<GameHistoryDTO> gameHistoryDTOS = new ArrayList<>();
        for(GameHistory gameHistory : gameHistoryRepository.findAll(limit, offset)) {
            List<MatchParticipant> matchParticipants = matchParticipantsRepository.findMatchParticipantsByMatchId(gameHistory.getMatchId());
            gameHistoryDTOS.add(buildGameHistoryDTO(gameHistory, matchParticipants));
        }
        return gameHistoryDTOS;
    }

    @Override
    public GameHistoryDTO getGameHistoryById(Long id) {
        GameHistory gameHistory = gameHistoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("GameHistory with id: " + id + " not found"));
        List<MatchParticipant> matchParticipants = matchParticipantsRepository.findMatchParticipantsByMatchId(gameHistory.getMatchId());
        return buildGameHistoryDTO(gameHistory, matchParticipants);
    }

    @Override
    public List<GameHistoryDTO> getGameHistoriesByUserId(Long userId, int limit, int offset) {
        List<GameHistory> gameHistories = gameHistoryRepository.findAllMatchesByUserIdSorted(userId, limit, offset);
        return buildGameHistoryDTOS(gameHistories);
    }

    @Override
    public List<GameHistoryDTO> getGameHistoriesByTeamId(Long teamId, int limit, int offset) {
        List<GameHistory> gameHistories = gameHistoryRepository.findAllMatchesByTeamIdSorted(teamId, limit, offset);
        return buildGameHistoryDTOS(gameHistories);
    }

    @Override
    public void deleteGameHistory(Long id) {
        gameHistoryRepository.deleteById(id);
    }

    @Transactional
    @Override
    public GameHistoryDTO createGameHistory(GameHistory gameHistory) {
        gameHistory.setId(null);
        GameHistory savedGame = gameHistoryRepository.save(gameHistory);
        return getGameHistoryById(savedGame.getId());
    }

    @Transactional
    @Override
    public GameHistoryDTO updateGameHistory(GameHistory gameHistory, Long id) {
        gameHistory.setId(id);
        GameHistory savedGame = gameHistoryRepository.save(gameHistory);
        return getGameHistoryById(savedGame.getId());
    }

    @Transactional
    @Override
    public GameHistoryDTO startGame(Long matchId) {
        GameHistory newGame = GameHistory.builder()
                                    .matchStatus(MatchStatus.ON_GOING)
                                    .matchId(matchId)
                                    .startMatchDate(new Date())
                                    .build();

        List<MatchParticipant> players = matchParticipantsRepository.findMatchParticipantsByMatchId(matchId);

        if(players.size() != 10) {
            throw new RuntimeException("Game have incorrect amount of players, could not start the game");
        }
        newGame.setMatchStatus(MatchStatus.ON_GOING);

        startedGames.add(newGame);
        gameHistoryRepository.save(newGame);
        return buildGameHistoryDTO(newGame, players);
    }

    @Scheduled(fixedRate = 60 * 10000)
    @Override
    public void LookForStartedMatches(){
        ArrayList<GameHistory> toRemove = new ArrayList<>();
        for(GameHistory game : startedGames) {
            if(game.getMatchStatus() == MatchStatus.ON_GOING) {
                List<MatchParticipant> matchParticipants = matchParticipantsRepository.findMatchParticipantsByMatchId(game.getMatchId());

                List<Player> players = new ArrayList<>();
                for (MatchParticipant matchParticipant : matchParticipants) {
                    Player player = playerRepository.findByUserId(matchParticipant.getUserId());

                    if (player == null || player.getPuuid() == null) {
                        startedGames.remove(game);
                        matchPosterService.retriveMatchPoster(game.getMatchId());
                        break;
                    }

                    players.add(player);
                }

                LiveMatchDTO liveMatchDTO = riotService.getLiveMatchDTO(players.get(0).getPuuid());

                if (liveMatchDTO.getStatus() != LiveMatchStatus.IN_PROGRESS) {
                    continue;
                }

                // CHECK IF PARTICIPANTS MATCHES
                // nested for loops but its fine beacuse all are size 10
                // there might be a better way to do this
                boolean misMatch = false;
                for (var matchParticipant : liveMatchDTO.getParticipants()) {
                    for (Player player : players) {
                        for (MatchParticipant participant : matchParticipants) {
                            {
                                if (
                                        Objects.equals(participant.getUserId(), player.getUserId())
                                                && !matchParticipant.getPuuid().equals(player.getPuuid())
                                                && matchParticipant.getTeamId() != participant.getRiot_team_number()
                                ) {
                                    misMatch = true;
                                    break;
                                }
                            }
                        }
                    }
                }

                if (misMatch) {
                    startedGames.remove(game);
                    matchPosterService.retriveMatchPoster(game.getMatchId());
                    continue;
                }

                onGoingGames.put(game, players.get(0));
                toRemove.add(game);
            }
        }

        toRemove.forEach(startedGames::remove);
    }

    @Scheduled(fixedRate = 60 * 1000)
    @Override
    public void LookForEndedMatches() {
        ArrayList<GameHistory> toRemove = new ArrayList<>();
        for(GameHistory game : onGoingGames.keySet()) {
            if(game.getMatchStatus() == MatchStatus.ON_GOING) {
                LiveMatchDTO liveMatchDTO = riotService.getLiveMatchDTO(onGoingGames.get(game).getPuuid());

                if(liveMatchDTO.getStatus() != LiveMatchStatus.IN_PROGRESS) {
                    MatchDetailsDTO match = riotService.getMatchDetailsDTO(game.getRiotMatchId());

                    if(!Objects.equals(match.getEndOfGameResult(), "GameComplete")) {continue;}

                    game.setMatchStatus(MatchStatus.FINISHED);
                    endGame(game, match);
                    toRemove.add(game);
                }
            }
        }
        for(GameHistory game : toRemove) {
            onGoingGames.remove(game);
        }
    }

    @Transactional
    @Override
    public GameHistoryDTO endGame(GameHistory gameHistory, MatchDetailsDTO matchDetailsDTO) {

        if(gameHistory.getMatchStatus() != MatchStatus.FINISHED) {
            throw new IllegalArgumentException("Game is not on going");
        }


        gameHistory.setWinner(matchDetailsDTO.getWinner());
        gameHistory.setEndMatchDate(new Date());
        gameHistory.setJsonData(matchDetailsDTO.toString());

        List<MatchParticipant> players = matchParticipantsRepository.findMatchParticipantsByMatchId(gameHistory.getMatchId());
        /*
           Placeholder +25 - 25 w przyszlosci algorytm
         */
        for (var player : players) {
            if(player.getRiot_team_number() == gameHistory.getWinner()){
                ratingService.updatePlayerRating(player.getUserId(), 25);
            } else {
                ratingService.updatePlayerRating(player.getUserId(), -25);
            }
        }
        GameHistory saved =gameHistoryRepository.save(gameHistory);
        return buildGameHistoryDTO(saved, players);
    }

    private GameHistoryDTO buildGameHistoryDTO(GameHistory gameHistory, List<MatchParticipant> matchParticipants) {
        return GameHistoryDTO.builder()
                .gameHistory(gameHistory)
                .matchParticipants(matchParticipants)
                .build();
    }

    private List<GameHistoryDTO> buildGameHistoryDTOS(List<GameHistory> gameHistories) {
        List<GameHistoryDTO> gameHistoryDTOS = new ArrayList<>();
        for(GameHistory gameHistory : gameHistories) {
            List<MatchParticipant> matchParticipants = matchParticipantsRepository.findMatchParticipantsByMatchId(gameHistory.getMatchId());
            gameHistoryDTOS.add(buildGameHistoryDTO(gameHistory, matchParticipants));
        }
        return gameHistoryDTOS;
    }
}
