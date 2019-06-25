package ro.sci.bookwormscommunity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.sci.bookwormscommunity.mapper.BookMapper;
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.repositories.BookRepository;
import ro.sci.bookwormscommunity.web.dto.BookDto;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public void saveBook(BookDto bookDto) throws Exception {
        Book book = BookMapper.mapBookDtoToBook(bookDto);
        bookRepository.save(book);
    }

    @Override
    public void deleteBook(Book book) {
        this.bookRepository.delete(book);
    }

    @Override
    public void updateBook(long id, BookDto bookdto) throws Exception {
        Book book = bookRepository.getOne(id);
        book.setBookName(bookdto.getBookName());
        book.setAuthorName(bookdto.getAuthorName());
        book.setNumberOfPages(bookdto.getNumberOfPages());
        book.setCondition(bookdto.getCondition());
        book.setLanguage(bookdto.getLanguage());
        book.setBookRent(bookdto.isBookRent());
        book.setBookSale(bookdto.isBookSale());
        book.setRentPrice(bookdto.getRentPrice());
        book.setSellPrice(bookdto.getSellPrice());
        book.setDescription(bookdto.getDescription());
        book.setType(bookdto.getType());
        book.setImage(bookdto.returnUpdatePhoto(book.getImage()));
        book.setUser(bookdto.getUser());
        bookRepository.save(book);
    }

    @Override
    public List<Book> getUserBooks(long id) {
        return bookRepository.getUserBooks(id);
    }
}



