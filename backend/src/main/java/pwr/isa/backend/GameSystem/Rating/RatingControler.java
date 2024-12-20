package pwr.isa.backend.GameSystem.Rating;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.isa.backend.Player.Player;
import pwr.isa.backend.Security.AuthSystem.AuthorizeAdminOnly;
import pwr.isa.backend.Team.Team;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("api/v1/rating")
@Tag(name = "Rating", description = "API for managing Player ladder")
public class RatingControler {

    private final RatingService ratingService;

    public RatingControler(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Operation(summary = "Get Player Rating", description = "Retrieve the current rating of a player by their user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "UnAuthorized", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content(schema = @Schema(type = "string"))),
    })
    @GetMapping("/player/{userId}")
    public ResponseEntity<Integer> getPlayerRating(@PathVariable Long userId) {
        Integer playerRating = ratingService.getPlayerRating(userId);
        return ResponseEntity.ok(playerRating);
    }

    @Operation(summary = "Get Best Players", description = "Retrieve a list of the top players with the highest ratings.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "UnAuthorized", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content(schema = @Schema(type = "string"))),
    })
    @GetMapping("/player/best")
    public ResponseEntity<List<Player>> getBestPlayers(
            @RequestParam(defaultValue = "100") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        List<Player> bestPlayers = ratingService.getBestPlayers(limit, offset);
        return ResponseEntity.ok(bestPlayers);
    }

    @Operation(summary = "Get Team Rating", description = "Retrieve the current rating of a team by its team ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "UnAuthorized", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content(schema = @Schema(type = "string"))),
    })
    @GetMapping("/team/{teamId}")
    public ResponseEntity<Integer> getTeamRating(@PathVariable Long teamId) {
        Integer teamRating = ratingService.getTeamRating(teamId);
        return ResponseEntity.ok(teamRating);
    }

    @Operation(summary = "Get Best Teams", description = "Retrieve a list of the top teams with the highest ratings.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "UnAuthorized", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content(schema = @Schema(type = "string"))),
    })
    @GetMapping("/team/best")
    public ResponseEntity<List<Team>> getBestTeams(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        List<Team> bestTeams = ratingService.getBestTeams(limit, offset);
        return ResponseEntity.ok(bestTeams);
    }

    @Operation(summary = "Update Team Rating", description = "Update the rating of a team by adjusting it based on a given difference.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "UnAuthorized", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content(schema = @Schema(type = "string"))),
    })
    @SecurityRequirement(name = "bearerAuth")
    @AuthorizeAdminOnly
    @PatchMapping("/team/{teamId}")
    public ResponseEntity<Team> updateTeamRating(
            @PathVariable Long teamId,
            @RequestParam Integer difference) {
        Team updatedTeam = ratingService.updateTeamRating(teamId, difference);
        return ResponseEntity.ok(updatedTeam);
    }

    @Operation(summary = "Update Player Rating", description = "Update the rating of a player by adjusting it based on a given difference.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "UnAuthorized", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content(schema = @Schema(type = "string"))),
    })
    @SecurityRequirement(name = "bearerAuth")
    @AuthorizeAdminOnly
    @PatchMapping("/player/{userId}")
    public ResponseEntity<Player> updatePlayerRating(
            @PathVariable Long userId,
            @RequestParam Integer difference) {
        Player updatedPlayer = ratingService.updatePlayerRating(userId, difference);
        return ResponseEntity.ok(updatedPlayer);
    }
}
