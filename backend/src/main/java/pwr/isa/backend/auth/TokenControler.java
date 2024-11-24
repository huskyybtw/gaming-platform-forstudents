package pwr.isa.backend.auth;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.isa.backend.User.User;

@RestController
@RequestMapping("api/v1/auth")
public class TokenControler {

    private TokenService tokenService;

    public TokenControler(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping(path = "/")
    public Token getToken(@RequestBody User user) {
        return tokenService.generateToken(user);
    }

    @GetMapping(path = "/")
    public ResponseEntity<Boolean> checkToken(@RequestBody User user, HttpServletRequest request) {
        return ResponseEntity.ok(tokenService.checkToken(user, request.getHeader("auth")));
    }
}
