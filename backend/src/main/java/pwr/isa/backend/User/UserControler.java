package pwr.isa.backend.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.isa.backend.Security.AuthSystem.Authorize;
import pwr.isa.backend.Security.AuthSystem.AuthorizeAdminOnly;
import pwr.isa.backend.Security.TokenSystem.TokenService;

@RestController
@RequestMapping("api/v1/users")
@Tag(name = "Users", description = "API for managing users")
public class UserControler {

    private final UserService userService;
    private final TokenService tokenService;

    public UserControler(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Operation(summary = "Retrieve all users", description = "Get a list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list of users")
    })
    @GetMapping(path = "/")
    public Iterable<User> readUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Retrieve a user by ID", description = "Get a single user by their unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @Authorize
    @GetMapping(path = "/{id}")
    public ResponseEntity<User> readUser(
            @Parameter(description = "ID of the user to retrieve", required = true)
            @PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.FOUND);
    }

    @Operation(summary = "Create a new user", description = "Add a new user to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping(path = "/")
    public ResponseEntity<User> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User object to create", required = true)
            @RequestBody User user) {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @Operation(summary = "Create a new admin user", description = "Add a new admin user to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Admin user created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @SecurityRequirement(name = "bearerAuth")
    @AuthorizeAdminOnly
    @PostMapping(path = "/admin")
    public ResponseEntity<User> createAdmin(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Admin user object to create", required = true)
            @RequestBody User user) {
        return new ResponseEntity<>(userService.createAdmin(user), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a user", description = "Update the details of an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @Authorize
    @PutMapping(path = "/{id}")
    public ResponseEntity<User> updateUser(
            @Parameter(description = "ID of the user to update", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated user object", required = true)
            @RequestBody User user) {
        return new ResponseEntity<>(userService.updateUser(user, id), HttpStatus.OK);
    }

    @Operation(summary = "Partially update a user", description = "Update specific fields of an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User patched successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @Authorize
    @PatchMapping(path = "/{id}")
    public ResponseEntity<User> patchUser(
            @Parameter(description = "ID of the user to patch", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Partial user object with fields to update", required = true)
            @RequestBody User user) {
        return new ResponseEntity<>(userService.patchUser(user, id), HttpStatus.OK);
    }

    @Operation(summary = "Delete a user", description = "Remove a user from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @Authorize
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to delete", required = true)
            @PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
