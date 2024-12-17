package pwr.isa.backend.Posters.UserPosters;

import org.springframework.stereotype.Service;

@Service
public class UserPosterServiceImpl implements UserPosterService{
    @Override
    public Iterable<UserPoster> getAllUserPosters(int limit, int offset, boolean sortByRating) {
        return null;
    }

    @Override
    public UserPoster getUserPosterById(Long userId) {
        return null;
    }

    @Override
    public UserPoster createUserPoster(UserPoster userPoster) {
        return null;
    }

    @Override
    public UserPoster updateUserPoster(UserPoster userPoster, Long userId) {
        return null;
    }

    @Override
    public void deleteUserPoster(Long userId) {

    }
}
