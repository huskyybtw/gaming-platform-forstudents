package pwr.isa.backend.Player;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pwr.isa.backend.Posters.MatchPosters.MatchPoster;

import java.util.List;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {
    Player findByNickname(String nickname);
    Player findByUserId(Long userId);

    @Query(value = "SELECT * FROM players ORDER BY :sort ASC LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<Player> findAllSortedAsc(
            @Param("limit") int limit,
            @Param("offset") int offset,
            @Param("sort") String sort
    );

    @Query(value = "SELECT * FROM players ORDER BY :sort DESC LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<Player> findAllSortedDesc(
            @Param("limit") int limit,
            @Param("offset") int offset,
            @Param("sort") String sort
    );


    Player findByTagLine(String tagLine);
}
