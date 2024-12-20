package pwr.isa.backend.Posters.TeamPosters;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamPosterRepository extends CrudRepository<TeamPoster, Long> {

    @Query(value = "SELECT * FROM team_posters ORDER BY :sort ASC LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<TeamPoster> findAllSortedAsc(
            @Param("limit") int limit,
            @Param("offset") int offset,
            @Param("sort") String sort
    );

    @Query(value = "SELECT * FROM team_posters ORDER BY :sort DESC LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<TeamPoster> findAllSortedDesc(
            @Param("limit") int limit,
            @Param("offset") int offset,
            @Param("sort") String sort
    );
    Optional<TeamPoster> findByTeamId(Long teamId);
}
