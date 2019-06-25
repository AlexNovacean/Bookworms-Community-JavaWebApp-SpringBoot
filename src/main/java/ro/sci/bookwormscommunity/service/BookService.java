package ro.sci.bookwormscommunity.service;

import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.web.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getAllBooks();

    Optional<Book> getBookById(Long id);

    void saveBook(BookDto bookDto) throws Exception;

    void updateBook(BookDto bookDto) throws Exception;

    void deleteBook(Book book);

    List<Book> getUserBooks(long id);
}
