package pwr.isa.backend.Posters.UserPosters;

import org.springframework.stereotype.Service;

@Service
public class UserPosterServiceImpl implements UserPosterService{
    private final UserPosterRepository userPosterRepository;

    public UserPosterServiceImpl(UserPosterRepository userPosterRepository) {
        this.userPosterRepository = userPosterRepository;
    }
    /*
        TODO Zaimplementowac metody
        TODO Uwzglednic walidacje
        TODO uwzglednic tranzakcje
        TODO przetestowac
    */
    // zwroc wszystkie postery uwzgledniajac limit offset
    // jesli sortowanie to posortuj pierw
    // musisz napisac querry w repository
    @Override
    public Iterable<UserPoster> getAllUserPosters(int limit, int offset, boolean sortByRating) {
        return null;
    }

    @Override
    public UserPoster getUserPosterById(Long userId) {
        return null;
    }
    // kazdy user moze miec tylko jeden poster mozesz przekierowac na update jesli juz istnieje
    @Override
    public UserPoster createUserPoster(UserPoster userPoster) {
        return null;
    }
    // wymaga podania wszystkich pól
    @Override
    public UserPoster updateUserPoster(UserPoster userPoster, Long userId) {
        return null;
    }

    @Override
    public void deleteUserPoster(Long userId) {

    }
}
