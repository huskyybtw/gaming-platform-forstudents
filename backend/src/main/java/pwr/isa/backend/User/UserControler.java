package pwr.isa.backend.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.isa.backend.Security.AuthSystem.Authorize;
import pwr.isa.backend.Security.AuthSystem.AuthorizeAdminOnly;
import pwr.isa.backend.Security.TokenSystem.TokenService;

@RestController
@RequestMapping("api/v1/users")
@Tag(name = "User", description = "API for managing users")
public class UserControler {

    private UserService userService;
    private TokenService tokenService;

    public UserControler(UserService userService,TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Operation(summary = "Get all users", description = "Returns a list of all registered users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "Invalid request parameters"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(type = "You do not have permission to access"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @AuthorizeAdminOnly
    @GetMapping(path= "/")
    public Iterable<User> readUsers() {
        return userService.getAllUsers();
    }

    @Authorize
    @Operation(summary = "Get user by ID", description = "Returns data of a single user based on a  identifier ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type =  "Invalid user ID"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(type = "You do not have permission to access this user"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(path= "/{id}")
    public ResponseEntity<User> readUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @Operation(summary = "Create a new user", description = "Creates a new user with the provided data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "Email cannot be empty\nPassword cannot be empty\nUser with this email already exists\nInvalid email"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(type = "You do not have permission"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(type = "Failed to send email")))
    })
    @PostMapping(path= "/")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @AuthorizeAdminOnly
    @Operation(summary = "Create a new user with administrator role", description = "Creates a new user with administrator privileges.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "Email cannot be empty\nPassword cannot be empty\nUser with this email already exists\nInvalid email"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(type = "You do not have permission to create an admin user"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(path= "/admin")
    public ResponseEntity<User> createAdmin(@RequestBody User user) {
        return new ResponseEntity<>(userService.createAdmin(user), HttpStatus.CREATED);
    }

    @Authorize
    @Operation(summary = "Update an existing user", description = "Updates the data of an existing user identified by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "Email cannot be empty\nPassword cannot be empty\nUser with this email already exists\nInvalid email"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(type = "You do not have permission to update this user"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(path= "{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody User user) {
        return new ResponseEntity<>(userService.updateUser(user,id), HttpStatus.OK);
    }

    @Authorize
    @Operation(summary = "Partially update an existing user", description = "Updates selected fields of an existing user identified by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(type = "string")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping(path= "/{id}")
    public ResponseEntity<User> patchUser(
            @PathVariable Long id,
            @RequestBody User user) {
        return new ResponseEntity<>(userService.patchUser(user,id), HttpStatus.OK);
    }

    @Authorize
    @Operation(summary = "Delete a user", description = "Deletes the user identified by the ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted", content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "mail cannot be empty\nPassword cannot be empty\nUser with this email already exists\nInvalid email"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(type = "Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(type = "You do not have permission to update this user"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping(path= "/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
