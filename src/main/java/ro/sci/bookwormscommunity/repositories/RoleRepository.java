package ro.sci.bookwormscommunity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.sci.bookwormscommunity.model.Role;

/**
 * Repository of the {@link Role} class.
 * <p>
 * Handles the DB connection and all the operations regarding the DB (create, update, delete, etc.).
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @see JpaRepository
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
