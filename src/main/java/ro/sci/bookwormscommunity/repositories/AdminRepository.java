package ro.sci.bookwormscommunity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.sci.bookwormscommunity.model.User;

public interface AdminRepository extends JpaRepository<User, Integer> {
}
