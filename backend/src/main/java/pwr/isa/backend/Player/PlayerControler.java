package pwr.isa.backend.Player;

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
import pwr.isa.backend.Consumer.DTO.LeagueDTO;
import pwr.isa.backend.Security.AuthSystem.Authorize;
import pwr.isa.backend.Security.AuthSystem.AuthorizeEveryOne;

import java.util.List;

@RestController
@RequestMapping("api/v1/players")
@Tag(name = "Player", description = "API for managing RIOT Games Profiles")
public class PlayerControler {

    private final PlayerService playerService;

    public PlayerControler(PlayerService playerService) {
        this.playerService = playerService;
    }


    @Operation(summary = "Get all players", description = "Returns a list of players with optional pagination and sorting by rating.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "Invalid request parameters"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to access this resource"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @GetMapping(path= "/")
    public List<Player> readPlayers(
            @RequestParam(required = false, defaultValue = "100") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "rating") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDirection
    ) {
        return playerService.getAllPlayers(limit, offset, sortBy, sortDirection);
    }


    @Operation(summary = "Get player by user ID", description = "Returns a player identified by the user's ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "Invalid user ID format"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to access this player"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @GetMapping(path= "/{userId}")
    public ResponseEntity<Player> readPlayer(@PathVariable Long userId) {
        return new ResponseEntity<>(playerService.getPlayerById(userId), HttpStatus.OK);
    }


    @Operation(summary = "Refresh player data", description = "Refreshes player data from external services by user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "nvalid user ID format"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to refresh this player"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @GetMapping(path= "/details/{userId}")
    public ResponseEntity<Player> refreshPlayer(@PathVariable Long userId) {
        return new ResponseEntity<>(playerService.refreshPlayer(userId), HttpStatus.OK);
    }


    @Operation(summary = "Refresh player data", description = "Refreshes player data from external services by user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "Invalid user ID format"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to refresh this player"))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(schema = @Schema(type = "Player with ID X not found"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @GetMapping(path= "/rank/{userId}")
    public ResponseEntity<List<LeagueDTO>> getPlayerRank(@PathVariable Long userId) {
        return new ResponseEntity<>(playerService.getPlayerRank(userId), HttpStatus.OK);
    }


    @Operation(summary = "Create a new player", description = "Creates a new player with the provided data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "User id is not valid or already assigned to another player\nNickname cannot be null or empty\nPlayer with this nickname already exists\nTag line cannot be null or empty\nTag line must be between 3 and 5 characters long\nTag line must contain only alphanumeric characters\nPlayer with this nickname already exists"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to create a player"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @AuthorizeEveryOne
    @PostMapping(path= "/")
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        return new ResponseEntity<>(playerService.createPlayer(player), HttpStatus.CREATED);
    }



    @Operation(summary = "Update a player", description = "Updates the data of an existing player identified by the user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "string",
                            example = "Player with user ID  not found\nUser cannot be changed\nNickname cannot be null or empty\nPlayer with this nickname already exists\nTag line cannot be null or empty\nTag line must be between 3 and 5 characters long\nTag line must contain only alphanumeric characters\nPlayer with this nickname already exists"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to update this player"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @Authorize
    @PutMapping(path= "/{userId}")
    public ResponseEntity<Player> updatePlayer(
            @PathVariable Long userId,
            @RequestBody Player player) {
        return new ResponseEntity<>(playerService.updatePlayer(player, userId), HttpStatus.OK);
    }


    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Partially update a player", description = "Partially updates selected fields of an existing player identified by the user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "string",
                            example = "Player with user ID X not found\nNickname cannot be null or empty\nPlayer with this nickname already exists\nTag line cannot be null or empty\nTag line must be between 3 and 5 characters long\nTag line must contain only alphanumeric characters\nPlayer with this nickname already exists"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to update this player"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @Authorize
    @PatchMapping(path= "/{userId}")
    public ResponseEntity<Player> patchPlayer(
            @PathVariable Long userId,
            @RequestBody Player player) {
        return new ResponseEntity<>(playerService.patchPlayer(player, userId), HttpStatus.OK);
    }



    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete a player", description = "Deletes the player identified by the user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted", content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "Invalid request parameters"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to delete this player"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @Authorize
    @DeleteMapping(path= "/{userId}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long userId) {
        playerService.deletePlayer(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
