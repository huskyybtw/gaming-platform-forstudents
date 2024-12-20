package pwr.isa.backend.Posters.UserPosters;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pwr.isa.backend.Posters.TeamPosters.TeamPoster;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPosterRepository extends CrudRepository<UserPoster, Long> {

    @Query(value = "SELECT * FROM user_posters ORDER BY :sort ASC LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<UserPoster> findAllSortedAsc(
            @Param("limit") int limit,
            @Param("offset") int offset,
            @Param("sort") String sort
    );

    @Query(value = "SELECT * FROM user_posters ORDER BY :sort DESC LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<UserPoster> findAllSortedDesc(
            @Param("limit") int limit,
            @Param("offset") int offset,
            @Param("sort") String sort
    );

    Optional<UserPoster> findByUserId(Long userId);
}
