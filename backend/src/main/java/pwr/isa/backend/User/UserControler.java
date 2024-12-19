package pwr.isa.backend.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.isa.backend.Security.AuthSystem.Authorize;
import pwr.isa.backend.Security.AuthSystem.AuthorizeAdminOnly;
import pwr.isa.backend.Security.TokenSystem.TokenService;


@RestController
@RequestMapping("api/v1/users")
public class UserControler {

    private UserService userService;
    private TokenService tokenService;

    public UserControler(UserService userService,TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @GetMapping(path= "/")
    public Iterable<User> readUsers() {
        return userService.getAllUsers();
    }

    @Authorize
    @GetMapping(path= "/{id}")
    public ResponseEntity<User> readUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.FOUND);
    }

    @PostMapping(path= "/")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @AuthorizeAdminOnly
    @PostMapping(path= "/admin")
    public ResponseEntity<User> createAdmin(@RequestBody User user) {
        return new ResponseEntity<>(userService.createAdmin(user), HttpStatus.CREATED);
    }

    @Authorize
    @PutMapping(path= "{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody User user) {
        return new ResponseEntity<>(userService.updateUser(user,id), HttpStatus.OK);
    }

    @Authorize
    @PatchMapping(path= "/{id}")
    public ResponseEntity<User> patchUser(
            @PathVariable Long id,
            @RequestBody User user) {
        return new ResponseEntity<>(userService.patchUser(user,id), HttpStatus.OK);
    }

    @Authorize
    @DeleteMapping(path= "/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
