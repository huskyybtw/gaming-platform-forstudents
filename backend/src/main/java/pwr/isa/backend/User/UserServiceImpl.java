package pwr.isa.backend.User;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import pwr.isa.backend.EmailValidator;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        if(userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("User with this email already exists");
        }
        
        if(!EmailValidator.isValid(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email");
        }

        return userRepository.save(user);
    }

    // ROOM FOR IMPROVEMENTS
    @Override
    public User updateUser(User user) {
        if(!userRepository.existsById(user.getID())) {
            throw new EntityNotFoundException("User with ID " + user.getID() + " not found");
        }

        // CHECK IF EMAIL IS NOT ALREADY PRESENT IN DB
        User foundUser = userRepository.findByEmail(user.getEmail());
        if (foundUser != null && foundUser.getEmail() != user.getEmail()) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        if(!EmailValidator.isValid(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        return userRepository.save(user);
    }

    // ROOM FOR IMPROVEMENTS
    public User patchUser(User user) {
        Optional<User> existing = userRepository.findById(user.getID());
        if (existing.isEmpty()) {
            throw new EntityNotFoundException("User with ID " + user.getID() + " not found");
        }

        if(Optional.ofNullable(user.getEmail()).isPresent()) {
            if(!EmailValidator.isValid(user.getEmail())) {
                throw new IllegalArgumentException("Invalid email");
            }

            User foundUser = userRepository.findByEmail(user.getEmail());
            if (foundUser != null && foundUser.getEmail() != user.getEmail()) {
                throw new IllegalArgumentException("User with this email already exists");
            }

            existing.get().setEmail(user.getEmail());
        }

        if(Optional.ofNullable(user.getPassword()).isPresent()) {
            if (user.getPassword().isBlank()) {
                throw new IllegalArgumentException("Password cannot be empty");
            }
            existing.get().setPassword(user.getPassword());
        }

        if(Optional.ofNullable(user.getRole()).isPresent()) {
            existing.get().setRole(user.getRole());
        }

        return userRepository.save(existing.get());
    }

    @Override
    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
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
    public boolean exsists(Long id) {
        return userRepository.existsById(id);
    }

}
