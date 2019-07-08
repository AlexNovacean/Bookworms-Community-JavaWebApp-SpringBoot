package ro.sci.bookwormscommunity.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.test.context.junit4.SpringRunner;
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.model.Review;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.repositories.BookRepository;
import ro.sci.bookwormscommunity.repositories.ReviewRepository;
import ro.sci.bookwormscommunity.web.dto.BookDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
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

    @Test
    public void getBookById() {
        Book book = new Book();
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        Book found = bookService.getBookById(1L);

        assertNotNull(found);
        assertEquals(book, found);
        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    public void saveBook() throws Exception {
        User user = new User(1L, "test@mail.com");
        BookDto bookDto = new BookDto("Name", "Author", 50, "Type", "Language", "Description", true, true, 0, 0, user);
        List<Book> books = new ArrayList<>();

        doAnswer((InvocationOnMock invocation) -> {
            Object[] arguments = invocation.getArguments();

            Book book = (Book) arguments[0];
            books.add(book);
            return null;
        }).when(bookRepository).save(any(Book.class));

        bookService.saveBook(bookDto);

        assertNotNull(books);
        assertEquals(1, books.size());
        assertTrue(books.stream().anyMatch(book -> book.getBookName().equals("Name")));
    }

    @Test
    public void updateBook() throws Exception {
        BookDto bookDto = new BookDto("Name", "Author", 50, "Type", "Language", "Description", true, true, 0, 0, new User(1L, "test@mail.com"));
        Book book = new Book(2L);

        when(bookRepository.getOne(anyLong())).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        bookService.updateBook(2L, bookDto);

        assertEquals(bookDto.getBookName(), book.getBookName());
        verify(bookRepository, times(1)).getOne(anyLong());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void deleteBook() throws Exception {
        Book book = new Book(1L);
        List<Book> books = new ArrayList<>();
        books.add(book);

        doAnswer((InvocationOnMock invocation) -> {
            books.remove(book);
            return null;
        }).when(bookRepository).deleteById(anyLong());

        bookService.deleteBook(1L);

        assertEquals(0, books.size());
        assertFalse(books.contains(book));
        verify(bookRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void getUserBooks() {
        List<Book> books = Arrays.asList(new Book(), new Book());
        when(bookRepository.getUserBooks(anyLong())).thenReturn(books);

        List<Book> foundBookList = bookService.getUserBooks(1L);

        assertNotNull(foundBookList);
        assertEquals(books, foundBookList);
    }

    @Test
    public void calculateRating() {
        List<Review> reviews = Arrays.asList(new Review(5), new Review(4));
        Book book = new Book();

        when(bookRepository.getOne(anyLong())).thenReturn(book);
        when(reviewRepository.getBookReviews(anyLong())).thenReturn(reviews);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        bookService.calculateRating(1L);

        assertEquals(4, book.getRating());
        verify(bookRepository, times(1)).getOne(anyLong());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(reviewRepository, times(1)).getBookReviews(anyLong());
    }

    @Test
    public void getTop10RatedBooks() {
        List<Book> books = Arrays.asList(new Book(), new Book());
        when(bookRepository.findTop10RatedBooks()).thenReturn(books);

        List<Book> foundBookList = bookService.getTop10RatedBooks();

        assertNotNull(foundBookList);
        assertEquals(books, foundBookList);
        verify(bookRepository, times(1)).findTop10RatedBooks();
    }

    @Test
    public void getLatest10AddedBooks() {
        List<Book> books = Arrays.asList(new Book(), new Book());
        when(bookRepository.findLatest10AddedBooks()).thenReturn(books);

        List<Book> foundBookList = bookService.getLatest10AddedBooks();

        assertNotNull(foundBookList);
        assertEquals(books, foundBookList);
        verify(bookRepository, times(1)).findLatest10AddedBooks();
    }

    @Test
    public void getAllBooksOrderedByRating() {
        List<Book> books = Arrays.asList(new Book(), new Book());
        when(bookRepository.findAllBooksOrderedByRating()).thenReturn(books);

        List<Book> foundBookList = bookService.getAllBooksOrderedByRating();

        assertNotNull(foundBookList);
        assertEquals(books, foundBookList);
        verify(bookRepository, times(1)).findAllBooksOrderedByRating();
    }

    @Test
    public void getAllBooksOrderedByDate() {
        List<Book> books = Arrays.asList(new Book(), new Book());
        when(bookRepository.findAllBooksOrderedByDate()).thenReturn(books);

        List<Book> foundBookList = bookService.getAllBooksOrderedByDate();

        assertNotNull(foundBookList);
        assertEquals(books, foundBookList);
        verify(bookRepository, times(1)).findAllBooksOrderedByDate();
    }

    @Test
    public void getAllBooksForRent() {
        List<Book> books = Arrays.asList(new Book(), new Book());
        when(bookRepository.findAllBooksForRent()).thenReturn(books);

        List<Book> foundBookList = bookService.getAllBooksForRent();

        assertNotNull(foundBookList);
        assertEquals(books, foundBookList);
        verify(bookRepository, times(1)).findAllBooksForRent();
    }

    @Test
    public void getAllBooksForSale() {
        List<Book> books = Arrays.asList(new Book(), new Book());
        when(bookRepository.findAllBooksForSale()).thenReturn(books);

        List<Book> foundBookList = bookService.getAllBooksForSale();

        assertNotNull(foundBookList);
        assertEquals(books, foundBookList);
        verify(bookRepository, times(1)).findAllBooksForSale();
    }

    @Test
    public void searchForAuthors() {
        List<Book> books = Arrays.asList(new Book("Book", "Author", "Type"), new Book("Bookk", "Authorr", "Typee"), new Book("Carte", "Autor", "Tip"));

        when(bookRepository.findAll()).thenReturn(books);

        List<Book> searchResult = bookService.searchForAuthors("Auth");

        assertNotNull(searchResult);
        assertEquals(2, searchResult.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void searchForBookName() {
        List<Book> books = Arrays.asList(new Book("Book", "Author", "Type"), new Book("Bookk", "Authorr", "Typee"), new Book("Carte", "Autor", "Tip"));

        when(bookRepository.findAll()).thenReturn(books);

        List<Book> searchResult = bookService.searchForBookName("Boo");

        assertNotNull(searchResult);
        assertEquals(2, searchResult.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void searchForBookType() {
        List<Book> books = Arrays.asList(new Book("Book", "Author", "Type"), new Book("Bookk", "Authorr", "Typee"), new Book("Carte", "Autor", "Tip"));

        when(bookRepository.findAll()).thenReturn(books);

        List<Book> searchResult = bookService.searchForBookType("Ty");

        assertNotNull(searchResult);
        assertEquals(2, searchResult.size());
        verify(bookRepository, times(1)).findAll();
    }
}