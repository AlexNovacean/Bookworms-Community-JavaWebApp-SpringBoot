package ro.sci.bookwormscommunity.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.web.dto.UserDto;
import ro.sci.bookwormscommunity.web.dto.UserRegistrationDto;

import java.io.IOException;
import java.util.List;

/**
 * Services that extends the {@link UserDetailsService}, adding additional functionality for transactions involving the {@link User}.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @author Raul
 * @see UserDetailsService
 */
public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    void save(UserRegistrationDto registration) throws IOException;

    User findById(Long id);

    List<User> getAllUsers();

    void updateUser(long userId, UserDto userDto) throws IOException;

    @Transactional
    void banUser(long id);

    @Transactional
    void promoteUser(long userId);
}
