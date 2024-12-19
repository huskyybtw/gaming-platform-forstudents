package pwr.isa.backend.Security.TokenSystem;


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

    @PostMapping(path = "/login")
    public Token getToken(@RequestBody LoginRequestDTO loginRequestDTO) {
        return tokenService.generateToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
    }

    @DeleteMapping(path = "/logout")
    public ResponseEntity<String> deleteToken(HttpServletRequest request) {
        String token = request.getHeader("auth");
        tokenService.deleteToken(token);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
