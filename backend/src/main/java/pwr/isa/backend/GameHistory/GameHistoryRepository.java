package pwr.isa.backend.GameHistory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameHistoryRepository extends CrudRepository<GameHistory, Long> {
}
