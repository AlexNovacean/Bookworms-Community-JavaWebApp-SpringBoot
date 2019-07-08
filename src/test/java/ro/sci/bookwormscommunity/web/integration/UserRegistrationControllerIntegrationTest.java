package ro.sci.bookwormscommunity.web.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ro.sci.bookwormscommunity.BookwormsCommunityApplication;
import ro.sci.bookwormscommunity.model.Role;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.repositories.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BookwormsCommunityApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class UserRegistrationControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Test
    public void showRegistrationForm() throws Exception {
        mvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));
    }

    @Test
    public void registerUserAccount_withValidAttributes_shouldSaveUser() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile("image", new byte[0]);

        mvc.perform(multipart("/registration")
                .file(emptyFile)
                .with(csrf())
                .param("firstName", "First Name")
                .param("lastName", "Last Name")
                .param("nickName", "Nickname")
                .param("location", "Location")
                .param("password", "password")
                .param("confirmPassword", "password")
                .param("email", "user@mail.com")
                .param("confirmEmail", "user@mail.com")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?success"))
                .andExpect(view().name("redirect:/login?success"));

        User found = userRepository.findByEmail("user@mail.com");

        assertEquals("First Name", found.getFirstName());
        assertEquals("Last Name", found.getLastName());
        assertEquals("Nickname", found.getNickname());
        assertEquals("Location", found.getLocation());
        assertEquals("user@mail.com", found.getEmail());
        assertTrue(encoder.matches("password", found.getPassword()));
    }

    @Test
    public void registerUserAccount_withExistingUserEmail_shouldReturnFieldError() throws Exception {
        userRepository.save(createTestUser());

        MockMultipartFile emptyFile = new MockMultipartFile("image", new byte[0]);

        mvc.perform(multipart("/registration")
                .file(emptyFile)
                .with(csrf())
                .param("firstName", "Same Email")
                .param("lastName", "Same Email")
                .param("nickName", "Same Email")
                .param("location", "Same Email")
                .param("password", "password")
                .param("confirmPassword", "password")
                .param("email", "test@mail.com")
                .param("confirmEmail", "test@mail.com")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrors("user", "email"));

        assertFalse(userRepository.findAll().stream().anyMatch(user -> user.getFirstName().equals("Same Email")));
    }

    @Test
    public void registerUserAccount_withInvalidAttributes_shouldReturnFieldError() throws Exception {
        MockMultipartFile fileOver1MB = new MockMultipartFile("image", new byte[2 * 1024 * 1024]);

        mvc.perform(multipart("/registration")
                .file(fileOver1MB)
                .with(csrf())
                .param("firstName", "")
                .param("lastName", "")
                .param("nickName", "")
                .param("location", "")
                .param("password", "password")
                .param("confirmPassword", "")
                .param("email", "address@mail.com")
                .param("confirmEmail", "")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().errorCount(11))
                .andExpect(model().attributeHasFieldErrors("user", "confirmEmail"))
                .andExpect(model().attributeHasFieldErrors("user", "firstName"))
                .andExpect(model().attributeHasFieldErrors("user", "lastName"))
                .andExpect(model().attributeHasFieldErrors("user", "nickName"))
                .andExpect(model().attributeHasFieldErrors("user", "location"))
                .andExpect(model().attributeHasFieldErrors("user", "confirmPassword"));

        assertNull(userRepository.findByEmail("address@mail.com"));
    }

    private User createTestUser() throws IOException {
        Path path = Paths.get("src/main/resources/static/images/default-picture.png");
        User user = new User();
        user.setFirstName("test user");
        user.setLastName("test user");
        user.setNickname("test user");
        user.setLocation("test user");
        user.setEmail("test@mail.com");
        user.setPassword(encoder.encode("password"));
        user.setRoles(Collections.singletonList(new Role("ROLE_USER")));
        user.setEnabled(true);
        user.setPhoto(Files.readAllBytes(path));
        return user;
    }

}