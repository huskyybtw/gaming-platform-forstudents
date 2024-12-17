package pwr.isa.backend.Team;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pwr.isa.backend.Player.Player;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {
    Optional<Team> findByTeamName(String teamName);
    // find list of players order by id limit= limit offset= offset
    @Query(value = "SELECT * FROM teams ORDER BY id LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Team> findBestTeams(int limit, int offset);
}
