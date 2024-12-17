package pwr.isa.backend.Posters.TeamPosters;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamPosterRepository extends CrudRepository<TeamPoster, Long> {
}
