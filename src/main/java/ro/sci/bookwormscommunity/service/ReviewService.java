package ro.sci.bookwormscommunity.service;

import org.springframework.transaction.annotation.Transactional;
import ro.sci.bookwormscommunity.model.Review;

import java.util.List;

public interface ReviewService {
    void saveReview(Review review);

    List<Review> getBookReviews(long bookId);

    Review getReviewById(long id);

    @Transactional
    void updateReview(long id, Review review);

    void deleteReviewById(long id);
}
