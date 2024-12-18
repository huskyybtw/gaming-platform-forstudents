package pwr.isa.backend.Posters.MatchPosters;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchPosterRepository extends CrudRepository<MatchPoster, Long> {
    @Query(value = "SELECT * FROM match_posters ORDER BY due_date LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<MatchPoster> findMatchPostersByDueDate (int limit, int offset);

}
