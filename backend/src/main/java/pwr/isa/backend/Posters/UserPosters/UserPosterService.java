package pwr.isa.backend.Posters.UserPosters;

import java.util.List;

public interface UserPosterService {

    UserPoster getUserPosterById(Long userId);
    UserPoster createUserPoster(UserPoster userPoster);
    UserPoster updateUserPoster(UserPoster userPoster, Long userId);
    void deleteUserPoster(Long userId);

    List<UserPoster> getAllUserPosters(int limit, int offset, String sortBy, String sortDirection);
}
