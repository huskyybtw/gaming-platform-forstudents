package pwr.isa.backend.GameHistory.MatchParticipants;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchParticipantsRepository extends CrudRepository<MatchParticipant, Long> {
    // MECZE UZYTKOWNIKA
    @Query(value = "SELECT match_id FROM Match_Participant WHERE User_id = :userId", nativeQuery = true)
    List<Long> findMatchesByUserId(@Param("userId") Long userId);

    // MECZE DRUZYNY
    @Query(value = "SELECT match_id from Match_Participant WHERE team_id = :teamId", nativeQuery = true)
    List<Long> findPlayersByTeamId(@Param("teamId") Long teamId);

    // GRACZE W DANYM MECZU
    @Query(value = "SELECT User_id FROM Match_Participant WHERE match_id = :matchId", nativeQuery = true)
    List<Long> findPlayersByMatchId(@Param("matchId") Long matchId);
}
