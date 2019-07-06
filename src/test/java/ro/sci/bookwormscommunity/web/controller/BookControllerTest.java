package ro.sci.bookwormscommunity.web.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.model.BookCondition;
import ro.sci.bookwormscommunity.model.Review;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.service.BookService;
import ro.sci.bookwormscommunity.service.ReviewService;
import ro.sci.bookwormscommunity.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
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
    public void anyRequestWhileNotAuthenticated_shouldReturn401Unauthorized() throws Exception {
        mockMvc.perform(get("/communityBooks"))
                .andExpect(status().isUnauthorized());

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

//    @Test
//    @WithMockUser
//    public void addBookWithInvalidAttributes_shouldReturnAddBookForm() throws Exception {
//        BookDto bookDto = new BookDto();
//        bookDto.setBookName("A");
//        bookDto.setAuthorName("A");
//        bookDto.setNumberOfPages(19);
//        bookDto.setType("");
//        bookDto.setLanguage("");
//        bookDto.setDescription("");
//        mockMvc.perform(post("/addBook")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .flashAttr("book", bookDto)
//        )
//                .andExpect(status().isOk())
//                .andExpect(view().name("addBook"))
//                .andExpect(model().attributeHasFieldErrors("book", "bookName"))
//                .andExpect(model().attributeHasFieldErrors("book", "authorName"))
//                .andExpect(model().attributeHasFieldErrors("book", "numberOfPages"))
//                .andExpect(model().attributeHasFieldErrors("book", "type"))
//                .andExpect(model().attributeHasFieldErrors("book", "language"))
//                .andExpect(model().attributeHasFieldErrors("book", "description"));
//
//        verifyNoMoreInteractions(bookService);
//        verifyNoMoreInteractions(reviewService);
//        verifyNoMoreInteractions(userService);
//    }

//    @Test
//    public void updateBook() {
//    }
}