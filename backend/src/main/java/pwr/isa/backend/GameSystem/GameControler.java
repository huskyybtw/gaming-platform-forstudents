package pwr.isa.backend.GameSystem;

import org.springframework.web.bind.annotation.*;
import pwr.isa.backend.Consumer.DTO.MatchDetailsDTO;
import pwr.isa.backend.Consumer.RiotService;

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
    public GameHistoryDTO readGameHistory(@PathVariable Long matchId) {
        return gameHistoryService.getGameHistoryById(matchId);
    }

    @GetMapping("/")
    public Iterable<GameHistoryDTO> readGameHistories(
    ) {
        return gameHistoryService.getAllGameHistories();
    }

    @GetMapping("/userHistory/{userId}")
    public Iterable<GameHistoryDTO> readGameHistoriesByUserId(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "10") int limit) {
        return gameHistoryService.getGameHistoriesByUserId(userId, limit);
    }

    @GetMapping("/teamHistory/{teamId}")
    public Iterable<GameHistoryDTO> readGameHistoriesByTeamId(
            @PathVariable Long teamId,
            @RequestParam(required = false, defaultValue = "10") int limit) {
        return gameHistoryService.getGameHistoriesByTeamId(teamId, limit);
    }

    @PostMapping("/")
    public GameHistoryDTO createGameHistory(
            @RequestBody GameHistory gameHistory
    ) {
        return gameHistoryService.createGameHistory(gameHistory);
    }

    @PatchMapping("/{id}")
    public GameHistoryDTO updateGameHistory(
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
    public GameHistoryDTO startGame(
            @PathVariable Long id) {
        return gameHistoryService.startGame(id);
    }

    @PutMapping("/stage/{id}/{matchId}")
    public GameHistoryDTO endGame(
            @PathVariable Long id,
            @PathVariable String matchId) {

        GameHistoryDTO gameHistory = gameHistoryService.getGameHistoryById(id);
        MatchDetailsDTO matchDetailsDTO = riotService.getMatchDetailsDTO(matchId);
        return gameHistoryService.endGame(gameHistory.getGameHistory(),matchDetailsDTO);
    }
}
