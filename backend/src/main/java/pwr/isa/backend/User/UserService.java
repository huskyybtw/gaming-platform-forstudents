package pwr.isa.backend.User;


public interface UserService {
    User createUser(User user);
    User getUserById(Long id);
    User updateUser(User user);
    User patchUser(User user);
    void deleteUser(Long id);
    Iterable<User> getAllUsers();
    boolean exsists(Long id);

}
