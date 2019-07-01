package ro.sci.bookwormscommunity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ro.sci.bookwormscommunity.model.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Transactional
    @Query("SELECT b " +
            "FROM " +
            "Book AS b " +
            "WHERE " +
            "b.user.id = :userId")
    List<Book> getUserBooks(@Param("userId") long userId);

    @Transactional
    @Query(nativeQuery = true, value = "SELECT * FROM book ORDER BY rating DESC LIMIT 10")
    List<Book> findTop10RatedBooks();

    @Transactional
    @Query(nativeQuery = true, value = "SELECT * FROM book ORDER BY add_date LIMIT 10")
    List<Book> findLatest10AddedBooks();

    @Transactional
    @Query("SELECT b FROM Book AS b " +
            "ORDER BY " +
            "b.rating DESC")
    List<Book> findAllBooksOrderedByRating();

    @Transactional
    @Query("SELECT b from Book AS b " +
            "ORDER BY " +
            "b.addDate DESC")
    List<Book> findAllBooksOrderedByDate();

    @Transactional
    @Query("SELECT b from Book AS b " +
            "WHERE " +
            "b.bookRent = true")
    List<Book> findAllBooksForRent();

    @Transactional
    @Query("SELECT b FROM Book AS b " +
            "WHERE " +
            "b.bookSale = true")
    List<Book> findAllBooksForSale();
}
