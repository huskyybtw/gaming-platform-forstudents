package pwr.isa.backend.User;

import org.springframework.stereotype.Service;
import pwr.isa.backend.EmailValidator;

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

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

}
