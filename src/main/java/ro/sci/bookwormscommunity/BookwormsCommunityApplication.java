package ro.sci.bookwormscommunity;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ro.sci.bookwormscommunity.model.Role;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.repositories.UserRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@SpringBootApplication
public class BookwormsCommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookwormsCommunityApplication.class, args);
    }

    @Bean
    public CommandLineRunner createAdminIfNotExist(UserRepository userRepository) {
        return (args) -> {

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            Path path = Paths.get("src/main/resources/static/images/admin-profile.jpg");

            User user = userRepository.findByEmail("admin");

            if (user == null) {
                user = new User("Admin", "Administrator", "admin", "Admin", "Application");
                user.setPassword(passwordEncoder.encode("admin"));
                user.setRoles(Arrays.asList(new Role("ROLE_ADMIN")));
                user.setPhoto(Files.readAllBytes(path));
                userRepository.save(user);
            }
        };
    }
}
