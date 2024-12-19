package pwr.isa.backend.Posters.UserPosters;

import org.springframework.web.bind.annotation.*;
import pwr.isa.backend.Security.AuthSystem.Authorize;
import pwr.isa.backend.Security.AuthSystem.AuthorizeEveryOne;

@RestController
@RequestMapping("api/v1/posters/user")
public class UserPosterControler {

    private final UserPosterService userPosterService;

    public UserPosterControler(UserPosterService userPosterService) {
        this.userPosterService = userPosterService;
    }

    @GetMapping(path= "/")
    public Iterable<UserPoster> readUserPosters(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        return userPosterService.getAllUserPosters(limit, offset);
    }

    @GetMapping(path= "/{userId}")
    public UserPoster readUserPoster(@PathVariable Long userId) {
        return userPosterService.getUserPosterById(userId);
    }

    @AuthorizeEveryOne
    @PostMapping(path= "/")
    public UserPoster createUserPoster(@RequestBody UserPoster userPoster) {
        return userPosterService.createUserPoster(userPoster);
    }

    @Authorize
    @PatchMapping(path= "/{userId}")
    public UserPoster updateUserPoster(
            @PathVariable Long userId,
            @RequestBody UserPoster userPoster) {
        return userPosterService.updateUserPoster(userPoster, userId);
    }

    @Authorize
    @DeleteMapping(path= "/{userId}")
    public void deleteUserPoster(@PathVariable Long userId) {
        userPosterService.deleteUserPoster(userId);
    }
}
