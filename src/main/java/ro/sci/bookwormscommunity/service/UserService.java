package ro.sci.bookwormscommunity.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.web.dto.UserRegistrationDto;

import java.io.IOException;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    void save(UserRegistrationDto registration);

}
