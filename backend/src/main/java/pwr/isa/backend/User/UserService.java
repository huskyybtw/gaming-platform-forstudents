package pwr.isa.backend.User;


public interface UserService {
    User createUser(User user);
    User createAdmin(User user);
    User getUserById(Long id);
    User updateUser(User user,Long id);
    User patchUser(User user,Long id);
    void deleteUser(Long id);
    Iterable<User> getAllUsers();
    boolean exists(Long id);
    User activateUser(Long id);

}
