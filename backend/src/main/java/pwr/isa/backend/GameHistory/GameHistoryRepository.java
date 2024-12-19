package pwr.isa.backend.GameHistory;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameHistoryRepository extends CrudRepository<GameHistory, Long> {
    Optional<GameHistory> findByMatchId(Long matchId);

    @Query(value = "SELECT gh.* FROM game_history gh " +
            "JOIN match_participant mp ON gh.match_id = mp.match_id " +
            "WHERE mp.user_id = :userId " +
            "ORDER BY gh.end_of_match_date DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<GameHistory> findAllMatchesByUserIdSorted(@Param("userId") Long userId, @Param("limit") int limit);

    @Query(value = "SELECT gh.* FROM game_history gh " +
            "JOIN match_participant mp ON gh.match_id = mp.match_id " +
            "WHERE mp.team_id = :teamId " +
            "ORDER BY gh.end_of_match_date DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<GameHistory> findAllMatchesByTeamIdSorted(@Param("teamId") Long teamId, @Param("limit") int limit);


}
