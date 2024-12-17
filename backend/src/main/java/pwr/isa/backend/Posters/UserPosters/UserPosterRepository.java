package pwr.isa.backend.Posters.UserPosters;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPosterRepository extends CrudRepository<UserPoster, Long> {
    UserPoster findByUserId(Long userId);
}
