package pwr.isa.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pwr.isa.backend.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    User findByEmail(String email);
}
