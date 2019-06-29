package ro.sci.bookwormscommunity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.sci.bookwormscommunity.model.Review;
import ro.sci.bookwormscommunity.repositories.ReviewRepository;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public void saveReview(Review review){
        reviewRepository.save(review);
    }

    @Override
    public List<Review> getBookReviews(long bookId){
        return reviewRepository.getBookReviews(bookId);
    }

    @Override
    public Review getReviewById(long id){
        return reviewRepository.findById(id).get();
    }

    @Override
    public void updateReview(long id, Review review){
        Review toBeUpdated = reviewRepository.getOne(id);
        toBeUpdated.setComment(review.getComment());
        toBeUpdated.setEditedBy(review.getEditedBy());
        toBeUpdated.setEdited(review.isEdited());
        reviewRepository.save(toBeUpdated);
    }

    @Override
    public void deleteReviewById(long id){
        reviewRepository.deleteById(id);
    }
}
