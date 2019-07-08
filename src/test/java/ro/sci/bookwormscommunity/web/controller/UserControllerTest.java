package ro.sci.bookwormscommunity.web.controller;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
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
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.service.BookService;
import ro.sci.bookwormscommunity.service.UserService;
import ro.sci.bookwormscommunity.web.dto.UserDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@Import(WebSecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private BookService bookService;

    @Test
    @WithMockUser
    public void userProfile() throws Exception {
        User user = new User(1L, "test@mail.com");
        List<Book> books = new ArrayList<>(Arrays.asList(new Book(1L), new Book(2L)));

        when(userService.findByEmail(anyString())).thenReturn(user);
        when(bookService.getUserBooks(anyLong())).thenReturn(books);

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(view().name("user"))
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("books", hasSize(2)))
                .andExpect(model().attribute("books", books));

        verify(userService, times(1)).findByEmail(anyString());
        verifyNoMoreInteractions(userService);
        verify(bookService, times(1)).getUserBooks(anyLong());
        verifyNoMoreInteractions(bookService);
    }

    @Test
    @WithMockUser
    public void seeUserProfile() throws Exception {
        User user = new User(1L, "test@mail.com");
        List<Book> books = new ArrayList<>(Arrays.asList(new Book(1L), new Book(2L)));

        when(userService.findById(anyLong())).thenReturn(user);
        when(bookService.getUserBooks(anyLong())).thenReturn(books);

        mockMvc.perform(get("/user/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("user"))
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("books", hasSize(2)))
                .andExpect(model().attribute("books", books));

        verify(userService, times(1)).findById(anyLong());
        verifyNoMoreInteractions(userService);
        verify(bookService, times(1)).getUserBooks(anyLong());
        verifyNoMoreInteractions(bookService);
    }

    @Test
    @WithMockUser
    public void showUpdateForm() throws Exception {
        User user = new User(1L, "first name", "last name", "nickname", "test@mail.com", "location", true);

        when(userService.findByEmail(anyString())).thenReturn(user);

        mockMvc.perform(get("/user/editProfile"))
                .andExpect(status().isOk())
                .andExpect(view().name("updateUser"))
                .andExpect(model().attribute("userDto", Matchers.isA(UserDto.class)))
                .andExpect(model().attribute("userDto", hasProperty("id", equalTo(1L))))
                .andExpect(model().attribute("userDto", hasProperty("firstName", is("first name"))))
                .andExpect(model().attribute("userDto", hasProperty("lastName", is("last name"))))
                .andExpect(model().attribute("userDto", hasProperty("nickname", is("nickname"))))
                .andExpect(model().attribute("userDto", hasProperty("email", is("test@mail.com"))))
                .andExpect(model().attribute("userDto", hasProperty("location", is("location"))));

        verify(userService, times(1)).findByEmail(anyString());
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    @WithMockUser(username = "test@mail.com")
    public void updateUser_withOtherUserEmail_shouldReturnFieldError() throws Exception {
        User user = new User(1L, "test@mail.com");
        MockMultipartFile emptyFile = new MockMultipartFile("photo", new byte[0]);

        when(userService.findByEmail(anyString())).thenReturn(user);

        mockMvc.perform(multipart("/user/updateProfile")
                .file(emptyFile)
                .with(csrf())
                .param("firstName", "first name")
                .param("lastName", "last name")
                .param("nickname", "nickname")
                .param("location", "location")
                .param("email", "changed@mail.com")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("updateUser"))
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrors("userDto", "email"));

        verify(userService, times(2)).findByEmail(anyString());
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(username = "test@mail.com")
    public void updateUser_withInvalidAttributes_shouldReturnFieldError() throws Exception {
        User user = new User(1L, "test@mail.com");
        byte[] over1MB = new byte[2 * 1024 * 1024];
        Arrays.fill(over1MB, (byte) 0);
        MockMultipartFile fileOver1MB = new MockMultipartFile("photo", over1MB);

        when(userService.findByEmail(anyString())).thenReturn(user);

        mockMvc.perform(multipart("/user/updateProfile")
                .file(fileOver1MB)
                .with(csrf())
                .param("firstName", "")
                .param("lastName", "")
                .param("nickname", "")
                .param("location", "")
                .param("email", "test@mail.com")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("updateUser"))
                .andExpect(model().errorCount(6))
                .andExpect(model().attributeHasFieldErrors("userDto", "firstName", "lastName", "nickname", "location", "photo"));

        verify(userService, times(1)).findByEmail(anyString());
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(username = "test@mail.com", password = "initial")
    public void updateUser_withMismatchedPassword_shouldReturnFieldError() throws Exception {
        User user = new User(1L, "test@mail.com");
        MockMultipartFile emptyFile = new MockMultipartFile("photo", new byte[0]);

        when(userService.findByEmail(anyString())).thenReturn(user);

        mockMvc.perform(multipart("/user/updateProfile")
                .file(emptyFile)
                .with(csrf())
                .param("firstName", "first name")
                .param("lastName", "last name")
                .param("nickname", "nickname")
                .param("location", "location")
                .param("email", "test@mail.com")
                .param("password", "changed")
                .param("confirmPassword", "mismatched")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("updateUser"))
                .andExpect(model().errorCount(1));

        verify(userService, times(1)).findByEmail(anyString());
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(username = "test@mail.com")
    public void updateUser_withValidAttributes_shouldUpdateUser() throws Exception {
        User user = new User(1L, "test@mail.com");
        MockMultipartFile emptyFile = new MockMultipartFile("photo", new byte[0]);

        when(userService.findByEmail(anyString())).thenReturn(user);
        doAnswer((InvocationOnMock invocation) -> {
            Object[] arguments = invocation.getArguments();
            UserDto updatedUser = (UserDto) arguments[1];
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setNickname(updatedUser.getNickname());
            user.setLocation(updatedUser.getLocation());
            return null;
        }).when(userService).updateUser(anyLong(), ArgumentMatchers.any(UserDto.class));

        mockMvc.perform(multipart("/user/updateProfile")
                .file(emptyFile)
                .with(csrf())
                .param("firstName", "first name")
                .param("lastName", "last name")
                .param("nickname", "nickname")
                .param("location", "location")
                .param("email", "test@mail.com")
                .param("password", "")
                .param("confirmPassword", "")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/user?updated"))
                .andExpect(view().name("redirect:/user?updated"));

        verify(userService, times(1)).findByEmail(anyString());
        verify(userService, times(1)).updateUser(anyLong(), ArgumentMatchers.any(UserDto.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(username = "test@mail.com")
    public void updateUser_withNewPasswordOrEmail_shouldLogUserOut() throws Exception {
        User user = new User(1L, "test@mail.com");
        MockMultipartFile emptyFile = new MockMultipartFile("photo", new byte[0]);

        when(userService.findByEmail("test@mail.com")).thenReturn(user);
        when(userService.findByEmail("changed@mail.com")).thenReturn(null);
        doAnswer((InvocationOnMock invocation) -> {
            Object[] arguments = invocation.getArguments();
            UserDto updatedUser = (UserDto) arguments[1];
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setNickname(updatedUser.getNickname());
            user.setLocation(updatedUser.getLocation());
            return null;
        }).when(userService).updateUser(anyLong(), ArgumentMatchers.any(UserDto.class));

        mockMvc.perform(multipart("/user/updateProfile")
                .file(emptyFile)
                .with(csrf())
                .param("firstName", "first name")
                .param("lastName", "last name")
                .param("nickname", "nickname")
                .param("location", "location")
                .param("email", "changed@mail.com")
                .param("password", "newPassword")
                .param("confirmPassword", "newPassword")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/logout"))
                .andExpect(view().name("redirect:/logout"));

        verify(userService, times(2)).findByEmail(anyString());
        verify(userService, times(1)).updateUser(anyLong(), ArgumentMatchers.any(UserDto.class));
        verifyNoMoreInteractions(userService);
    }
}