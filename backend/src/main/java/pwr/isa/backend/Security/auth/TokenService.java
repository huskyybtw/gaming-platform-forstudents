package pwr.isa.backend.Security.auth;
import pwr.isa.backend.entity.User;

public interface TokenService {
    Token generateToken(String email, String password);
    void deleteToken(String token);
    boolean validateToken(String token,Long id);
    User checkToken (String token);
}
