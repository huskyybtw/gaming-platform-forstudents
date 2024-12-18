package pwr.isa.backend.GameHistory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameHistoryRepository extends CrudRepository<GameHistory, Long> {
    Optional<GameHistory> findByMatchId(Long matchId);
}
