package pwr.isa.backend.Posters.MatchPosters;

import java.util.List;

public interface MatchPosterService {
    MatchPosterDTO createMatchPoster(MatchPoster matchPoster, Long teamId);

    MatchPosterDTO joinMatchPoster(Long posterId, Long userId, int team);

    MatchPosterDTO leaveMatchPoster(Long posterId, Long userId);

    MatchPosterDTO updateMatchPoster(Long posterId, MatchPoster matchPoster);

    void deleteMatchPoster(Long posterId);

    MatchPosterDTO getMatchPosterById(Long posterId);

    MatchPosterDTO joinAsTeam(Long posterId, Long teamId);

    MatchPosterDTO startMatch(Long posterId);

    void retriveMatchPoster(Long posterId);

    List<MatchPosterDTO> getAllMatchPosters(int limit, int offset, String sortBy, String sortDirection);
}
