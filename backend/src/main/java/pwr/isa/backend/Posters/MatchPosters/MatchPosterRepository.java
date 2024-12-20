package pwr.isa.backend.Posters.MatchPosters;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pwr.isa.backend.Posters.TeamPosters.TeamPoster;

import java.util.List;

@Repository
public interface MatchPosterRepository extends CrudRepository<MatchPoster, Long> {
    @Query(value = "SELECT * FROM match_posters ORDER BY :sort ASC LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<MatchPoster> findAllSortedAsc(
            @Param("limit") int limit,
            @Param("offset") int offset,
            @Param("sort") String sort
    );

    @Query(value = "SELECT * FROM match_posters ORDER BY :sort DESC LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<MatchPoster> findAllSortedDesc(
            @Param("limit") int limit,
            @Param("offset") int offset,
            @Param("sort") String sort
    );

}
