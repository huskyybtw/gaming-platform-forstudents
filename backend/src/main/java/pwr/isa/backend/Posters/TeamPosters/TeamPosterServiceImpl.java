package pwr.isa.backend.Posters.TeamPosters;

import org.springframework.stereotype.Service;

@Service
public class TeamPosterServiceImpl implements TeamPosterService {
    private final TeamPosterRepository teamPosterRepository;

    public TeamPosterServiceImpl(TeamPosterRepository teamPosterRepository) {
        this.teamPosterRepository = teamPosterRepository;
    }

    @Override
    public Iterable<TeamPoster> getAllTeamPosters(int limit, int offset, boolean sortByRating) {
        return null;
    }

    @Override
    public TeamPoster getTeamPosterById(Long teamId) {
        return null;
    }

    @Override
    public TeamPoster createTeamPoster(TeamPoster teamPoster) {
        return null;
    }

    @Override
    public TeamPoster updateTeamPoster(TeamPoster teamPoster, Long teamId) {
        return null;
    }

    @Override
    public void deleteTeamPoster(Long teamId) {

    }
}
