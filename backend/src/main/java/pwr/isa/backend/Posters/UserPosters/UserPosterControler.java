package pwr.isa.backend.Posters.UserPosters;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.isa.backend.Security.AuthSystem.Authorize;
import pwr.isa.backend.Security.AuthSystem.AuthorizeEveryOne;

import java.util.List;

@RestController
@RequestMapping("api/v1/posters/user")
public class UserPosterControler {

    private final UserPosterService userPosterService;

    public UserPosterControler(UserPosterService userPosterService) {
        this.userPosterService = userPosterService;
    }

    @GetMapping(path= "/")
    public ResponseEntity<List<UserPoster>> readUserPosters(
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "created_at") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDirection) {
        return new ResponseEntity<>(userPosterService.getAllUserPosters(limit, offset, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping(path= "/{userId}")
    public UserPoster readUserPoster(@PathVariable Long userId) {
        return userPosterService.getUserPosterById(userId);
    }

    @AuthorizeEveryOne
    @PostMapping(path= "/")
    public ResponseEntity<UserPoster> createUserPoster(@RequestBody UserPoster userPoster) {
        return new ResponseEntity<>(userPosterService.createUserPoster(userPoster), HttpStatus.CREATED);
    }

    @Authorize
    @PatchMapping(path= "/{userId}")
    public ResponseEntity<UserPoster> updateUserPoster(
            @PathVariable Long userId,
            @RequestBody UserPoster userPoster) {
        return new ResponseEntity<>(userPosterService.updateUserPoster( userPoster, userId), HttpStatus.OK);
    }

    @Authorize
    @DeleteMapping(path= "/{userId}")
    public ResponseEntity<Void> deleteUserPoster(@PathVariable Long userId) {
        userPosterService.deleteUserPoster(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
