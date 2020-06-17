package ro.sci.bookwormscommunity.service;

import org.springframework.transaction.annotation.Transactional;
import ro.sci.bookwormscommunity.model.Review;

import java.util.List;

/**
 * Service that handles the reviews of the books.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @author Raul
 */
public interface ReviewService {
    void saveReview(Review review);

    List<Review> getBookReviews(long bookId);

    Review getReviewById(long id);

    @Transactional
    void updateReview(long reviewId, Review editedReview);

    void deleteReviewById(long id);
}
