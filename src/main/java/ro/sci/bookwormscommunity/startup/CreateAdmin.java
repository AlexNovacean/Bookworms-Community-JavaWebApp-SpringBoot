package ro.sci.bookwormscommunity.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ro.sci.bookwormscommunity.model.Role;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.repositories.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * On Application StartUp creates an ADMIN user if one does not exist.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 */
@Component
public class CreateAdmin implements ApplicationListener<ApplicationReadyEvent> {

    private Logger logger = LoggerFactory.getLogger(CreateAdmin.class);

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent applicationReadyEvent) {
        Path path = Paths.get("src/main/resources/static/images/admin-profile.jpg");
        User user = userRepository.findByEmail("admin");

        try {
            if (user == null) {
                user = new User("Admin", "Administrator", "admin", "Admin", "Application");
                user.setPassword(passwordEncoder.encode("admin"));
                user.setRoles(Arrays.asList(new Role("ROLE_ADMIN")));
                user.setPhoto(Files.readAllBytes(path));
                user.setEnabled(true);
                userRepository.save(user);
            }
        } catch (IOException e) {
            logger.error("Error when retrieving the admin default picture: ", e);
        }
    }
}
