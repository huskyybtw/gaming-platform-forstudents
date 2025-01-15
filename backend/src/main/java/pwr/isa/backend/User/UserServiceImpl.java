package pwr.isa.backend.User;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pwr.isa.backend.Email.EmailService;
import pwr.isa.backend.Email.EmailValidator;
import pwr.isa.backend.Player.Player;
import pwr.isa.backend.Player.PlayerRepository;
import pwr.isa.backend.Player.PlayerService;
import pwr.isa.backend.Posters.UserPosters.UserPosterService;
import pwr.isa.backend.Security.SHA256;
import pwr.isa.backend.Team.Team;
import pwr.isa.backend.Team.TeamService;
import pwr.isa.backend.Team.TeamUsers.TeamUsersRepository;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/*
    TODO Usuwanie napenwno ma problemy np.usuniecie kapitana
 */

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserPosterService userPosterService;
    private final TeamUsersRepository teamUsersRepository;
    private final EmailService emailService;
    private final PlayerRepository playerRepository;

    public UserServiceImpl(UserRepository userRepository,
                           @Lazy UserPosterService userPosterService,
                           TeamUsersRepository teamUsersRepository,
                           EmailService emailService,
                           PlayerRepository playerRepository) {
        this.userRepository = userRepository;
        this.userPosterService = userPosterService;
        this.teamUsersRepository = teamUsersRepository;
        this.emailService = emailService;
        this.playerRepository = playerRepository;
    }

    @Override
    public User createUser(User user) {
        validateNewUser(user);

        user.setPassword(SHA256.hash(user.getPassword()));
        user.setID(null);
        user.setRole(UserRole.USER);

        var saved = userRepository.save(user);
        sendActivationEmail(user);

        // TEMPORARY SOLUTION
        playerRepository.save(Player.builder()
                .userId(saved.getID())
                .lastUpdate(new Timestamp(new Date().getTime()))
                        .rating(1000)
                .build());
        return user;
    }

    @Override
    public User createAdmin(User user) {
        validateNewUser(user);

        user.setPassword(SHA256.hash(user.getPassword()));
        user.setID(null);
        user.setEnabled(true);
        user.setRole(UserRole.ADMIN);

        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user, Long id) {
        user.setID(id);
        validateExistingUser(user);

        user.setPassword(SHA256.hash(user.getPassword()));

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        return userRepository.save(user);
    }

    @Override
    public User patchUser(User user, Long id) {
        user.setID(id);
        Optional<User> target = userRepository.findById(user.getID());

        if (target.isEmpty()) {
            throw new EntityNotFoundException("User with ID " + user.getID() + " not found");
        }

        for (Field field : User.class.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object sourceValue = field.get(user);

                if ("id".equals(field.getName())) {
                    continue;
                }

                if ("password".equals(field.getName())) {
                    if (sourceValue != null) {
                        field.set(target.get(), SHA256.hash((String) sourceValue));
                    }
                    continue;
                }

                if ("email".equals(field.getName())) {
                    validateEmail(user.getEmail());
                    field.set(target.get(), sourceValue);
                    continue;
                }

                if (sourceValue != null) {
                    field.set(target.get(), sourceValue);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            }
        }
        return userRepository.save(target.get());
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
        teamUsersRepository.findTeamsByUserId(id).forEach( (it) ->
                teamUsersRepository.deleteTeamUser(id, it));
        userPosterService.deleteUserPoster(id);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found"));
    }

    @Override
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean exists(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public void activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found"));
        user.setEnabled(true);
        userRepository.save(user);
    }

    // Validation and Utility Methods

    private void validateNewUser(User user) {
        if(user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        validateEmail(user.getEmail());
    }

    private void validateExistingUser(User user) {
        if (!userRepository.existsById(user.getID())) {
            throw new EntityNotFoundException("User with ID " + user.getID() + " not found");
        }

        User foundUser = userRepository.findByEmail(user.getEmail());
        if (foundUser != null && !Objects.equals(foundUser.getID(), user.getID())) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        validateEmail(user.getEmail());
    }

    private void validateEmail(String email) {
        if (!EmailValidator.isValid(email)) {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    private void sendActivationEmail(User user) {
        try {
            emailService.sendEmail(user.getEmail(), user.getID());
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
