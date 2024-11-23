package pwr.isa.backend.User;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import pwr.isa.backend.EmailValidator;

import java.lang.reflect.Field;
import java.util.Objects;
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
    public User updateUser(User user,Long id) {
        user.setID(id);
        if(!userRepository.existsById(user.getID())) {
            throw new EntityNotFoundException("User with ID " + user.getID() + " not found");
        }

        // CHECK IF EMAIL IS NOT ALREADY PRESENT IN DB
        User foundUser = userRepository.findByEmail(user.getEmail());
        if (foundUser != null && !Objects.equals(foundUser.getEmail(), user.getEmail())) {
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
    @Override
    public User patchUser(User user,Long id) {
        user.setID(id);
        Optional<User> target = userRepository.findById(user.getID());

        if(!target.isPresent()){
            throw new EntityNotFoundException("User with ID " + user.getID() + " not found");
        }

        for (Field field : User.class.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object sourceValue = field.get(user);

                if ("id".equals(field.getName())) {
                    continue;
                }

                if ("email".equals(field.getName())) {
                    if(!EmailValidator.isValid(user.getEmail())) {
                        throw new IllegalArgumentException("Invalid email");
                    }

                    User foundUser = userRepository.findByEmail(user.getEmail());
                    if (foundUser != null && Objects.equals(foundUser.getEmail(), user.getEmail())) {
                        throw new IllegalArgumentException("User with this email already exists");
                    }

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
