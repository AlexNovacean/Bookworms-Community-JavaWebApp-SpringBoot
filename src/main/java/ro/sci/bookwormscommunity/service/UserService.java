package ro.sci.bookwormscommunity.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.web.dto.UserDto;
import ro.sci.bookwormscommunity.web.dto.UserRegistrationDto;

import java.util.List;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    void save(UserRegistrationDto registration) throws Exception;

    User findById(Long id);

    List<User> getAllUsers();

    void updateUser(long id, UserDto userDto) throws Exception;

    @Transactional
    public void banUser(long id);

    void promoteUser(long userId);

}
