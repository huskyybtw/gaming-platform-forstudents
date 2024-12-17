package pwr.isa.backend.Posters.UserPosters;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;

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
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(required = false) boolean sortByRating) {
        return userPosterService.getAllUserPosters(limit, offset, sortByRating);
    }

    @GetMapping(path= "/{userId}")
    public UserPoster readUserPoster(@PathVariable Long userId) {
        return userPosterService.getUserPosterById(userId);
    }

    @PostMapping(path= "/")
    public UserPoster createUserPoster(@RequestBody UserPoster userPoster) {
        return userPosterService.createUserPoster(userPoster);
    }

    @PatchMapping(path= "/{userId}")
    public UserPoster updateUserPoster(
            @PathVariable Long userId,
            @RequestBody UserPoster userPoster) {
        return userPosterService.updateUserPoster(userPoster, userId);
    }

    @DeleteMapping(path= "/{userId}")
    public void deleteUserPoster(@PathVariable Long userId) {
        userPosterService.deleteUserPoster(userId);
    }
}

