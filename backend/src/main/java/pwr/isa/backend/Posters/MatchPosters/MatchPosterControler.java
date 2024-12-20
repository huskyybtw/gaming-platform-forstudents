package pwr.isa.backend.Posters.MatchPosters;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import java.util.List;

@RestController
@RequestMapping("api/v1/posters/match")
@Tag(name = "Match Posters", description = "API for scheduling and managing matches")
public class MatchPosterControler {

    private final MatchPosterService matchPosterService;

    public MatchPosterControler(MatchPosterService matchPosterService) {
        this.matchPosterService = matchPosterService;
    }

    @Operation(summary = "Get all match posters", description = "Returns a list of all match posters with optional pagination.")
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
    public List<MatchPosterDTO> getAllMatchPosters(
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "created_at") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDirection) {
        return matchPosterService.getAllMatchPosters(limit, offset, sortBy, sortDirection);
    }

    @Operation(summary = "Get a match poster by ID", description = "Returns a single match poster identified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "Invalid poster ID format"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to access this match poster"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @GetMapping("/{posterId}")
    public MatchPosterDTO getMatchPosterById(@PathVariable Long posterId) {
        return matchPosterService.getMatchPosterById(posterId);
    }

    @AuthorizeEveryOne
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create a match poster", description = "Creates a new match poster. If a teamId is provided, the entire team joins the match poster.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "string",
                            example = "Owner does not exist\nInvalid request parameters"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to create a match poster"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @PostMapping("/")
    public ResponseEntity<MatchPosterDTO> createMatchPoster(
            @RequestBody MatchPoster matchPoster,
            @RequestParam(required = false) Long teamId) {
        return new ResponseEntity<>( matchPosterService.createMatchPoster(matchPoster, teamId), HttpStatus.CREATED);
    }




    @Operation(summary = "Start a match", description = "Starts the match identified by the match poster ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "Invalid match poster ID format"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to start this match"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @Authorize
    @PostMapping("/start/{posterId}")
    public ResponseEntity<MatchPosterDTO> startMatch(@PathVariable Long posterId) {
        return new ResponseEntity<>( matchPosterService.startMatch(posterId),HttpStatus.CREATED);
    }


    @Operation(summary = "Join a match poster as a player", description = "Allows a user to join a match poster with an optional team number (defaults to 100).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "string",
                            example = "Match poster not found\nUser already joined the match\nUser does not exist\nTeam is full"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to join this match poster"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @Authorize
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{posterId}/join/{userId}")
    public MatchPosterDTO joinMatchPoster(
            @PathVariable Long posterId,
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "100") int team) {
        return matchPosterService.joinMatchPoster(posterId, userId, team);
    }

    // TODO - AUTORYZACJA DLA TYCH TRZECH METOD


    @Operation(summary = "Leave a match poster", description = "Removes the user from the match poster participants.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "Match poster not found\nInvalid user or poster ID"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to leave this match"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @Authorize
    @PostMapping("/{posterId}/leave/{userId}")
    public MatchPosterDTO leaveMatchPoster(
            @PathVariable Long posterId,
            @PathVariable Long userId) {
        return matchPosterService.leaveMatchPoster(posterId, userId);
    }


    @Operation(summary = "Join a match poster as a team", description = "Allows a full team (5 members) to join the match poster.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "Match poster not found\nInvalid Team\nTeams are already full"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to join this match as a team"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @Authorize
    @PostMapping("/{posterId}/joinTeam/{teamId}")
    public MatchPosterDTO joinTeam(
            @PathVariable Long posterId,
            @PathVariable Long teamId) {
        return matchPosterService.joinAsTeam(posterId, teamId);
    }




    @Operation(summary = "Update a match poster", description = "Updates the data of an existing match poster.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "Match poster not found\nOwner needs to exist"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to update this match poster"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @Authorize
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{posterId}")
    public MatchPosterDTO updateMatchPoster(
            @PathVariable Long posterId,
            @RequestBody MatchPoster matchPoster) {
        return matchPosterService.updateMatchPoster(posterId, matchPoster);
    }



    @Operation(summary = "Delete a match poster", description = "Deletes the match poster identified by the ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted", content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "Invalid request parameters"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to delete this match poster"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @Authorize
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{posterId}")
    public ResponseEntity<Void> deleteMatchPoster(@PathVariable Long posterId) {
        matchPosterService.deleteMatchPoster(posterId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
