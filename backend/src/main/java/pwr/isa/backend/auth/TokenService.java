package pwr.isa.backend.auth;
import pwr.isa.backend.User.User;

public interface TokenService {
    Token generateToken(User user);
    boolean checkToken (User user, String token);
}
