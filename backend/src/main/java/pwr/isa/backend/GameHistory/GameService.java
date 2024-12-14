package pwr.isa.backend.GameHistory;

import org.springframework.scheduling.annotation.Scheduled;
import pwr.isa.backend.RIOT.DTO.MatchDetailsDTO;

public interface GameService {
    GameHistory getGameHistoryById(Long id);
    Iterable<GameHistory> getAllGameHistories();
    Iterable<GameHistory> getGameHistoriesByUserId(Long userId, int limit);
    Iterable<GameHistory> getGameHistoriesByTeamId(Long teamId, int limit);
    GameHistory createGameHistory(GameHistory gameHistory);
    GameHistory updateGameHistory(GameHistory gameHistory, Long id);
    void deleteGameHistory(Long id);
    GameHistory stageGame();
    GameHistory startGame(GameHistory gameHistory,Long id);
    GameHistory endGame(Long id, MatchDetailsDTO matchDetailsDTO);

    @Scheduled(fixedRate = 60 * 1000)
    void checkGames();
}
