package pwr.isa.backend.Posters.UserPosters;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPosterRepository extends CrudRepository<UserPoster, Long> {

    @Query(value = "SELECT * FROM user_posters ORDER BY created_at ASC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<UserPoster> findAllWithLimitAndOffsetSortedByDate(@Param("limit") int limit, @Param("offset") int offset);

    Optional<UserPoster> findByUserId(Long userId);
}
