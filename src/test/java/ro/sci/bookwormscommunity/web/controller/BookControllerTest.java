package ro.sci.bookwormscommunity.web.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ro.sci.bookwormscommunity.config.WebSecurityConfig;
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.model.BookCondition;
import ro.sci.bookwormscommunity.model.Review;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.service.BookService;
import ro.sci.bookwormscommunity.service.ReviewService;
import ro.sci.bookwormscommunity.service.UserService;
import ro.sci.bookwormscommunity.web.dto.BookDto;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
@Import(WebSecurityConfig.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private UserService userService;

    @MockBean
    private ReviewService reviewService;

    @Test
    @WithAnonymousUser
    public void anyRequestWhileNotAuthenticated_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/communityBooks"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));

        verifyNoMoreInteractions(bookService);
        verifyNoMoreInteractions(reviewService);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser
    public void showBooks() throws Exception {
        List<Book> bookList = new ArrayList<>(Arrays.asList(new Book(1), new Book(2), new Book(3)));

        when(bookService.getAllBooks()).thenReturn(bookList);

        mockMvc.perform(get("/communityBooks"))
                .andExpect(status().isOk())
                .andExpect(view().name("communityBooks"))
                .andExpect(model().attribute("books", hasSize(3)))
                .andExpect(model().attribute("books", bookList));

        verify(bookService, times(1)).getAllBooks();
        verifyNoMoreInteractions(bookService);

    }

    @Test
    @WithMockUser
    public void bookDetailsForm() throws Exception {
        List<Review> reviews = new ArrayList<>(Arrays.asList(new Review(1), new Review(2), new Review(3)));
        Book book = new Book(1L);
        book.setUser(new User(1, "test@mail.com"));

        when(bookService.getBookById(anyLong())).thenReturn(book);
        when(reviewService.getBookReviews(anyLong())).thenReturn(reviews);

        mockMvc.perform(get("/bookDetails/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("bookDetails"))
                .andExpect(model().attribute("book", book))
                .andExpect(model().attribute("book", hasProperty("id", is(1L))))
                .andExpect(model().attribute("reviews", hasSize(3)))
                .andExpect(model().attribute("reviews", reviews));

        verify(bookService, times(1)).getBookById(anyLong());
        verifyNoMoreInteractions(bookService);
        verify(reviewService, times(1)).getBookReviews(anyLong());
        verifyNoMoreInteractions(reviewService);

    }

    @Test
    @WithMockUser
    public void bookDetailsForm_bookNotFound() throws Exception {
        when(bookService.getBookById(anyLong())).thenThrow(new NoSuchElementException(""));

        mockMvc.perform(get("/bookDetails/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error-404"));

        verify(bookService, times(1)).getBookById(anyLong());
        verifyNoMoreInteractions(bookService);
        verifyNoMoreInteractions(reviewService);
    }

    @Test
    @WithMockUser
    public void showSaveBookForm() throws Exception {
        mockMvc.perform(get("/addBook"))
                .andExpect(status().isOk())
                .andExpect(view().name("addBook"))
                .andExpect(model().attribute("conditions", BookCondition.values()));

        verifyNoMoreInteractions(bookService);
        verifyNoMoreInteractions(reviewService);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser
    public void deleteBookForm() throws Exception {
        doNothing().when(bookService).deleteBook(anyLong());

        mockMvc.perform(get("/deleteBook/{id}", 1L))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/communityBooks?deletedBook"))
                .andExpect(view().name("redirect:/communityBooks?deletedBook"));

        verify(bookService, times(1)).deleteBook(anyLong());
        verifyNoMoreInteractions(bookService);
        verifyNoMoreInteractions(reviewService);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser
    public void showUpdateForm() throws Exception {
        Book book = new Book(1L, "name", "author", 100, "type", "language", "description", BookCondition.AS_NEW.getCondition(), true, true, 0, 0, new User(1, "test@mail.com"));
        when(bookService.getBookById(anyLong())).thenReturn(book);

        mockMvc.perform(get("/editBook/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("updateBook"))
                .andExpect(model().attribute("conditions", BookCondition.values()))
                .andExpect(model().attribute("bookDto", hasProperty("id", is(1L))))
                .andExpect(model().attribute("bookDto", hasProperty("bookName", is("name"))))
                .andExpect(model().attribute("bookDto", hasProperty("authorName", is("author"))));

        verify(bookService, times(1)).getBookById(anyLong());
        verifyNoMoreInteractions(bookService);
        verifyNoMoreInteractions(reviewService);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser
    public void showBooksByRating() throws Exception {
        List<Book> bookList = new ArrayList<>(Arrays.asList(new Book(1), new Book(2), new Book(3)));

        when(bookService.getAllBooksOrderedByRating()).thenReturn(bookList);

        mockMvc.perform(get("/communityBooks/byRating"))
                .andExpect(status().isOk())
                .andExpect(view().name("communityBooks"))
                .andExpect(model().attribute("books", hasSize(3)))
                .andExpect(model().attribute("books", bookList));

        verify(bookService, times(1)).getAllBooksOrderedByRating();
        verifyNoMoreInteractions(bookService);
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(reviewService);
    }

    @Test
    @WithMockUser
    public void showBooksByDate() throws Exception {
        List<Book> bookList = new ArrayList<>(Arrays.asList(new Book(1), new Book(2), new Book(3)));

        when(bookService.getAllBooksOrderedByDate()).thenReturn(bookList);

        mockMvc.perform(get("/communityBooks/byDate"))
                .andExpect(status().isOk())
                .andExpect(view().name("communityBooks"))
                .andExpect(model().attribute("books", hasSize(3)))
                .andExpect(model().attribute("books", bookList));

        verify(bookService, times(1)).getAllBooksOrderedByDate();
        verifyNoMoreInteractions(bookService);
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(reviewService);
    }

    @Test
    @WithMockUser
    public void showBooksForRent() throws Exception {
        List<Book> bookList = new ArrayList<>(Arrays.asList(new Book(1), new Book(2), new Book(3)));

        when(bookService.getAllBooksForRent()).thenReturn(bookList);

        mockMvc.perform(get("/communityBooks/forRent"))
                .andExpect(status().isOk())
                .andExpect(view().name("communityBooks"))
                .andExpect(model().attribute("books", hasSize(3)))
                .andExpect(model().attribute("books", bookList));

        verify(bookService, times(1)).getAllBooksForRent();
        verifyNoMoreInteractions(bookService);
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(reviewService);
    }

    @Test
    @WithMockUser
    public void showBooksForSale() throws Exception {
        List<Book> bookList = new ArrayList<>(Arrays.asList(new Book(1), new Book(2), new Book(3)));

        when(bookService.getAllBooksForSale()).thenReturn(bookList);

        mockMvc.perform(get("/communityBooks/forSale"))
                .andExpect(status().isOk())
                .andExpect(view().name("communityBooks"))
                .andExpect(model().attribute("books", hasSize(3)))
                .andExpect(model().attribute("books", bookList));

        verify(bookService, times(1)).getAllBooksForSale();
        verifyNoMoreInteractions(bookService);
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(reviewService);
    }

    @Test
    @WithMockUser
    public void searchForBooks() throws Exception {
        List<Book> bookList = new ArrayList<>(Arrays.asList(new Book(1), new Book(2), new Book(3)));

        when(bookService.searchForAuthors(null)).thenReturn(bookList);
        when(bookService.searchForBookName(null)).thenReturn(bookList);
        when(bookService.searchForBookType(null)).thenReturn(bookList);

        mockMvc.perform(get("/searchBooks"))
                .andExpect(status().isOk())
                .andExpect(view().name("searchResults"))
                .andExpect(model().attribute("booksOfAuthors", hasSize(3)))
                .andExpect(model().attribute("booksOfAuthors", bookList))
                .andExpect(model().attribute("booksWithName", hasSize(3)))
                .andExpect(model().attribute("booksWithName", bookList))
                .andExpect(model().attribute("booksOfType", hasSize(3)))
                .andExpect(model().attribute("booksOfType", bookList));

        verify(bookService, times(1)).searchForAuthors(null);
        verify(bookService, times(1)).searchForBookName(null);
        verify(bookService, times(1)).searchForBookType(null);
        verifyNoMoreInteractions(bookService);
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(reviewService);
    }

    @Test
    @WithMockUser
    public void addBookWithInvalidAttributes_shouldReturnAddBookForm() throws Exception {
        User user = new User(1, "test@mail.com");
        MockMultipartFile emptyFile = new MockMultipartFile("photo", new byte[0]);

        when(userService.findByEmail(anyString())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/addBook")
                .file(emptyFile)
                .with(csrf())
                .param("bookName", "A")
                .param("authorName", "A")
                .param("numberOfPages", "19")
                .param("type", "")
                .param("language", "")
                .param("description", "")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("addBook"))
                .andExpect(model().attributeHasFieldErrors("book", "bookName"))
                .andExpect(model().attributeHasFieldErrors("book", "authorName"))
                .andExpect(model().attributeHasFieldErrors("book", "numberOfPages"))
                .andExpect(model().attributeHasFieldErrors("book", "type"))
                .andExpect(model().attributeHasFieldErrors("book", "language"))
                .andExpect(model().attributeHasFieldErrors("book", "description"))
                .andExpect(model().attribute("conditions", BookCondition.values()));

        verify(userService, times(1)).findByEmail(anyString());
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(bookService);
        verifyNoMoreInteractions(reviewService);
    }

    @Test
    @WithMockUser
    public void addBookWithValidAttributes_shouldSaveBook() throws Exception {
        User user = new User(1, "test@mail.com");
        Path path = Paths.get("src/main/resources/static/images/book.png");
        MockMultipartFile file = new MockMultipartFile("photo", "book.png", "image/png", new ByteArrayInputStream(Files.readAllBytes(path)));
        List<BookDto> savedBooks = new ArrayList<>();

        when(userService.findByEmail(anyString())).thenReturn(user);
        doAnswer((InvocationOnMock invocation) -> {
            Object[] arguments = invocation.getArguments();

            BookDto bookDto = (BookDto) arguments[0];
            savedBooks.add(bookDto);
            return null;
        }).when(bookService).saveBook(any(BookDto.class));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/addBook")
                .file(file)
                .with(csrf())
                .param("bookName", "Book")
                .param("authorName", "Author")
                .param("numberOfPages", "20")
                .param("type", "Type")
                .param("language", "Language")
                .param("description", "Description")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/addBook?success"))
                .andExpect(view().name("redirect:/addBook?success"));

        verify(userService, times(1)).findByEmail(anyString());
        verifyNoMoreInteractions(userService);
        verify(bookService, times(1)).saveBook(any(BookDto.class));
        verifyNoMoreInteractions(bookService);

        assertNotNull(savedBooks);
        assertEquals("Book", savedBooks.get(0).getBookName());
        assertEquals("Author", savedBooks.get(0).getAuthorName());
        assertEquals("Type", savedBooks.get(0).getType());
        assertEquals("20", savedBooks.get(0).getNumberOfPages().toString());
        assertEquals("Language", savedBooks.get(0).getLanguage());
        assertEquals("Description", savedBooks.get(0).getDescription());
        assertFalse(savedBooks.get(0).isBookRent());
        assertFalse(savedBooks.get(0).isBookSale());
        assertEquals("0", savedBooks.get(0).getRentPrice().toString());
        assertEquals("0", savedBooks.get(0).getSellPrice().toString());
        assertEquals(file, savedBooks.get(0).getPhoto());
    }

    @Test
    @WithMockUser
    public void updateBook() throws Exception {
        User user = new User(1, "test@mail.com");
        Book book = new Book(1L);
        MockMultipartFile emptyFile = new MockMultipartFile("photo", new byte[0]);

        when(userService.findByEmail(anyString())).thenReturn(user);
        when(bookService.getBookById(anyLong())).thenReturn(book);
        doAnswer((InvocationOnMock invocation) -> {
            Object[] arguments = invocation.getArguments();
            BookDto bookDto = (BookDto) arguments[1];
            book.setBookName(bookDto.getBookName());
            book.setAuthorName(bookDto.getAuthorName());
            book.setType(bookDto.getType());
            book.setLanguage(bookDto.getLanguage());
            book.setDescription(bookDto.getDescription());
            book.setNumberOfPages(bookDto.getNumberOfPages());
            return null;
        }).when(bookService).updateBook(anyLong(), any(BookDto.class));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/updateBook/{id}", 1L)
                .file(emptyFile)
                .with(csrf())
                .param("bookName", "Book")
                .param("authorName", "Author")
                .param("numberOfPages", "20")
                .param("type", "Type")
                .param("language", "Language")
                .param("description", "Description")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/bookDetails/1"))
                .andExpect(view().name("redirect:/bookDetails/1"));

        verify(userService, times(1)).findByEmail(anyString());
        verifyNoMoreInteractions(userService);
        verify(bookService, times(1)).getBookById(anyLong());
        verify(bookService, times(1)).updateBook(anyLong(), any(BookDto.class));
        verifyNoMoreInteractions(bookService);

        assertEquals("Book", book.getBookName());
        assertEquals("Author", book.getAuthorName());
        assertEquals("Type", book.getType());
        assertEquals(20, book.getNumberOfPages());
        assertEquals("Language", book.getLanguage());
        assertEquals("Description", book.getDescription());
    }
}