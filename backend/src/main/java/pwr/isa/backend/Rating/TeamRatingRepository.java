package pwr.isa.backend.Rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRatingRepository extends JpaRepository<TeamRating, Long> {

    /**
     * Znajdź ocenę zespołu na podstawie identyfikatora zespołu (team_id).
     *
     * @param teamId identyfikator zespołu
     * @return Optional z TeamRating
     */
    Optional<TeamRating> findByTeamId(Long teamId);
}
