package pwr.isa.backend.Posters.TeamPosters;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import pwr.isa.backend.Security.AuthSystem.Authorize;
import pwr.isa.backend.Security.AuthSystem.AuthorizeEveryOne;

@RestController
@RequestMapping("api/v1/posters/team")
@Tag(name = "Team Posters", description = "API for managing team posters")
public class TeamPosterControler {

    private final TeamPosterService teamPosterService;

    public TeamPosterControler(TeamPosterService teamPosterService) {
        this.teamPosterService = teamPosterService;
    }


    @Operation(summary = "Get all team posters", description = "Returns a list of all team posters. Supports pagination using limit and offset, and optional sorting by rating.")
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
    public Iterable<TeamPoster> readTeamPosters(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(required = false) boolean sortByRating) {
        return teamPosterService.getAllTeamPosters(limit, offset, sortByRating);
    }

    @Operation(summary = "Get a team poster by team ID", description = "Returns a single team poster identified by the team's ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "Invalid team ID format"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to access this resource"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @GetMapping(path= "/{teamId}")
    public TeamPoster readTeamPoster(@PathVariable Long teamId) {
        return teamPosterService.getTeamPosterById(teamId);
    }

    // TODO AUTORYZACJA MUSI BYC TEAM CAPITAN A NIE MAM JAK WZIAC ID NA TEN MOMENT


    @Operation(summary = "Create a team poster", description = "Creates a new team poster for the specified team.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "Team ID cannot be null\nDescription cannot be empty\nDueDate cannot be before CreatedAt\nTeamPoster not found for team with id: X"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to create a team poster"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @PostMapping(path= "/")
    public TeamPoster createTeamPoster(@RequestBody TeamPoster teamPoster) {
        return teamPosterService.createTeamPoster(teamPoster);
    }

    @Operation(summary = "Update a team poster", description = "Partially updates the team poster identified by the team's ID. Non-empty fields overwrite existing values.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "string",
                            example = "Team ID cannot be null\nDescription cannot be empty\nDueDate cannot be before CreatedAt\nTeam not found with id"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to update this team poster"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @Authorize
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping(path= "/{teamId}")
    public TeamPoster updateTeamPoster(
            @PathVariable Long teamId,
            @RequestBody TeamPoster teamPoster) {
        return teamPosterService.updateTeamPoster(teamPoster, teamId);
    }

    @Operation(summary = "Delete a team poster", description = "Deletes the team poster identified by the team's ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted", content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "Invalid request parameters"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to delete this team poster"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @Authorize
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping(path= "/{teamId}")
    public void deleteTeamPoster(@PathVariable Long teamId) {
        teamPosterService.deleteTeamPoster(teamId);
    }


}
