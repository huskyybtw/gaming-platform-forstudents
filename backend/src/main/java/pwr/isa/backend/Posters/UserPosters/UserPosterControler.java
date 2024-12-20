package pwr.isa.backend.Posters.UserPosters;

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
@RequestMapping("api/v1/posters/user")
@Tag(name = "User Posters", description = "API for managing user posters")
public class UserPosterControler {

    private final UserPosterService userPosterService;

    public UserPosterControler(UserPosterService userPosterService) {
        this.userPosterService = userPosterService;
    }


    @Operation(summary = "Get all user posters", description = "Returns a list of all user posters. Supports pagination using limit and offset.")
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
    public ResponseEntity<List<UserPoster>> readUserPosters(
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "created_at") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDirection) {
        return new ResponseEntity<>(userPosterService.getAllUserPosters(limit, offset, sortBy, sortDirection), HttpStatus.OK);
    }


    @Operation(summary = "Get a user poster by user ID", description = "Returns a single user poster identified by the user's ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "Invalid user ID format"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to access this resource"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @GetMapping(path= "/{userId}")
    public UserPoster readUserPoster(@PathVariable Long userId) {
        return userPosterService.getUserPosterById(userId);
    }



    @Operation(summary = "Create a user poster", description = "Creates a new user poster with the provided data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "User ID cannot be null\nDescription cannot be empty\nCreatedAt date cannot be null\nDueDate cannot be before CreatedAt\nUser not found with id: X\nDescription cannot exceed 500 characters\nDueDate cannot be in the past"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to create a user poster"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "sAn unexpected error occurred on the server")))
    })
    @AuthorizeEveryOne
    @PostMapping(path= "/")
    public ResponseEntity<UserPoster> createUserPoster(@RequestBody UserPoster userPoster) {
        return new ResponseEntity<>(userPosterService.createUserPoster(userPoster), HttpStatus.CREATED);
    }


    @Operation(summary = "Update a user poster", description = "Partially updates the user poster identified by the user's ID. Non-empty fields overwrite existing values.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "User ID cannot be null\nDescription cannot be empty\nCreatedAt date cannot be null\nDueDate cannot be before CreatedAt\nUser not found with id: X\nDescription cannot exceed 500 characters\nDueDate cannot be in the past"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to update this user poster"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @Authorize
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping(path= "/{userId}")
    public ResponseEntity<UserPoster> updateUserPoster(
            @PathVariable Long userId,
            @RequestBody UserPoster userPoster) {
        return new ResponseEntity<>(userPosterService.updateUserPoster( userPoster, userId), HttpStatus.OK);
    }



    @Operation(summary = "Delete a user poster", description = "Deletes the user poster identified by the user's ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted", content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "Invalid request parameters"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "nvalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to delete this user poster"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @Authorize
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping(path= "/{userId}")
    public ResponseEntity<Void> deleteUserPoster(@PathVariable Long userId) {
        userPosterService.deleteUserPoster(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
