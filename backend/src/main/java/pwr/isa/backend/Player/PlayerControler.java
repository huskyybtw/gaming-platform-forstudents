package pwr.isa.backend.Player;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.isa.backend.RIOT.DTO.LeagueDTO;

import java.util.List;

@RestController
@RequestMapping("api/v1/players")
@Tag(name = "Player", description = "API for managing players")
public class PlayerControler {

    private final PlayerService playerService;

    public PlayerControler(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Operation(summary = "Get all players", description = "Retrieve a list of all players with optional pagination and sorting by rating.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of players"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(path= "/")
    public Iterable<Player> readPlayers(
            @Parameter(description = "Number of records to return", example = "100")
            @RequestParam(defaultValue = "100") int limit,
            @Parameter(description = "Number of records to skip", example = "0")
            @RequestParam(defaultValue = "0") int offset,
            @Parameter(description = "Sort players by rating", example = "false")
            @RequestParam(required = false,defaultValue = "false") boolean sortByRating
    ) {
        return playerService.getAllPlayers(limit, offset, sortByRating);
    }

    @Operation(summary = "Get player by User ID", description = "Retrieve a player's details using their User ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Player found"),
            @ApiResponse(responseCode = "404", description = "Player not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(path= "/{userId}")
    public ResponseEntity<Player> readPlayer(
            @Parameter(description = "User ID of the player to retrieve", required = true, example = "1")
            @PathVariable Long userId) {
        return new ResponseEntity<>(playerService.getPlayerById(userId), HttpStatus.FOUND);
    }

    @Operation(summary = "Refresh player details", description = "Refresh and retrieve updated player details from Riot API.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player details successfully refreshed"),
            @ApiResponse(responseCode = "404", description = "Player not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(path= "/details/{userId}")
    public ResponseEntity<Player> refreshPlayer(
            @Parameter(description = "User ID of the player to refresh", required = true, example = "1")
            @PathVariable Long userId) {
        return new ResponseEntity<>(playerService.refreshPlayer(userId), HttpStatus.OK);
    }

    @Operation(summary = "Get player rank", description = "Retrieve the rank details of a player from Riot API.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved player rank"),
            @ApiResponse(responseCode = "404", description = "Player not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(path= "/riot/rank/{userId}")
    public ResponseEntity<List<LeagueDTO>> getPlayerRank(
            @Parameter(description = "User ID of the player to get rank for", required = true, example = "1")
            @PathVariable Long userId) {
        return new ResponseEntity<>(playerService.getPlayerRank(userId), HttpStatus.OK);
    }


    @PostMapping(path= "/")
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        return new ResponseEntity<>(playerService.createPlayer(player), HttpStatus.CREATED);
    }

    @PutMapping(path= "/{userId}")
    public ResponseEntity<Player> updatePlayer(
            @PathVariable Long userId,
            @RequestBody Player player) {
        return new ResponseEntity<>(playerService.updatePlayer(player, userId), HttpStatus.OK);
    }

    @PatchMapping(path= "/{userId}")
    public ResponseEntity<Player> patchPlayer(
            @PathVariable Long userId,
            @RequestBody Player player) {
        return new ResponseEntity<>(playerService.patchPlayer(player, userId), HttpStatus.OK);
    }

    @DeleteMapping(path= "/{userId}")
    public ResponseEntity<Player> deletePlayer(@PathVariable Long userId) {
        playerService.deletePlayer(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
