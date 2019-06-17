package ro.sci.bookwormscommunity.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.sci.bookwormscommunity.web.model.Book;
import ro.sci.bookwormscommunity.web.repository.BookRepository;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private static final Logger log = LoggerFactory.getLogger(BookService.class);
    @Autowired
    BookRepository bookRepository;

    public List<Book> findAll(){
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id){
        return bookRepository.findById(id);
    }

    public void save(Book book){
        bookRepository.save(book);
    }
}



