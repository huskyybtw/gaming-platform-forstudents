package pwr.isa.backend.Posters.TeamPosters;

import org.springframework.stereotype.Service;

@Service
public class TeamPosterServiceImpl implements TeamPosterService {
    private final TeamPosterRepository teamPosterRepository;

    public TeamPosterServiceImpl(TeamPosterRepository teamPosterRepository) {
        this.teamPosterRepository = teamPosterRepository;
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
    public Iterable<TeamPoster> getAllTeamPosters(int limit, int offset, boolean sortByRating) {
        return null;
    }

    // nazwa mowi za siebie
    @Override
    public TeamPoster getTeamPosterById(Long teamId) {
        return null;
    }
    // kazdy team moze miec tylko jeden poster mozesz przekierowac na update jesli juz istnieje
    @Override
    public TeamPoster createTeamPoster(TeamPoster teamPoster) {
        return null;
    }

    // wymaga podania wszystkich pól
    // walidacja tych pół
    @Override
    public TeamPoster updateTeamPoster(TeamPoster teamPoster, Long teamId) {
        return null;
    }

    @Override
    public void deleteTeamPoster(Long teamId) {

    }
}
