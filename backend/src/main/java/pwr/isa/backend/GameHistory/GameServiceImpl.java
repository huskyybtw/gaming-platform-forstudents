package pwr.isa.backend.GameHistory;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pwr.isa.backend.GameHistory.MatchParticipants.MatchParticipant;
import pwr.isa.backend.GameHistory.MatchParticipants.MatchParticipantsRepository;
import pwr.isa.backend.RIOT.DTO.MatchDetailsDTO;
import pwr.isa.backend.RIOT.RiotServiceImpl;

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
    private final RiotServiceImpl riotService;
    private final HashSet<GameHistory> onGoingGames = new HashSet<>();

    public GameServiceImpl(GameHistoryRepository gameHistoryRepository,
                           MatchParticipantsRepository matchParticipantsRepository,
                           RiotServiceImpl riotService) {
        this.gameHistoryRepository = gameHistoryRepository;
        this.matchParticipantsRepository = matchParticipantsRepository;
        this.riotService = riotService;
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
    public GameHistory startGame(GameHistory gameHistory,Long id) {
        GameHistory foundGame = gameHistoryRepository.findById(id).
            orElseThrow(()-> new EntityNotFoundException("Game with ID " + id + " not found"));

        List<MatchParticipant> players = matchParticipantsRepository.findMatchParticipantsByMatchId(id);

        if(players.size() != 10) {
            throw new RuntimeException("Game have incorrect amount of players, could not start the game");
        }

        /*
            NA CZAS JAK NIE MAM OD MICHALA
            querry puuid from player repository
            fetch matchId from riotService ussing puuid
            String matchId = riotService.getUserMatches().get(0);
            foundGame.setMatchId(matchId);
         */
        
        foundGame.setMatchStatus(MatchStatus.ON_GOING);


        onGoingGames.add(foundGame);
        return gameHistoryRepository.save(foundGame);
    }

    @Scheduled(fixedRate = 60 * 1000)
    @Override
    public void checkGames() {
        ArrayList<GameHistory> toRemove = new ArrayList<>();
        for(GameHistory game : onGoingGames) {
            if(game.getMatchStatus() == MatchStatus.ON_GOING) {
                // check if game has ended
                // if ended, set status to FINISHED
                // remove from onGoingGames
                //MatchDetailsDTO match = riotService.getMatchDetailsDTO(game.riotMatchId);

                //if(!Objects.equals(match.getEndOfGameResult(), "GameComplete")) {continue;}

                game.setMatchStatus(MatchStatus.FINISHED);
                //endGame(game.getId(), match);
                toRemove.add(game);
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

        List<MatchParticipant> players = matchParticipantsRepository.findMatchParticipantsByMatchId(id);

        if(players.size() != 10) {
            throw new RuntimeException("Game have incorrect amount of players, could not start the game");
        }

        for (var player : players) {
            if(player.getRiot_team_number() == gameHistory.getWinner()){
                // + ranking
            } else {
                // - ranking
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
