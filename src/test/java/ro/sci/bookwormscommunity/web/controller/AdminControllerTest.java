package ro.sci.bookwormscommunity.web.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ro.sci.bookwormscommunity.config.WebSecurityConfig;
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.model.Role;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.service.BanMailService;
import ro.sci.bookwormscommunity.service.BookService;
import ro.sci.bookwormscommunity.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AdminController.class)
@Import(WebSecurityConfig.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private UserService userService;

    @MockBean
    private BanMailService mailService;

    @Test
    @WithMockUser
    public void promote() throws Exception {
        User user = new User();
        Role roleUser = new Role("user");
        Role roleModerator = new Role("moderator");
        user.setRoles(Arrays.asList(roleUser));

        doAnswer((InvocationOnMock invocation) -> {
            user.setRoles(Arrays.asList(roleModerator));
            return null;
        }).when(userService).promoteUser(anyLong());

        mockMvc.perform(get("/user/{id}/promote", 1L))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/user/1?promoted"))
                .andExpect(view().name("redirect:/user/1?promoted"));

        assertEquals(Arrays.asList(roleModerator), user.getRoles());
        verify(userService, times(1)).promoteUser(anyLong());
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(bookService);
        verifyNoMoreInteractions(mailService);
    }

    @Test
    @WithMockUser
    public void banUser() throws Exception {
        User user = new User();
        user.setEnabled(true);

        when(userService.findById(anyLong())).thenReturn(user);
        doNothing().when(mailService).sendAccountDisabledMail(any(User.class));
        doAnswer((InvocationOnMock invocation) -> {
            user.setEnabled(false);
            return null;
        }).when(userService).banUser(anyLong());

        mockMvc.perform(get("/user/ban/{id}", 1L))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/user/1?banned"))
                .andExpect(view().name("redirect:/user/1?banned"));

        assertFalse(user.isEnabled());
        verify(userService, times(1)).findById(anyLong());
        verify(userService, times(1)).banUser(anyLong());
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(bookService);
        verify(mailService, times(1)).sendAccountDisabledMail(any(User.class));
        verifyNoMoreInteractions(mailService);
    }

    @Test
    @WithMockUser
    public void exportCSVUsers() throws Exception {
        List<User> allUsers = new ArrayList<>();
        String expectedCsvContent = "ACCOUNT ENABLED,E-MAIL,FIRST NAME,LAST NAME,LOCATION,NICKNAME,USER ID\n" +
                "true,user@mail.com,First,Last,Location,Nick,1\n";
        allUsers.add(new User(1L, "First", "Last", "Nick", "user@mail.com", "Location", true));

        when(userService.getAllUsers()).thenReturn(allUsers);

        MvcResult result =
                mockMvc.perform(MockMvcRequestBuilders.get("/export-users"))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(expectedCsvContent, result.getResponse().getContentAsString());
        assertEquals("text/csv", result.getResponse().getContentType());
        verify(userService, times(1)).getAllUsers();
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(bookService);
        verifyNoMoreInteractions(mailService);
    }

    @Test
    @WithMockUser
    public void exportCSVBooks() throws Exception {
        List<Book> allBooks = new ArrayList<>();
        String expectedCsvContent = "BOOK AUTHOR,BOOK CONDITION,BOOK ID,BOOK LANGUAGE,BOOK NAME,BOOK RENT,BOOK SALE,BOOK TYPE,NUMBER OF PAGES,RENT PRICE,SELL PRICE\n" +
                "Author,Condition,1,Language,Book,true,true,Type,100,0,0\n";
        allBooks.add(new Book(1L, "Book", "Author", 100, "Type", "Language", "Condition", true, true, 0, 0));

        when(bookService.getAllBooks()).thenReturn(allBooks);

        MvcResult result =
                mockMvc.perform(MockMvcRequestBuilders.get("/export-books"))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        System.out.println(result.getResponse().getContentAsString());

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("text/csv", result.getResponse().getContentType());
        assertEquals(expectedCsvContent, result.getResponse().getContentAsString());
        verify(bookService, times(1)).getAllBooks();
        verifyNoMoreInteractions(bookService);
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(mailService);
    }
}