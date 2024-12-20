package pwr.isa.backend.GameSystem;

import org.springframework.scheduling.annotation.Scheduled;
import pwr.isa.backend.Consumer.DTO.MatchDetailsDTO;

import java.util.List;

public interface GameService {
    GameHistoryDTO getGameHistoryById(Long id);
    Iterable<GameHistoryDTO> getAllGameHistories(int limit, int offset);
    List<GameHistoryDTO> getGameHistoriesByUserId(Long userId, int limit, int offset);
    List<GameHistoryDTO> getGameHistoriesByTeamId(Long teamId, int limit, int offset);
    GameHistoryDTO createGameHistory(GameHistory gameHistory);
    GameHistoryDTO updateGameHistory(GameHistory gameHistory, Long id);
    void deleteGameHistory(Long id);
    GameHistoryDTO startGame(Long id);

    @Scheduled(fixedRate = 60 * 10000)
    void LookForStartedMatches();

    @Scheduled(fixedRate = 60 * 1000)
    void LookForEndedMatches();

    GameHistoryDTO endGame(GameHistory gameHistory, MatchDetailsDTO matchDetailsDTO);
}
