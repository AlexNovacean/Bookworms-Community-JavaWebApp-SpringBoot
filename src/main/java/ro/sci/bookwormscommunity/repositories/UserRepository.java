package ro.sci.bookwormscommunity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ro.sci.bookwormscommunity.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional
    User findByEmail(String email);

}
