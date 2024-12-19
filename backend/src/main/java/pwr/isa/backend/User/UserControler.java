package pwr.isa.backend.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.isa.backend.Security.SHA256;
import pwr.isa.backend.Security.auth.TokenService;


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

    @Operation(summary = "Get all users", description = "Retrieve a list of all registered users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(path= "/")
    public Iterable<User> readUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a user by their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(path= "/{id}")
    public ResponseEntity<User> readUser(
            @Parameter(description = "ID of the user to retrieve", required = true)
            @PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.FOUND);
    }

    @Operation(summary = "Create a new user", description = "Register a new user in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(path= "/")
    public ResponseEntity<User> createUser(
            @Parameter(description = "User object to be created", required = true)
            @RequestBody User user) {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing user", description = "Fully update an existing user's information by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(path= "{id}")
    public ResponseEntity<User> updateUser(
            @Parameter(description = "ID of the user to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated user object", required = true)
            @RequestBody User user) {
        return new ResponseEntity<>(userService.updateUser(user,id), HttpStatus.OK);
    }

    @Operation(summary = "Partially update a user", description = "Update specific fields of an existing user by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping(path= "/{id}")
    public ResponseEntity<User> patchUser(
            @Parameter(description = "ID of the user to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "User object with fields to update", required = true)
            @RequestBody User user) {
        return new ResponseEntity<>(userService.patchUser(user,id), HttpStatus.OK);
    }
    @Operation(summary = "Delete a user", description = "Remove a user from the system by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User successfully deleted"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping(path= "/{id}")
    public ResponseEntity<User> deleteUser(
            @Parameter(description = "ID of the user to delete", required = true)
            @PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
