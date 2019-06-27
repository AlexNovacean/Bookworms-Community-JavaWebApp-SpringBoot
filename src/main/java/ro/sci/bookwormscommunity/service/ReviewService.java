package ro.sci.bookwormscommunity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.sci.bookwormscommunity.model.Review;
import ro.sci.bookwormscommunity.repositories.ReviewRepository;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public void saveReview(Review review){
        reviewRepository.save(review);
    }

    public List<Review> getBookReviews(long bookId){
        return reviewRepository.getBookReviews(bookId);
    }
}
