package ro.sci.bookwormscommunity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.sci.bookwormscommunity.mapper.BookMapper;
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.model.Review;
import ro.sci.bookwormscommunity.repositories.BookRepository;
import ro.sci.bookwormscommunity.repositories.ReviewRepository;
import ro.sci.bookwormscommunity.web.dto.BookDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link BookService}.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @see BookService
 */
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    /**
     * Returns all books existing in the DB.
     *
     * @return all the existing books.
     */
    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Returns the book which is saved in DB with the provided id.
     *
     * @param id the identifier of the book to be returned
     * @return {@link Book} instance
     */
    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).get();
    }

    /**
     * Converts a {@link BookDto} to a {@link Book} object , with the help of the {@link BookMapper#mapBookDtoToBook(BookDto)} method and saves the book object to DB.
     *
     * @param bookDto provided {@link BookDto} instance
     * @throws IOException if an error occurs while mapping the BookDto image file to the Book photo byte array.
     */
    @Override
    public void saveBook(BookDto bookDto) throws IOException {
        Book book = BookMapper.mapBookDtoToBook(bookDto);
        bookRepository.save(book);
    }

    /**
     * Deletes from the DB the {@link Book} object identified by the provided id.
     *
     * @param id identifier for the book object to be deleted.
     */
    @Override
    public void deleteBook(long id) {
        this.bookRepository.deleteById(id);
    }

    /**
     * Updates the field or fields of the {@link Book} object identified with the provided id, with the field or field from the provided {@link BookDto}
     *
     * @param id identifier for the book which will be updated.
     * @param bookDto {@link BookDto} instance containing the new values for the book object that will be updated.
     * @throws IOException if an error occurs while mapping the BookDto image file to the Book photo byte array.
     */
    @Override
    public void updateBook(long id, BookDto bookDto) throws IOException {
        Book book = bookRepository.getOne(id);
        book.setBookName(bookDto.getBookName());
        book.setAuthorName(bookDto.getAuthorName());
        book.setNumberOfPages(bookDto.getNumberOfPages());
        book.setCondition(bookDto.getCondition());
        book.setLanguage(bookDto.getLanguage());
        book.setBookRent(bookDto.isBookRent());
        book.setBookSale(bookDto.isBookSale());
        book.setRentPrice(bookDto.getRentPrice());
        book.setSellPrice(bookDto.getSellPrice());
        book.setDescription(bookDto.getDescription());
        book.setType(bookDto.getType());
        book.setImage(bookDto.returnUpdatePhoto(book.getImage()));
        book.setUser(bookDto.getUser());
        bookRepository.save(book);
    }

    /**
     * Returns all the {@link Book} objects that were added by a specific {@link ro.sci.bookwormscommunity.model.User}
     *
     * @param id identifier of the user.
     * @return a list with all the books that were previously saved by the user.
     */
    @Override
    public List<Book> getUserBooks(long id) {
        return bookRepository.getUserBooks(id);
    }

    /**
     * Calculate and update a specific {@link Book} objects's rating field based on all the reviews submitted for that book.
     *
     * @param id identifier of the book for which the rating will be calculated.
     */
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

    /**
     * Returns 10 {@link Book} objects that have the highest registered rating in the DB.
     *
     * @return a list with 10 {@link Book} object with the highest rating from the DB.
     */
    @Override
    public List<Book> getTop10RatedBooks() {
        return bookRepository.findTop10RatedBooks();
    }

    /**
     * Returns 10 {@link Book} objects that were most recently added to the DB.
     *
     * @return a list with 10 {@link Book} objects that have the most recent Date in the DB.
     */
    @Override
    public List<Book> getLatest10AddedBooks() {
        return bookRepository.findLatest10AddedBooks();
    }

    /**
     * Returns all the {@link Book} objects from the DB ordered descending by their rating.
     *
     * @return a list with all the {@link Book} object in descending order of their rating.
     */
    @Override
    public List<Book> getAllBooksOrderedByRating() {
        return bookRepository.findAllBooksOrderedByRating();
    }

    /**
     * Returns all the {@link Book} objects from the DB ordered descending by their addDate.
     *
     * @return a list with all the {@link Book} objects in descending order of ther addDate (from the most recent to oldest).
     */
    @Override
    public List<Book> getAllBooksOrderedByDate() {
        return bookRepository.findAllBooksOrderedByDate();
    }

    /**
     * Returns all the {@link Book} objects that are posted as For Rent (the bookRent field has the value true).
     *
     * @return a list with all the {@link Book} objects that have the value of bookRent as true.
     */
    @Override
    public List<Book> getAllBooksForRent() {
        return bookRepository.findAllBooksForRent();
    }

    /**
     * Returns all the {@link Book} objects that are posted as For Sale (the bookSale field has the value true).
     *
     * @return a list with all the {@link Book} objects that have the value of bookSale as true.
     */
    @Override
    public List<Book> getAllBooksForSale() {
        return bookRepository.findAllBooksForSale();
    }

    /**
     * Returns a all the {@link Book} objects which Author Name matches , partially or fully, the searchPatter.
     * <p>
     * The matching between the Author Name and the searchPatter is calculated with the {@link BookServiceImpl#simpleTextSearch(char[], char[])} method.
     *
     * @param searchPattern provided String that will serve as the search pattern
     * @return a list with all the {@link Book} object which Author Name matches with the provided searchPattern, fully or partially.
     */
    @Override
    public List<Book> searchForAuthors(String searchPattern) {
        List<Book> books = bookRepository.findAll();
        List<Book> searchResults = new ArrayList<>();

        for (Book book : books) {
            if (simpleTextSearch(searchPattern.toLowerCase().toCharArray(), book.getAuthorName().toLowerCase().toCharArray()) >= 0) {
                searchResults.add(book);
            }
        }
        return searchResults;
    }

    /**
     * Returns a all the {@link Book} objects which Book Name matches , partially or fully, the searchPatter.
     * <p>
     * The matching between the Book Name and the searchPatter is calculated with the {@link BookServiceImpl#simpleTextSearch(char[], char[])} method.
     *
     * @param searchPattern provided String that will serve as the search pattern
     * @return a list with all the {@link Book} object which Book Name matches with the provided searchPattern, fully or partially.
     */
    @Override
    public List<Book> searchForBookName(String searchPattern) {
        List<Book> books = bookRepository.findAll();
        List<Book> searchResults = new ArrayList<>();

        for (Book book : books) {
            if (simpleTextSearch(searchPattern.toLowerCase().toCharArray(), book.getBookName().toLowerCase().toCharArray()) >= 0) {
                searchResults.add(book);
            }
        }
        return searchResults;
    }

    /**
     * Returns a all the {@link Book} objects which Type matches , partially or fully, the searchPatter.
     * <p>
     * The matching between the Type and the searchPatter is calculated with the {@link BookServiceImpl#simpleTextSearch(char[], char[])} method.
     *
     * @param searchPattern provided String that will serve as the search pattern
     * @return a list with all the {@link Book} object which Type matches with the provided searchPattern, fully or partially.
     */
    @Override
    public List<Book> searchForBookType(String searchPattern) {
        List<Book> books = bookRepository.findAll();
        List<Book> searchResults = new ArrayList<>();

        for (Book book : books) {
            if (simpleTextSearch(searchPattern.toLowerCase().toCharArray(), book.getType().toLowerCase().toCharArray()) >= 0) {
                searchResults.add(book);
            }
        }
        return searchResults;
    }

    private int simpleTextSearch(char[] pattern, char[] text) {
        int patternSize = pattern.length;
        int textSize = text.length;

        if (patternSize == 0) return -1;

        int i = 0;

        while ((i + patternSize) <= textSize) {
            int j = 0;
            while (text[i + j] == pattern[j]) {
                j += 1;
                if (j >= patternSize)
                    return i;
            }
            i += 1;
        }
        return -1;
    }
}



