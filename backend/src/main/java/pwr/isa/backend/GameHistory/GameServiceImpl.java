package pwr.isa.backend.GameHistory;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pwr.isa.backend.GameHistory.MatchParticipants.MatchParticipant;
import pwr.isa.backend.GameHistory.MatchParticipants.MatchParticipantsRepository;
import pwr.isa.backend.Player.Player;
import pwr.isa.backend.Player.PlayerRepository;
import pwr.isa.backend.Posters.MatchPosters.MatchPosterRepository;
import pwr.isa.backend.RIOT.DTO.MatchDetailsDTO;
import pwr.isa.backend.RIOT.RiotService;
import pwr.isa.backend.Rating.RatingService;

import java.util.*;

/*
    * Sevice for managing games
    *
    *
    *
    * GameHistory doesnt contain participants at the moment
    * So at the moment we are not getting participants in games
    * Mozna naprawic robiac gameHistory DTO
 */

@Service
public class GameServiceImpl implements GameService {

    private final GameHistoryRepository gameHistoryRepository;
    private final MatchParticipantsRepository matchParticipantsRepository;
    private final MatchPosterRepository matchPosterRepository;
    private final PlayerRepository playerRepository;
    private final RiotService riotService;
    private final RatingService ratingService;
    private final HashSet<GameHistory> onGoingGames = new HashSet<>();
    private final HashSet<GameHistory> startedGames = new HashSet<>();

    public GameServiceImpl(GameHistoryRepository gameHistoryRepository,
                           MatchParticipantsRepository matchParticipantsRepository,
                           MatchPosterRepository matchPosterRepository,
                           PlayerRepository playerRepository,
                           RiotService riotService, RatingService ratingService) {
        this.gameHistoryRepository = gameHistoryRepository;
        this.matchParticipantsRepository = matchParticipantsRepository;
        this.matchPosterRepository = matchPosterRepository;
        this.playerRepository = playerRepository;
        this.riotService = riotService;
        this.ratingService = ratingService;
    }

    @Override
    public Iterable<GameHistory> getAllGameHistories() {
        return gameHistoryRepository.findAll();
    }

    @Override
    public GameHistory getGameHistoryById(Long id) {
        return gameHistoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game with ID " + id + " not found"));
    }

    // POTRZEBNE FEATUERY ALE JEST PROBLEM Z TYM ZE TRZYMAMY GRACZY
    // W TAKI SPOSOB ZE NIE MA JAK TEGO Z QUERROWAC I NIE MA JAK TEAMOW QUERROWAC
    // JAKIS JOIN TABLE TRZEBA ZROBIC 
    @Override
    public Iterable<GameHistory> getGameHistoriesByUserId(Long userId, int limit) {
        List<Long> matchIds = matchParticipantsRepository.findMatchesByUserId(userId);
        return sortGames(matchIds, limit);
    }

    @Override
    public Iterable<GameHistory> getGameHistoriesByTeamId(Long teamId, int limit) {
        List<Long> matchIds = matchParticipantsRepository.findMatchesByTeamId(teamId);
        return sortGames(matchIds, limit);
    }

    @Override
    public GameHistory createGameHistory(GameHistory gameHistory) {
        return gameHistoryRepository.save(gameHistory);
    }

    @Override
    public GameHistory updateGameHistory(GameHistory gameHistory, Long id) {
        gameHistory.setId(id);
        return gameHistoryRepository.save(gameHistory);
    }

    @Override
    public void deleteGameHistory(Long id) {
        gameHistoryRepository.deleteById(id);
    }


    @Override
    public GameHistory startGame(Long matchId) {
        GameHistory newGame = GameHistory.builder()
                                    .matchStatus(MatchStatus.ON_GOING)
                                    .matchId(matchId)
                                    .startDate(new Date())
                                    .build();

        List<MatchParticipant> players = matchParticipantsRepository.findMatchParticipantsByMatchId(matchId);

        if(players.size() != 10) {
            throw new RuntimeException("Game have incorrect amount of players, could not start the game");
        }
        newGame.setMatchStatus(MatchStatus.ON_GOING);

        startedGames.add(newGame);
        return gameHistoryRepository.save(newGame);
    }

    @Scheduled(fixedRate = 60 * 10000)
    @Override
    public void findRiotMatchId(){
        ArrayList<GameHistory> toRemove = new ArrayList<>();
        for(GameHistory game : startedGames) {
            if(game.getMatchStatus() == MatchStatus.ON_GOING) {

            }
                List<Long> matchParticipants =  matchParticipantsRepository.findPlayersByMatchId(game.getMatchId());

                List<Player> players = new ArrayList<>();
                players.add(playerRepository.findById(matchParticipants.get(0)).get());
                players.add(playerRepository.findById(matchParticipants.get(0)).get());

                /*
                riotService.getLiveMatchDTO(players.get(0).getPuuid());
                riotService.getLiveMatchDTO(players.get(1).getPuuid());

                porownach otrzymane match id jest dziala to mecz sie zaczal
                game.setMatchId();
                gameHistoryRepository.save(game);
                */

                onGoingGames.add(game);
                toRemove.add(game);
        }
        toRemove.forEach(startedGames::remove);
    }

    @Scheduled(fixedRate = 60 * 1000)
    @Override
    public void checkGames() {
        ArrayList<GameHistory> toRemove = new ArrayList<>();
        for(GameHistory game : onGoingGames) {
            if(game.getMatchStatus() == MatchStatus.ON_GOING) {
                /*

                MatchDetailsDTO match = riotService.getMatchDetailsDTO(game.getMatchId());
                if(!Objects.equals(match.getEndOfGameResult(), "GameComplete")) {continue;}
                game.setMatchStatus(MatchStatus.FINISHED);
                endGame(game.getId(), match);
                toRemove.add(game);
                 */
            }
        }
        onGoingGames.removeAll(toRemove);
    }

    @Override
    public GameHistory endGame(Long id, MatchDetailsDTO matchDetailsDTO) {
        GameHistory gameHistory = gameHistoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game with ID " + id + " not found"));

        if(gameHistory.getMatchStatus() != MatchStatus.FINISHED) {
            throw new IllegalArgumentException("Game is not on going");
        }


        gameHistory.setWinner(matchDetailsDTO.getWinner());
        gameHistory.setEndOfMatchDate(new Date());
        gameHistory.setJsonData(matchDetailsDTO.toString());

        List<MatchParticipant> players = matchParticipantsRepository.findMatchParticipantsByMatchId(gameHistory.getMatchId());

        if(players.size() != 10) {
            throw new RuntimeException("Game have incorrect amount of players, could not start the game");
        }

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
        return gameHistoryRepository.save(gameHistory);
    }


    @Override
    public List<GameHistory> sortGames(List<Long> matchIds, int limit) {
        List<GameHistory> gameHistories = new ArrayList<>();

        for (Long matchId : matchIds) {
            gameHistories.add(gameHistoryRepository.findById(matchId)
                    .orElseThrow(() -> new EntityNotFoundException("Game with ID " + matchId + " not found")));
        }
        // Slow approach but good for now
        Collections.sort(gameHistories);
        return gameHistories.subList(0, Math.min(limit, gameHistories.size()));
    }

}
