package ro.sci.bookwormscommunity;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ro.sci.bookwormscommunity.model.Role;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.repositories.UserRepository;

import java.util.Arrays;

@SpringBootApplication
public class BookwormsCommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookwormsCommunityApplication.class, args);
	}

	@Bean
	public CommandLineRunner createAdminIfNotExist(UserRepository userRepository){
		return (args) -> {

			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

			User user = userRepository.findByEmail("admin");

			if(user==null){
				user = new User("Admin","Administrator","admin","admin","application");
				user.setPassword(passwordEncoder.encode("admin"));
				user.setRoles(Arrays.asList(new Role("ROLE_ADMIN")));
				userRepository.save(user);
			}
		};
	}
}
