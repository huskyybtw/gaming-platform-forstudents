package pwr.isa.backend.Player;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository// x
public interface PlayerRepository extends CrudRepository<Player, Long> {
    Player findByNickname(String nickname);
}
