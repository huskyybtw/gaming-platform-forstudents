package pwr.isa.backend.Posters.TeamPosters;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamPosterRepository extends CrudRepository<TeamPoster, Long> {

    @Query(value = "SELECT * FROM team_posters LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<TeamPoster> findAllWithLimitAndOffset(@Param("limit") int limit, @Param("offset") int offset);
    @Query(value = "SELECT * FROM team_posters ORDER BY created_at ASC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<TeamPoster> findAllWithLimitAndOffsetSortedByDate(@Param("limit") int limit, @Param("offset") int offset);
    Optional<TeamPoster> findByTeamId(Long teamId);
    // ORDER BY Rating...
}
