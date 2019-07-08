package ro.sci.bookwormscommunity.web.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ro.sci.bookwormscommunity.config.WebSecurityConfig;
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.service.BookService;
import ro.sci.bookwormscommunity.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(MainController.class)
@Import(WebSecurityConfig.class)
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    public void root_withAuthenticatedUser_shouldReturnHomePage() throws Exception {
        User user = new User(1, "test@mail.com");
        List<Book> bookList = new ArrayList<>(Arrays.asList(new Book(1), new Book(2), new Book(3)));

        when(userService.findByEmail(anyString())).thenReturn(user);
        when(bookService.getTop10RatedBooks()).thenReturn(bookList);
        when(bookService.getLatest10AddedBooks()).thenReturn(bookList);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("Home"))
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("topBooks", bookList))
                .andExpect(model().attribute("latestBooks", bookList));

        verify(bookService, times(1)).getTop10RatedBooks();
        verify(bookService, times(1)).getLatest10AddedBooks();
        verifyNoMoreInteractions(bookService);
        verify(userService, times(1)).findByEmail(anyString());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void root_withUnauthenticatedUser_shouldReturnHomePage() throws Exception {
        List<Book> bookList = new ArrayList<>(Arrays.asList(new Book(1), new Book(2), new Book(3)));

        when(bookService.getTop10RatedBooks()).thenReturn(bookList);
        when(bookService.getLatest10AddedBooks()).thenReturn(bookList);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("Home"))
                .andExpect(model().attribute("topBooks", bookList))
                .andExpect(model().attribute("latestBooks", bookList));

        verify(bookService, times(1)).getTop10RatedBooks();
        verify(bookService, times(1)).getLatest10AddedBooks();
        verifyNoMoreInteractions(bookService);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void login() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));

        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(bookService);
    }
}