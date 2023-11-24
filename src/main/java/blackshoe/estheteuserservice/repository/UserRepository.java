package blackshoe.estheteuserservice.repository;

import blackshoe.estheteuserservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(final String email);
    boolean existsByEmail(final String email);
    Optional<User> findById(final UUID id);
}
