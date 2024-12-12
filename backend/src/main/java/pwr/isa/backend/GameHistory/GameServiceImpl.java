package pwr.isa.backend.GameHistory;

import org.springframework.stereotype.Service;
import pwr.isa.backend.RIOT.DTO.MatchDetailsDTO;

@Service
public class GameServiceImpl implements GameService {
    private final GameHistoryRepository gameHistoryRepository;

    public GameServiceImpl(GameHistoryRepository gameHistoryRepository) {
        this.gameHistoryRepository = gameHistoryRepository;
    }

    @Override
    public GameHistory getGameHistoryById(Long id) {
        return null;
    }

    @Override
    public Iterable<GameHistory> getAllGameHistories() {
        return null;
    }

    @Override
    public Iterable<GameHistory> getGameHistoriesByUserId(Long userId, int limit) {
        return null;
    }

    @Override
    public Iterable<GameHistory> getGameHistoriesByTeamId(Long teamId, int limit) {
        return null;
    }

    @Override
    public GameHistory createGameHistory(GameHistory gameHistory) {
        return null;
    }

    @Override
    public GameHistory updateGameHistory(GameHistory gameHistory, Long id) {
        return null;
    }

    @Override
    public void deleteGameHistory(Long id) {

    }

    @Override
    public GameHistory stageGame() {
        return null;
    }

    @Override
    public GameHistory startGame(Long id) {
        return null;
    }

    @Override
    public GameHistory endGame(Long id, MatchDetailsDTO matchDetailsDTO) {
        return null;
    }
}
