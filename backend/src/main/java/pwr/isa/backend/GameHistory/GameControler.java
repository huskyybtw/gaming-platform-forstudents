package pwr.isa.backend.GameHistory;

import org.springframework.web.bind.annotation.*;
import pwr.isa.backend.RIOT.DTO.MatchDetailsDTO;
import pwr.isa.backend.RIOT.RiotService;

@RestController
@RequestMapping("api/v1/gameHistory")
public class GameControler {
    private final GameService gameHistoryService;
    private final RiotService riotService;

    public GameControler(GameService gameHistoryService, RiotService riotService) {
        this.gameHistoryService = gameHistoryService;
        this.riotService = riotService;
    }

    @GetMapping("/{matchId}")
    public GameHistory readGameHistory(@PathVariable Long matchId) {
        return gameHistoryService.getGameHistoryById(matchId);
    }

    @GetMapping("/")
    public Iterable<GameHistory> readGameHistories(
    ) {
        return gameHistoryService.getAllGameHistories();
    }

    @GetMapping("/userHistory/{userId}")
    public Iterable<GameHistory> readGameHistoriesByUserId(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "10") int limit) {
        return gameHistoryService.getGameHistoriesByUserId(userId, limit);
    }

    @GetMapping("/teamHistory/{teamId}")
    public Iterable<GameHistory> readGameHistoriesByTeamId(
            @PathVariable Long teamId,
            @RequestParam(required = false, defaultValue = "10") int limit) {
        return gameHistoryService.getGameHistoriesByTeamId(teamId, limit);
    }

    @PostMapping("/")
    public GameHistory createGameHistory(
            @RequestBody GameHistory gameHistory
    ) {
        return gameHistoryService.createGameHistory(gameHistory);
    }

    @PatchMapping("/{id}")
    public GameHistory updateGameHistory(
            @PathVariable Long id,
            @RequestBody GameHistory gameHistory
    ) {
        return gameHistoryService.updateGameHistory(gameHistory, id);
    }

    @DeleteMapping("/{id}")
    public void deleteGameHistory(@PathVariable Long id) {
        gameHistoryService.deleteGameHistory(id);
    }


    @PatchMapping("/stage/{id}")
    public GameHistory startGame(
            @PathVariable Long id) {
        return gameHistoryService.startGame(id);
    }

    @PutMapping("/stage/{id}")
    public GameHistory endGame(@PathVariable Long id) {
        String matchId = "test";
        MatchDetailsDTO matchDetailsDTO = riotService.getMatchDetailsDTO(matchId);
        return gameHistoryService.endGame(id,matchDetailsDTO);
    }
}
