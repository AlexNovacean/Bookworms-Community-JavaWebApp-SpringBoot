package ro.sci.bookwormscommunity.web.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ro.sci.bookwormscommunity.config.WebSecurityConfig;
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.model.Review;
import ro.sci.bookwormscommunity.model.Role;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.service.BookService;
import ro.sci.bookwormscommunity.service.ReviewService;
import ro.sci.bookwormscommunity.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ReviewController.class)
@Import(WebSecurityConfig.class)
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private BookService bookService;

    @Test
    @WithMockUser
    public void postInvalidReview_shouldReturnValidationError() throws Exception {
        User user = new User(1L, "test@mail.com");
        Book book = new Book(1L);
        book.setUser(user);
        List<Review> reviews = new ArrayList<>();

        when(userService.findByEmail(anyString())).thenReturn(user);
        when(bookService.getBookById(anyLong())).thenReturn(book);
        when(reviewService.getBookReviews(anyLong())).thenReturn(reviews);

        mockMvc.perform(post("/bookDetails/{id}", 1L)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("rating", "0")
                .param("comment", "")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("bookDetails"))
                .andExpect(model().attribute("book", book))
                .andExpect(model().attribute("reviews", reviews))
                .andExpect(model().attributeHasFieldErrors("review", "rating"))
                .andExpect(model().attributeHasFieldErrors("review", "comment"));

        verify(userService, times(1)).findByEmail(anyString());
        verifyNoMoreInteractions(userService);
        verify(bookService, times(1)).getBookById(anyLong());
        verifyNoMoreInteractions(bookService);
        verify(reviewService, times(1)).getBookReviews(anyLong());
        verifyNoMoreInteractions(reviewService);
    }

    @Test
    @WithMockUser
    public void postValidReview_shouldSaveReview() throws Exception {
        User user = new User(1L, "test@mail.com");
        user.setNickname("nickname");
        Book book = new Book(1L);
        book.setUser(user);
        List<Review> reviews = new ArrayList<>();

        when(userService.findByEmail(anyString())).thenReturn(user);
        when(bookService.getBookById(anyLong())).thenReturn(book);
        doAnswer((InvocationOnMock invocation) -> {
            Object[] arguments = invocation.getArguments();
            Review review = (Review) arguments[0];
            reviews.add(review);
            return null;
        }).when(reviewService).saveReview(any(Review.class));
        doAnswer((InvocationOnMock invocation) -> {
            book.setRating(reviews.get(0).getRating());
            return null;
        }).when(bookService).calculateRating(anyLong());

        mockMvc.perform(post("/bookDetails/{id}", 1L)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("rating", "5")
                .param("comment", "Review Comment")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/bookDetails/1#allreviews"))
                .andExpect(view().name("redirect:/bookDetails/1#allreviews"));

        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        assertEquals(5, reviews.get(0).getRating());
        assertEquals("Review Comment", reviews.get(0).getComment());
        assertEquals(5, book.getRating());
        verify(userService, times(1)).findByEmail(anyString());
        verifyNoMoreInteractions(userService);
        verify(bookService, times(1)).getBookById(anyLong());
        verify(bookService, times(1)).calculateRating(anyLong());
        verifyNoMoreInteractions(bookService);
        verify(reviewService, times(1)).saveReview(any(Review.class));
        verifyNoMoreInteractions(reviewService);
    }

    @Test
    @WithMockUser
    public void managePosts() throws Exception {
        Review review = new Review(1L);
        review.setBook(new Book(1L));
        when(reviewService.getReviewById(anyLong())).thenReturn(review);

        mockMvc.perform(get("/managePosts/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("managePosts"))
                .andExpect(model().attribute("review", review));

        verify(reviewService, times(1)).getReviewById(anyLong());
        verifyNoMoreInteractions(reviewService);
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    @WithMockUser
    public void editPost() throws Exception {
        User user = new User(1L, "test@mail.com");
        user.setRoles(Collections.singletonList(new Role("ROLE_MODERATOR")));
        Review review = new Review(1L, "Initial Comment", false, null);
        review.setBook(new Book(1L));
        List<Review> reviews = new ArrayList<>();
        reviews.add(review);

        when(userService.findByEmail(anyString())).thenReturn(user);
        when(reviewService.getReviewById(anyLong())).thenReturn(review);
        doAnswer((InvocationOnMock invocation) -> {
            Object[] arguments = invocation.getArguments();

            Review editedReview = (Review) arguments[1];
            reviews.set(0, editedReview);
            return null;
        }).when(reviewService).updateReview(anyLong(), any(Review.class));

        mockMvc.perform(post("/managePosts/{id}", 1L)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("comment", "Edited Comment")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/bookDetails/1"))
                .andExpect(view().name("redirect:/bookDetails/1"));

        assertEquals(1, reviews.size());
        assertEquals("Edited Comment", reviews.get(0).getComment());
        assertTrue(reviews.get(0).isEdited());
        assertEquals("Moderator", reviews.get(0).getEditedBy());
        verify(reviewService, times(1)).updateReview(anyLong(), any(Review.class));
        verify(reviewService, times(1)).getReviewById(anyLong());
        verifyNoMoreInteractions(reviewService);
        verify(userService, times(1)).findByEmail(anyString());
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    @WithMockUser
    public void deletePost() throws Exception {
        Review review = new Review(1L);
        review.setBook(new Book(1L));
        List<Review> reviews = new ArrayList<>();
        reviews.add(review);

        when(reviewService.getReviewById(anyLong())).thenReturn(review);
        doAnswer((InvocationOnMock invocation) -> {
            reviews.remove(review);
            return null;
        }).when(reviewService).deleteReviewById(anyLong());

        mockMvc.perform(get("/managePosts/delete/{id}", 1L))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/bookDetails/1"))
                .andExpect(view().name("redirect:/bookDetails/1"));

        assertEquals(0, reviews.size());
        assertFalse(reviews.contains(review));
        verify(reviewService, times(1)).getReviewById(anyLong());
        verify(reviewService, times(1)).deleteReviewById(anyLong());
        verifyNoMoreInteractions(reviewService);
        verifyNoMoreInteractions(bookService);
        verifyNoMoreInteractions(userService);
    }
}