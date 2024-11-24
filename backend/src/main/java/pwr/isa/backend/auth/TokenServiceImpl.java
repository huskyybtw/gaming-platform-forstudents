package pwr.isa.backend.auth;

import org.springframework.stereotype.Service;
import pwr.isa.backend.Exceptions.UserNotAuthorizedException;
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
    public Token generateToken(User user) {
        User dbUser = userRepository.findByEmail(user.getEmail());
        if (dbUser == null) {
            throw new IllegalArgumentException("User with this email does not exist");
        }

        if(!dbUser.isEnabled()) {
            throw new IllegalArgumentException("User is not activated");
        }

        if(!dbUser.equals(user)) {
            throw new IllegalArgumentException("Credentials doesnt match");
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
    public boolean checkToken(User user, String token) {
        Token FoundToken = TOKENS.get(token);

        if(FoundToken == null) {
            throw new UserNotAuthorizedException("Token not found");
        }

        if(FoundToken.getAssignedUser() != user) {
            throw new UserNotAuthorizedException("Token is invalid");
        }

        if(!FoundToken.getDue().before(new Date())){
            TOKENS.remove(token);
            throw new UserNotAuthorizedException("Token is expired");
        }

        return true;
    }
}
