package ro.sci.bookwormscommunity.service;

import org.springframework.transaction.annotation.Transactional;
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.web.dto.BookDto;

import java.io.IOException;
import java.util.List;

/**
 * Service containing all the required method for book transactions.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @author Raul
 */
public interface BookService {
    List<Book> getAllBooks();

    Book getBookById(Long id);

    void saveBook(BookDto bookDto) throws IOException;

    @Transactional
    void updateBook(long id, BookDto bookDto) throws IOException;

    void deleteBook(long id);

    List<Book> getUserBooks(long id);

    void calculateRating(long id);

    List<Book> getTop10RatedBooks();

    List<Book> getLatest10AddedBooks();

    List<Book> getAllBooksOrderedByRating();

    List<Book> getAllBooksOrderedByDate();

    List<Book> getAllBooksForRent();

    List<Book> getAllBooksForSale();

    List<Book> searchForAuthors(String searchPattern);

    List<Book> searchForBookName(String searchPattern);

    List<Book> searchForBookType(String searchPattern);
}
