package pwr.isa.backend.Security.TokenSystem;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class TokenControler {

    private TokenService tokenService;

    public TokenControler(TokenService tokenService) {
        this.tokenService = tokenService;
    }


    @Operation(summary = "Generate a token", description = "Generates a token for a user based on provided credentials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "User with this email does not exist\nUser is not activated\nInvalid credentials"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type ="Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "You do not have permission to access this resource"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "An unexpected error occurred on the server")))
    })
    @PostMapping(path = "/login")
    public Token getToken(@RequestBody LoginRequestDTO loginRequestDTO) {
        return tokenService.generateToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
    }

    @Operation(summary = "Delete a token", description = "Deletes (invalidates) the token from the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted", content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(type = "string", example="Invalid request parameters"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(type = "string", example="Invalid or missing authentication token"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(type = "string", example="You do not have permission to perform this action"))),
            @ApiResponse(responseCode = "500", description = "InternalServerError",
                    content = @Content(schema = @Schema(type = "string", example="An unexpected error occurred on the server")))
    })
    @DeleteMapping(path = "/logout")
    public ResponseEntity<String> deleteToken(HttpServletRequest request) {
        String token = request.getHeader("auth");
        tokenService.deleteToken(token);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
