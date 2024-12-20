package pwr.isa.backend.Team;

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
import pwr.isa.backend.Security.AuthSystem.Authorize;
import pwr.isa.backend.Security.AuthSystem.AuthorizeEveryOne;

import java.util.List;

@RestController
@RequestMapping("api/v1/teams")
@Tag(name = "Teams", description = "API for managing Teams")
public class TeamControler {

    private final TeamService teamService;

    public TeamControler(TeamService teamService) {
        this.teamService = teamService;
    }


    @Operation(summary = "Get all teams", description = "Returns a list of all registered teams in the system.")
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
    @GetMapping("/")
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        List<TeamDTO> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    @Operation(summary = "Get a team by ID", description = "Returns data of a single team based on a identifier.")
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
    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Long id) {
        TeamDTO team = teamService.getTeamById(id);
        return ResponseEntity.ok(team);
    }



    @Operation(summary = "Create a team", description = "Creates a new team with the provided data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "Team name cannot be null or empty\nA team with this name already exists\nTeam captain cannot be null\nUser with this id does not exist"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type ="Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to create a team"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @AuthorizeEveryOne
    @PostMapping("/")
    public ResponseEntity<TeamDTO> createTeam(@RequestBody Team team) {
        TeamDTO createdTeam = teamService.createTeam(team);
        return new ResponseEntity<>(createdTeam, HttpStatus.CREATED);
    }


    @Operation(summary = "Add a player to a team", description = "Adds a user to a team by their IDs.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "Team name cannot be null or empty\nA team with this name already exists\nTeam captain cannot be null\nUser with this id does not exist"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type ="Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to create a team"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @Authorize
    @PostMapping("/manage/{userId}/{teamId}")
    public ResponseEntity<TeamDTO> addPlayerToTeam(
            @PathVariable Long teamId,
            @PathVariable Long userId) {
        TeamDTO team = teamService.addPlayerToTeam(teamId, userId);
        return ResponseEntity.ok(team);
    }

    // TODO Do zastanowienia jak zrobic autoryzajce bo jak narazie tylko capitan moze usuwac


    @Operation(summary = "Remove a player from a team", description = "Removes a user from a team by their IDs.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted", content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "User with this id is not in the team"))),
            @ApiResponse(responseCode = "401", description = "UnAuthorized", content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(type = "You do not have permission to modify this team"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError", content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @Authorize
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/manage/{userId}/{teamId}")
    public ResponseEntity<TeamDTO> removePlayerFromTeam(
            @PathVariable Long teamId,
            @PathVariable Long userId) {
        TeamDTO team = teamService.removePlayerFromTeam(teamId, userId);
        return ResponseEntity.ok(team);
    }

    @Operation(summary = "Update a team", description = "Updates the team's information by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "Team name cannot be null or empty\nA team with this name already exists\nTeam captain cannot be null\nUser with id: X does not exist"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to update this team"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @Authorize
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<TeamDTO> updateTeam(
            @PathVariable Long id,
            @RequestBody Team updatedTeam) {
        TeamDTO team = teamService.updateTeam(id, updatedTeam);
        return ResponseEntity.ok(team); // 200
    }



    @Operation(summary = "Delete a team", description = "Deletes a team identified by the unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted", content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "string", example="Invalid request parameters"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "string", example="Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "string", example="You do not have permission to delete this team"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "string", example="An unexpected error occurred on the server")))
    })
    @Authorize
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}
