package ro.sci.bookwormscommunity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.sci.bookwormscommunity.model.Review;
import ro.sci.bookwormscommunity.repositories.ReviewRepository;

import java.util.List;

/**
 * Implementation for the {@link ReviewService}.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @author Raul
 * @see ReviewService
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    /**
     * Saves the provided {@link Review} to the DB.
     *
     * @param review {@link Review} object which will be saved.
     */
    @Override
    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    /**
     * Returns all the reviews posted for a specific book.
     *
     * @param bookId book identifier for which the review will be retrieved.
     * @return a list of  all {@link Review} objects that have been saved for a specific book.
     */
    @Override
    public List<Review> getBookReviews(long bookId) {
        return reviewRepository.getBookReviews(bookId);
    }

    /**
     * Returns a specific {@link Review}.
     *
     * @param id the review identifier.
     * @return a {@link Review} instance.
     */
    @Override
    public Review getReviewById(long id) {
        return reviewRepository.findById(id).get();
    }

    /**
     * Updates an existing {@link Review} object.
     *
     * @param reviewId     the identifier of the review which will be updated.
     * @param editedReview {@link Review} object containing the changes that need to be updated.
     */
    @Override
    public void updateReview(long reviewId, Review editedReview) {
        Review toBeUpdated = reviewRepository.getOne(reviewId);
        toBeUpdated.setComment(editedReview.getComment());
        toBeUpdated.setEditedBy(editedReview.getEditedBy());
        toBeUpdated.setEdited(editedReview.isEdited());
        reviewRepository.save(toBeUpdated);
    }

    /**
     * Deletes from the DB a specific {@link Review}.
     *
     * @param id the identifier of the {@link Review} object which will be deleted.
     */
    @Override
    public void deleteReviewById(long id) {
        reviewRepository.deleteById(id);
    }
}
