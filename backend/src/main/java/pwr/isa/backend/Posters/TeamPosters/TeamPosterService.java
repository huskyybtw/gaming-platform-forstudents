package pwr.isa.backend.Posters.TeamPosters;

import org.springframework.stereotype.Service;

import java.util.List;

public interface TeamPosterService {
    List<TeamPoster> getAllTeamPosters(int limit, int offset, String sortBy, String sortDirection);

    TeamPoster getTeamPosterById(Long teamId);

    TeamPoster createTeamPoster(TeamPoster teamPoster);

    TeamPoster updateTeamPoster(TeamPoster teamPoster, Long teamId);

    void deleteTeamPoster(Long teamId);
}
