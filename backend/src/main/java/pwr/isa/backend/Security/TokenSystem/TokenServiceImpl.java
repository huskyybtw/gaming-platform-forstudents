package pwr.isa.backend.Security.TokenSystem;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pwr.isa.backend.Email.EmailService;
import pwr.isa.backend.Exceptions.NotAuthenticatedException;
import pwr.isa.backend.Exceptions.NotAuthorizedException;
import pwr.isa.backend.Security.SHA256;
import pwr.isa.backend.User.User;
import pwr.isa.backend.User.UserRepository;

import java.util.Date;
import java.util.HashMap;

@Service
public class TokenServiceImpl implements TokenService {
    private UserRepository userRepository;
    private final EmailService emailService;
    private final HashMap<String,Token> TOKENS;
    private final Long TOKEN_EXPIRATION_TIME = 3600000L;

    public TokenServiceImpl(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.TOKENS = new HashMap<>();
    }

    @Override
    public Token generateToken(String email, String password) {
        password = SHA256.hash(password);
        User dbUser = userRepository.findByEmail(email);
        if (dbUser == null) {
            throw new NotAuthenticatedException("User with this email does not exist");
        }

        if(!dbUser.isEnabled()) {
            try {
                emailService.sendEmail(dbUser.getEmail(), dbUser.getID());
            }
            catch (Exception e) {
                throw new NotAuthenticatedException("User is not activated");
            }

            throw new NotAuthenticatedException("User is not activated");
        }

        if(!dbUser.getPassword().equals(password) && !dbUser.getEmail().equals(email)) {
            throw new NotAuthenticatedException("Invalid credentials");
        }

        Token token = Token.builder()
                .assignedUser(dbUser)
                .token("test")
                .createdAt(new Date())
                .Due(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .build();

        TOKENS.put(token.getToken(),token);
        return token;
    }

    @Override
    public User getUserFromToken(String token) {
        Token FoundToken = TOKENS.get(token);

        validateToken(FoundToken);

        return FoundToken.getAssignedUser();
    }

    @Override
    public boolean isAuthorized(String token, Long id) {
        Token FoundToken = TOKENS.get(token);

        validateToken(FoundToken);

        if(!FoundToken.getAssignedUser().getID().equals(id)) {
            throw new NotAuthorizedException("Token is not assigned to this user");
        }

        return true;
    }

    @Override
    public void validateToken(Token token) {

        if(token == null) {
            throw new NotAuthorizedException("Token not found");
        }

        if(token.getAssignedUser() == null) {
            throw new NotAuthorizedException("Token not assigned to any user");
        }

        if(token.getDue().before(new Date())){
            TOKENS.remove(token.getToken());
            throw new NotAuthorizedException("Token is expired");
        }
    }

    @Override
    @Scheduled(fixedRate = 36000)
    public void deactivateTokens() {
        for (Token token : TOKENS.values()) {

            if(token.getDue().before(new Date())){
                TOKENS.remove(token.getToken());
            }

        }
    }

    @Override
    public void deleteToken(String token) {
        TOKENS.remove(token);
    }
}
