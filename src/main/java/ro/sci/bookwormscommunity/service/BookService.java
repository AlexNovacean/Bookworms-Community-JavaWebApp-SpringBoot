package ro.sci.bookwormscommunity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.repositories.BookRepository;
import ro.sci.bookwormscommunity.web.dto.BookDto;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public void save(BookDto bookdto) throws Exception {
        Book book = new Book();
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
        book.setImage(bookdto.returnPhoto().getBytes());
        book.setUser(bookdto.getUser());
        bookRepository.save(book);
    }
}



