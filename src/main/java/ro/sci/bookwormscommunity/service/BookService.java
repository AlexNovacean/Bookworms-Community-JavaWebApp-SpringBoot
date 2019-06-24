package ro.sci.bookwormscommunity.service;

import ro.sci.bookwormscommunity.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookService {
    public abstract List<Book> getAllBooks();
    public abstract Optional<Book> getBookById(Long Id);
    public abstract void saveBook(Book book);
    public abstract void updateBook(Book book);
    public abstract void deleteBook(Book book);
}
