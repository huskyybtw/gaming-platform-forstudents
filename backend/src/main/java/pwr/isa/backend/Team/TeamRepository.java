package pwr.isa.backend.Team;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pwr.isa.backend.Player.Player;
import pwr.isa.backend.Posters.UserPosters.UserPoster;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {
    Optional<Team> findByTeamName(String teamName);
    @Query(value = "SELECT * FROM teams ORDER BY :sort ASC LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<Team> findAllSortedAsc(
            @Param("limit") int limit,
            @Param("offset") int offset,
            @Param("sort") String sort
    );

    @Query(value = "SELECT * FROM teams ORDER BY :sort DESC LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<Team> findAllSortedDesc(
            @Param("limit") int limit,
            @Param("offset") int offset,
            @Param("sort") String sort
    );
}
