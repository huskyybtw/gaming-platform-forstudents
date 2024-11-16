package pwr.isa.backend.User;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/users")
public class UserControler {

    private UserService userService;


    public UserControler(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(path= "/")
    public String readUsers() {
        return userService.getAllUsers().toString();
    }

    @GetMapping(path= "/{id}")
    public String readUser(@PathVariable Long id) {
        return userService.getUserById(id).toString();
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
