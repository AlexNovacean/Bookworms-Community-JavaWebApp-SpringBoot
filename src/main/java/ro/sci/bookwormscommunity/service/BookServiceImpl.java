package ro.sci.bookwormscommunity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.sci.bookwormscommunity.mapper.BookMapper;
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.model.Review;
import ro.sci.bookwormscommunity.repositories.BookRepository;
import ro.sci.bookwormscommunity.repositories.ReviewRepository;
import ro.sci.bookwormscommunity.web.dto.BookDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReviewRepository reviewRepository;

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
    public void deleteBook(long id) {
        this.bookRepository.deleteById(id);
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

    @Override
    public void calculateRating(long id) {
        Book book = bookRepository.getOne(id);
        List<Review> reviews = reviewRepository.getBookReviews(id);

        int reviewSum = 0;

        for (Review r : reviews) {
            reviewSum += r.getRating();
        }

        book.setRating(reviewSum / reviews.size());
        bookRepository.save(book);
    }

    @Override
    public List<Book> getTop10RatedBooks() {
        return bookRepository.findTop10RatedBooks();
    }

    @Override
    public List<Book> getLatest10AddedBooks() {
        return bookRepository.findLatest10AddedBooks();
    }

    @Override
    public List<Book> getAllBooksOrderedByRating() {
        return bookRepository.findAllBooksOrderedByRating();
    }

    @Override
    public List<Book> getAllBooksOrderedByDate() {
        return bookRepository.findAllBooksOrderedByDate();
    }

    @Override
    public List<Book> getAllBooksForRent() {
        return bookRepository.findAllBooksForRent();
    }

    @Override
    public List<Book> getAllBooksForSale() {
        return bookRepository.findAllBooksForSale();
    }

    public List<Book> searchForAuthors(String searchPattern){
        List<Book> books = bookRepository.findAll();
        List<Book> searchResults = new ArrayList<>();

        for(Book book : books){
            if(searchPattern.equalsIgnoreCase(book.getAuthorName())){
                searchResults.add(book);
            }
        }
        return searchResults;
    }

    public List<Book> searchForBookName(String searchPattern){
        List<Book> books = bookRepository.findAll();
        List<Book> searchResults = new ArrayList<>();

        for(Book book : books){
            if(searchPattern.equalsIgnoreCase(book.getBookName())){
                searchResults.add(book);
            }
        }
        return searchResults;
    }

    public List<Book> searchForBookType(String searchPattern){
        List<Book> books = bookRepository.findAll();
        List<Book> searchResults = new ArrayList<>();

        for(Book book : books){
            if(searchPattern.equalsIgnoreCase(book.getType())){
                searchResults.add(book);
            }
        }
        return searchResults;
    }
}



