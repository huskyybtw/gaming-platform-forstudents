package pwr.isa.backend.Security.auth;

import org.springframework.stereotype.Service;
import pwr.isa.backend.Exceptions.NotAuthorizedException;
import pwr.isa.backend.User.User;
import pwr.isa.backend.User.UserRepository;

import java.util.Date;
import java.util.HashMap;

@Service
public class TokenServiceImpl implements TokenService {
    private UserRepository userRepository;
    private final HashMap<String,Token> TOKENS;

    public TokenServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.TOKENS = new HashMap<>();
    }

    @Override
    public Token generateToken(String email, String password) {
        User dbUser = userRepository.findByEmail(email);
        if (dbUser == null) {
            throw new IllegalArgumentException("User with this email does not exist");
        }

        if(!dbUser.isEnabled()) {
            throw new IllegalArgumentException("User is not activated");
        }

        if(!dbUser.getPassword().equals(password) && !dbUser.getEmail().equals(email)) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        Token token = Token.builder()
                .assignedUser(dbUser)
                .token("test")
                .createdAt(new Date())
                .Due(new Date(System.currentTimeMillis() + 3600000))
                .build();

        TOKENS.put(token.getToken(),token);
        return token;
    }

    @Override
    public User checkToken(String token) {
        Token FoundToken = TOKENS.get(token);

        if(FoundToken == null) {
            throw new NotAuthorizedException("Token not found");
        }

        if(FoundToken.getAssignedUser() == null) {
            throw new NotAuthorizedException("Token not assigned to any user");
        }

        if(FoundToken.getDue().before(new Date())){
            TOKENS.remove(token);
            throw new NotAuthorizedException("Token is expired");
        }

        return FoundToken.getAssignedUser();
    }

    @Override
    public void deleteToken(String token) {
        TOKENS.remove(token);
    }

    @Override
    public boolean validateToken(String token, Long id) {
        Token FoundToken = TOKENS.get(token);

        if(FoundToken == null) {
            throw new NotAuthorizedException("Token not found");
        }

        if(FoundToken.getAssignedUser() == null) {
            throw new NotAuthorizedException("Token not assigned to any user");
        }

        if(FoundToken.getDue().before(new Date())){
            TOKENS.remove(token);
            throw new NotAuthorizedException("Token is expired");
        }

        if(!FoundToken.getAssignedUser().getID().equals(id)) {
            throw new NotAuthorizedException("Token is not assigned to this user");
        }

        return true;
    }
}
