package pwr.isa.backend.Team.TeamUsers;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamUsersRepository extends CrudRepository<TeamUser, Long> {
    @Query(value = "SELECT user_id FROM team_users WHERE team_id = :teamId", nativeQuery = true)
    List<Long> findUsersByTeamId(@Param("teamId") Long teamId);

    @Query(value = "SELECT team_id FROM team_users WHERE team_id = :userId", nativeQuery = true)
    List<Long> findTeamsByUserId(@Param("userId") Long userId);

    @Query(value = "INSERT INTO team_users (team_id, user_id) VALUES (:teamId, :userId)", nativeQuery = true)
    void addTeamUser(@Param("teamId") Long teamId, @Param("userId") Long userId);

    @Query(value = "DELETE FROM team_users WHERE team_id = :teamId AND user_id = :userId", nativeQuery = true)
    void deleteTeamUser(@Param("teamId") Long teamId, @Param("userId") Long userId);
}
