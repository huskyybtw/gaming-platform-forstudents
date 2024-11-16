package pwr.isa.backend.User;


public interface UserService {
    User createUser(User user);
    User getUserById(Long id);
    Iterable<User> getAllUsers();
}
