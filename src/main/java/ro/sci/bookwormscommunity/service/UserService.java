package ro.sci.bookwormscommunity.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.web.dto.UserRegistrationDto;

import java.util.List;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    void save(UserRegistrationDto registration) throws Exception;

    User findById(Long id);

    List<User> getAllUsers();

}
