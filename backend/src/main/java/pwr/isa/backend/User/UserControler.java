package pwr.isa.backend.User;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/users")
public class UserControler {

    private UserService userService;


    public UserControler(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path= "/test")
    public String test1() {
        return "test";
    }

    @GetMapping(path= "/")
    public String readUsers() {
        return "all users";
    }

    @GetMapping(path= "/{id}")
    public String readUser() {
        return "user by id";
    }

    @PostMapping(path= "/")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping(path= "/{id}")
    public String updateUser() {
        return "update user";
    }

    @PatchMapping(path= "/{id}")
    public String patchUser() {
        return "patch user";
    }

    @DeleteMapping(path= "/{id}")
    public String deleteUser() {
        return "delete user";
    }
}
