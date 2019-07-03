package ro.sci.bookwormscommunity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ro.sci.bookwormscommunity.model.User;

/**
 * Repository of the {@link User} class.
 * <p>
 * Handles the DB connection and all the operations regarding the DB (create, update, delete, etc.).
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @see JpaRepository
 */
public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional
    User findByEmail(String email);

}
