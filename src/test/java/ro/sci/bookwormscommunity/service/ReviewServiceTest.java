package ro.sci.bookwormscommunity.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.test.context.junit4.SpringRunner;
import ro.sci.bookwormscommunity.model.Review;
import ro.sci.bookwormscommunity.repositories.ReviewRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService = new ReviewServiceImpl();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveReview() {
        List<Review> reviews = new ArrayList<>();

        doAnswer((InvocationOnMock invocation) -> {
            Object[] arguments = invocation.getArguments();
            if (arguments != null && arguments.length > 0 && arguments[0] != null) {

                Review review = (Review) arguments[0];
                reviews.add(review);
            }
            return null;
        }).when(reviewRepository).save(any(Review.class));

        Review review = new Review();

        reviewService.saveReview(review);

        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        verify(reviewRepository, times(1)).save(any());
        assertEquals(review, reviews.get(0));
    }

    @Test
    public void getBookReviews() throws Exception {
        when(reviewRepository.getBookReviews(anyLong())).thenReturn(new ArrayList<>(Arrays.asList(new Review(1), new Review(2), new Review(3))));

        List<Review> reviews = reviewService.getBookReviews(1);

        assertNotNull(reviews);
        assertEquals(3, reviews.size());
        verify(reviewRepository, times(1)).getBookReviews(anyLong());
    }

    @Test
    public void getReviewById() throws Exception {

        Optional<Review> review = Optional.of(new Review(1));
        when(reviewRepository.findById(1L)).thenReturn(review);

        Review result = reviewService.getReviewById(1);

        assertNotNull(result);
        assertEquals(review.get(), result);
    }

    @Test
    public void updateReview() throws IOException {
        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review(1, "Comment", false, null));

        when(reviewRepository.getOne(anyLong())).thenReturn(reviews.get(0));

        doAnswer((InvocationOnMock invocation) -> {
            Object[] arguments = invocation.getArguments();
            Review editedReview = (Review) arguments[0];
            reviews.set(0, editedReview);
            return null;
        }).when(reviewRepository).save(any(Review.class));

        Review forEditing = new Review(1, "Edited Comment", true, "Editor");

        reviewService.updateReview(1, forEditing);

        assertEquals(forEditing.getId(), reviews.get(0).getId());
        assertEquals(forEditing.getComment(), reviews.get(0).getComment());
        assertEquals(forEditing.isEdited(), reviews.get(0).isEdited());
        assertEquals(forEditing.getEditedBy(), reviews.get(0).getEditedBy());
        verify(reviewRepository, times(1)).getOne(anyLong());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    public void deleteReviewById() throws Exception {
        List<Review> reviews = new ArrayList<>(Arrays.asList(new Review(1), new Review(2), new Review(3)));

        doAnswer((InvocationOnMock invocation) -> {
            Object[] arguments = invocation.getArguments();

            long reviewId = (Long) arguments[0];
            reviews.removeIf(r -> r.getId() == reviewId);

            return null;
        }).when(reviewRepository).deleteById(anyLong());

        reviewService.deleteReviewById(1);

        assertEquals(2, reviews.size());
        assertFalse(reviews.stream().anyMatch(r -> r.getId() == 1));
    }
}