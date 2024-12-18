package pwr.isa.backend.GameHistory.MatchParticipants;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MatchParticipantsRepository extends CrudRepository<MatchParticipant, Long> {
    // MECZE UZYTKOWNIKA
    @Query(value = "SELECT match_id FROM Match_Participant WHERE User_id = :userId", nativeQuery = true)
    List<Long> findMatchesByUserId(@Param("userId") Long userId);

    // MECZE DRUZYNY
    @Query(value = "SELECT match_id from Match_Participant WHERE team_id = :teamId", nativeQuery = true)
    List<Long> findMatchesByTeamId(@Param("teamId") Long teamId);

    // USER ID GRACZY W DANYM MECZU
    @Query(value = "SELECT User_id FROM Match_Participant WHERE match_id = :matchId", nativeQuery = true)
    List<Long> findPlayersByMatchId(@Param("matchId") Long matchId);

    // MATCH PARTICIPANTS W DANYM MECZU
    @Query(value = "SELECT * FROM Match_Participant WHERE match_id = :matchId", nativeQuery = true)
    List<MatchParticipant> findMatchParticipantsByMatchId(@Param("matchId") Long matchId);

    // DODANIE GRACZA DO MECZU
    @Modifying
    @Query(value = "INSERT INTO Match_Participant (match_id, user_id, team_id, riot_team_number) VALUES (:matchId, :userId,:teamId,:riotTeamNumber)", nativeQuery = true)
    void addMatchParticipant(@Param("matchId") Long matchId, @Param("userId") Long userId, @Param("teamId") Long _teamId, @Param("riotTeamNumber") int riotTeamNumber);

    // USUNIECIE GRACZA Z MECZU
    @Modifying
    @Query(value = "DELETE FROM Match_Participant WHERE match_id = :matchId AND user_id = :userId", nativeQuery = true)
    void deleteMatchParticipant(@Param("matchId") Long matchId, @Param("userId") Long userId);

    // USUNIECIE WSZYSTKICH GRACZY Z MECZU
    @Modifying
    @Query(value = "DELETE FROM Match_Participant WHERE match_id = :matchId", nativeQuery = true)
    void deleteAllByMatchId(Long matchId);
}
