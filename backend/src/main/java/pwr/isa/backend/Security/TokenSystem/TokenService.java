package pwr.isa.backend.Security.TokenSystem;
import pwr.isa.backend.User.User;

public interface TokenService {
    Token generateToken(String email, String password);
    void deleteToken(String token);
    boolean isAuthorized(String token, Long id);
    User getUserFromToken(String token);
    void validateToken(Token token);
    void deactivateTokens();
}
