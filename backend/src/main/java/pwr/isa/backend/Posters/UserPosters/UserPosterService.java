package pwr.isa.backend.Posters.UserPosters;

import org.springframework.stereotype.Service;

public interface UserPosterService {
    Iterable<UserPoster> getAllUserPosters(int limit, int offset, boolean sortByRating);

    UserPoster getUserPosterById(Long userId);

    UserPoster createUserPoster(UserPoster userPoster);

    UserPoster updateUserPoster(UserPoster userPoster, Long userId);

    void deleteUserPoster(Long userId);
}
