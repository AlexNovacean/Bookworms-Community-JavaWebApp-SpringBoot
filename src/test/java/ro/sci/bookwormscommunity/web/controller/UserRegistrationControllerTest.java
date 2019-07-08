package ro.sci.bookwormscommunity.web.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ro.sci.bookwormscommunity.config.WebSecurityConfig;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.service.UserService;
import ro.sci.bookwormscommunity.web.dto.UserRegistrationDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserRegistrationController.class)
@Import(WebSecurityConfig.class)
public class UserRegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void showRegistrationForm() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));

        verifyNoMoreInteractions(userService);

    }

    @Test
    public void registerUserAccount_withExistingUserEmail_shouldReturnFieldError() throws Exception {
        User user = new User(1L,"test@mail.com");
        MockMultipartFile emptyFile = new MockMultipartFile("image", new byte[0]);
        when(userService.findByEmail(anyString())).thenReturn(user);

        mockMvc.perform(multipart("/registration")
                .file(emptyFile)
                .with(csrf())
                .param("firstName", " first name")
                .param("lastName", "last name")
                .param("nickName", "nickname")
                .param("location", "location")
                .param("password", "password")
                .param("confirmPassword", "password")
                .param("email", "test@mail.com")
                .param("confirmEmail", "test@mail.com")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrors("user", "email"));

        verify(userService, times(1)).findByEmail(anyString());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void registerUserAccount_withInvalidAttributes_shouldReturnFieldError() throws Exception{
        MockMultipartFile fileOver1MB = new MockMultipartFile("image", new byte[2*1024*1024]);

        when(userService.findByEmail(anyString())).thenReturn(null);

        mockMvc.perform(multipart("/registration")
                .file(fileOver1MB)
                .with(csrf())
                .param("firstName", "")
                .param("lastName", "")
                .param("nickName", "")
                .param("location", "")
                .param("password", "password")
                .param("confirmPassword", "")
                .param("email", "test@mail.com")
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

        verify(userService, times(1)).findByEmail(anyString());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void registerUserAccount_withValidAttributes_shouldRegisterAccount() throws Exception{
        User user = new User();
        MockMultipartFile emptyFile = new MockMultipartFile("image", new byte[0]);

        when(userService.findByEmail(anyString())).thenReturn(null);
        doAnswer((InvocationOnMock invocation) -> {
            Object[] argument = invocation.getArguments();
            UserRegistrationDto userDto = (UserRegistrationDto) argument[0];
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setNickname(userDto.getNickName());
            user.setPassword(userDto.getPassword());
            user.setEmail(userDto.getEmail());
            user.setLocation(userDto.getLocation());
            return null;
        }).when(userService).save(any(UserRegistrationDto.class));

        mockMvc.perform(multipart("/registration")
                .file(emptyFile)
                .with(csrf())
                .param("firstName", "first name")
                .param("lastName", "last name")
                .param("nickName", "nickname")
                .param("location", "location")
                .param("password", "password")
                .param("confirmPassword", "password")
                .param("email", "test@mail.com")
                .param("confirmEmail", "test@mail.com")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?success"))
                .andExpect(view().name("redirect:/login?success"));

        assertEquals("first name",user.getFirstName());
        assertEquals("last name",user.getLastName());
        assertEquals("nickname",user.getNickname());
        assertEquals("location",user.getLocation());
        assertEquals("password",user.getPassword());
        assertEquals("test@mail.com",user.getEmail());
        verify(userService, times(1)).findByEmail(anyString());
        verify(userService,times(1)).save(any(UserRegistrationDto.class));
        verifyNoMoreInteractions(userService);
    }
}