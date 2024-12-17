package pwr.isa.backend.Rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRatingRepository extends JpaRepository<PlayerRating, Long> {

    /**
     * Znajdź ocenę gracza na podstawie identyfikatora gracza (player.id).
     *
     * @param playerId identyfikator gracza
     * @return Optional z PlayerRating
     */
    Optional<PlayerRating> findByPlayerId(Long playerId);
}
