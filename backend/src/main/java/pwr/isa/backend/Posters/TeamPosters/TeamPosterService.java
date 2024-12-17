package pwr.isa.backend.Posters.TeamPosters;

import org.springframework.stereotype.Service;

public interface TeamPosterService {
    Iterable<TeamPoster> getAllTeamPosters(int limit, int offset, boolean sortByRating);

    TeamPoster getTeamPosterById(Long teamId);

    TeamPoster createTeamPoster(TeamPoster teamPoster);

    TeamPoster updateTeamPoster(TeamPoster teamPoster, Long teamId);

    void deleteTeamPoster(Long teamId);
}
