package ro.sci.bookwormscommunity.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.web.dto.UserRegistrationDto;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    void save(UserRegistrationDto registration) throws Exception;

}
