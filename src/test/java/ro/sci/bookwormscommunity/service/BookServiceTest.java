package ro.sci.bookwormscommunity.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.repositories.BookRepository;
import ro.sci.bookwormscommunity.repositories.ReviewRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService = new BookServiceImpl();

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllBooks() throws Exception {

        List<Book> books = new ArrayList<>(Arrays.asList(new Book(), new Book()));
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findAll();

    }
}



