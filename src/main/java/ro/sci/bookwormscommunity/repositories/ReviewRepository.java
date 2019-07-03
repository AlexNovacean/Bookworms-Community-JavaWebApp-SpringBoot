package ro.sci.bookwormscommunity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ro.sci.bookwormscommunity.model.Review;

import java.util.List;

/**
 * Repository of the {@link Review} class.
 * <p>
 * Handles the DB connection and all the operations regarding the DB (create, update, delete, etc.).
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @see JpaRepository
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Transactional
    @Query("SELECT r " +
            "FROM Review AS r " +
            "WHERE " +
            "r.book.id = :bookId " +
            "ORDER BY r.created")
    List<Review> getBookReviews(@Param("bookId") long bookId);
}
