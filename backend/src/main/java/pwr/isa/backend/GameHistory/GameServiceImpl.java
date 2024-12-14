package pwr.isa.backend.GameHistory;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pwr.isa.backend.GameHistory.MatchParticipants.MatchParticipantsRepository;
import pwr.isa.backend.RIOT.DTO.MatchDetailsDTO;

import java.util.*;

@Service
public class GameServiceImpl implements GameService {

    private final GameHistoryRepository gameHistoryRepository;
    private final MatchParticipantsRepository matchParticipantsRepository;
    private final HashSet<GameHistory> onGoingGames = new HashSet<>();

    public GameServiceImpl(GameHistoryRepository gameHistoryRepository, MatchParticipantsRepository matchParticipantsRepository) {
        this.gameHistoryRepository = gameHistoryRepository;
        this.matchParticipantsRepository = matchParticipantsRepository;
    }

    @Override
    public GameHistory getGameHistoryById(Long id) {
        return gameHistoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game with ID " + id + " not found"));
    }

    @Override
    public Iterable<GameHistory> getAllGameHistories() {
        return gameHistoryRepository.findAll();
    }


    // POTRZEBNE FEATUERY ALE JEST PROBLEM Z TYM ZE TRZYMAMY GRACZY
    // W TAKI SPOSOB ZE NIE MA JAK TEGO Z QUERROWAC I NIE MA JAK TEAMOW QUERROWAC
    // JAKIS JOIN TABLE TRZEBA ZROBIC 
    @Override
    public Iterable<GameHistory> getGameHistoriesByUserId(Long userId, int limit) {
        List<Long> matchIds = matchParticipantsRepository.findMatchesByUserId(userId);
        List<GameHistory> gameHistories = new ArrayList<>();

        for (Long matchId : matchIds) {
            gameHistories.add(gameHistoryRepository.findById(matchId)
                    .orElseThrow(() -> new EntityNotFoundException("Game with ID " + matchId + " not found")));
        }

        Collections.sort(gameHistories);
        return gameHistories.subList(0, Math.min(limit, gameHistories.size()));
    }

    @Override
    public Iterable<GameHistory> getGameHistoriesByTeamId(Long teamId, int limit) {
        return null;
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
    public GameHistory stageGame() {
        return GameHistory.builder()
                .Id(null)
                .stagingDate(new Date())
                .matchStatus(MatchStatus.STAGING)
                .build();
    }

    @Override
    public GameHistory startGame(GameHistory gameHistory,Long id) {
        GameHistory foundGame = gameHistoryRepository.findById(id).
            orElseThrow(()-> new EntityNotFoundException("Game with ID " + id + " not found"));

        if(foundGame.getMatchStatus() != MatchStatus.STAGING) {
            throw new IllegalArgumentException("Game is not in staging state");
        }

        gameHistory.setMatchStatus(MatchStatus.ON_GOING);
        // find a way to get match id at this point
        // find a way to store players
        foundGame = gameHistory;
        onGoingGames.add(foundGame);
        return gameHistoryRepository.save(foundGame);
    }

    @Override
    public GameHistory endGame(Long id, MatchDetailsDTO matchDetailsDTO) {
        GameHistory gameHistory = gameHistoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game with ID " + id + " not found"));

        gameHistory.setMatchStatus(MatchStatus.FINISHED);
        gameHistory.setWinner(matchDetailsDTO.getWinner());
        gameHistory.setEndOfMatchDate(new Date());
        gameHistory.setJsonData(matchDetailsDTO.toString());
        return null;
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
            }
        }
        onGoingGames.removeAll(toRemove);
    }

}
