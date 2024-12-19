package pwr.isa.backend.Posters.UserPosters;

public interface UserPosterService {
    Iterable<UserPoster> getAllUserPosters(int limit, int offset);
    UserPoster getUserPosterById(Long userId);
    UserPoster createUserPoster(UserPoster userPoster);
    UserPoster updateUserPoster(UserPoster userPoster, Long userId);
    void deleteUserPoster(Long userId);
}
