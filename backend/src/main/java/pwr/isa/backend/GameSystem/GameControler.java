package pwr.isa.backend.GameSystem;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.isa.backend.Consumer.DTO.MatchDetailsDTO;
import pwr.isa.backend.Consumer.RiotService;
import pwr.isa.backend.Security.AuthSystem.AuthorizeAdminOnly;

@RestController
@RequestMapping("api/v1/gameSystem")
@Tag(name = "Game", description = "API for managing Games and GamesHistory")
public class GameControler {
    private final GameService gameHistoryService;
    private final RiotService riotService;

    public GameControler(GameService gameHistoryService, RiotService riotService) {
        this.gameHistoryService = gameHistoryService;
        this.riotService = riotService;
    }

    @Operation(summary = "Get Game History", description = "Retrieve the game history for a specific match by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "UnAuthorized", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content(schema = @Schema(type = "string")))
    })
    @GetMapping("/{matchId}")
    public GameHistoryDTO readGameHistory(@PathVariable Long matchId) {
        return gameHistoryService.getGameHistoryById(matchId);
    }

    @Operation(summary = "Get All Game Histories", description = "Retrieve a list of all game histories.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "UnAuthorized", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content(schema = @Schema(type = "string")))
    })
    @GetMapping("/")
    public Iterable<GameHistoryDTO> readGameHistories(
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset) {
        return gameHistoryService.getAllGameHistories(limit, offset);
    }

    @Operation(summary = "Get User Game Histories", description = "Retrieve a list of game histories for a specific user by their user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "UnAuthorized", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content(schema = @Schema(type = "string")))
    })
    @GetMapping("/userHistory/{userId}")
    public Iterable<GameHistoryDTO> readGameHistoriesByUserId(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset) {
        return gameHistoryService.getGameHistoriesByUserId(userId, limit, offset);
    }

    @Operation(summary = "Get Team Game Histories", description = "Retrieve a list of game histories for a specific team by their team ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "UnAuthorized", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content(schema = @Schema(type = "string")))
    })
    @GetMapping("/teamHistory/{teamId}")
    public Iterable<GameHistoryDTO> readGameHistoriesByTeamId(
            @PathVariable Long teamId,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset) {
        return gameHistoryService.getGameHistoriesByTeamId(teamId, limit, offset);
    }

    @Operation(summary = "Create New Game History", description = "Create a new game history entry.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "UnAuthorized", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content(schema = @Schema(type = "string")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @AuthorizeAdminOnly
    @PostMapping("/")
    public ResponseEntity<GameHistoryDTO> createGameHistory(
            @RequestBody GameHistory gameHistory
    ) {
        return new ResponseEntity<>(gameHistoryService.createGameHistory(gameHistory), HttpStatus.CREATED);
    }

    @Operation(summary = "Update Game History", description = "Update the details of an existing game history entry.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "UnAuthorized", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content(schema = @Schema(type = "string")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @AuthorizeAdminOnly
    @PatchMapping("/{id}")
    public GameHistoryDTO updateGameHistory(
            @PathVariable Long id,
            @RequestBody GameHistory gameHistory
    ) {
        return gameHistoryService.updateGameHistory(gameHistory, id);
    }

    @Operation(summary = "Delete Game History", description = "Delete a game history entry by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted", content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "UnAuthorized", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content(schema = @Schema(type = "string")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @AuthorizeAdminOnly
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGameHistory(@PathVariable Long id) {
        gameHistoryService.deleteGameHistory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Start Game", description = "Start a game by updating the game history to reflect the started status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "UnAuthorized", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content(schema = @Schema(type = "string")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @AuthorizeAdminOnly
    @PatchMapping("/stage/{id}")
    public GameHistoryDTO startGame(
            @PathVariable Long id) {
        return gameHistoryService.startGame(id);
    }

    @Operation(summary = "End Game", description = "End a game by updating the game history and finalizing the match details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "UnAuthorized", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content(schema = @Schema(type = "string")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @AuthorizeAdminOnly
    @PutMapping("/stage/{id}/{matchId}")
    public GameHistoryDTO endGame(
            @PathVariable Long id,
            @PathVariable String matchId) {

        GameHistoryDTO gameHistory = gameHistoryService.getGameHistoryById(id);
        MatchDetailsDTO matchDetailsDTO = riotService.getMatchDetailsDTO(matchId);
        return gameHistoryService.endGame(gameHistory.getGameHistory(), matchDetailsDTO);
    }
}
