package ro.sci.bookwormscommunity.service;

import org.springframework.transaction.annotation.Transactional;
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.web.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getAllBooks();

    Optional<Book> getBookById(Long id);

    void saveBook(BookDto bookDto) throws Exception;

    @Transactional
    void updateBook(long id, BookDto bookDto) throws Exception;

    void deleteBook(long id);

    List<Book> getUserBooks(long id);

    void calculateRating(long id);

    List<Book> getTopRatedBooks();

    List<Book> getLatestAddedBooks();
}
