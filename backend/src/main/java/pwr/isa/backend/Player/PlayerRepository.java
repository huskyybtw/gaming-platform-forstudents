package pwr.isa.backend.Player;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {
    Player findByNickname(String nickname);
    Player findByUserId(Long userId);

    // find list of players order by id limit= limit offset= offset
    @Query(value = "SELECT * FROM players ORDER BY rating LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Player> findBestPlayers(int limit, int offset);

    Player findByTagLine(String tagLine);
}
