package pwr.isa.backend.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/users")
public class UserControler {

    private UserService userService;


    public UserControler(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(path= "/")
    public Iterable<User> readUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(path= "/{id}")
    public ResponseEntity<User> readUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.FOUND);
    }

    @PostMapping(path= "/")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @PutMapping(path= "/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody User user) {
        user.setID(id);
        return new ResponseEntity<>(userService.updateUser(user), HttpStatus.OK);
    }

    @PatchMapping(path= "/{id}")
    public ResponseEntity<User> patchUser(
            @PathVariable Long id,
            @RequestBody User user) {
        return new ResponseEntity<>(userService.patchUser(user), HttpStatus.OK);
    }

    @DeleteMapping(path= "/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
