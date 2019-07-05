package ro.sci.bookwormscommunity.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ro.sci.bookwormscommunity.model.*;
import ro.sci.bookwormscommunity.service.BookService;
import ro.sci.bookwormscommunity.service.ReviewService;
import ro.sci.bookwormscommunity.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class ReviewController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private BookService bookService;

    /**
     * Initializes the review {@link ModelAttribute} with a new instance of {@link Review}.
     *
     * @return {@link Review} instance.
     */
    @ModelAttribute("review")
    public Review review() {
        return new Review();
    }

    /**
     * Handles the {@link RequestMethod#POST} method used to post a review for a book.
     *
     * @param searchWord container for the user search input.
     * @param id         identifier for the book that will receive the review.
     * @param review     {@link Review} object containing all the information for the review that will be posted.
     * @param result     {@link BindingResult} instance that stores the validation errors of the {@link Review} object's fields.
     * @param principal  {@link Principal} object which stores the currently logged in user.
     * @param model      {@link Model} used to add attributes that requires to be returned to the View.
     * @return the name of the view where the review are posted
     */
    @PostMapping("/bookDetails/{id}")
    public String postReview(@ModelAttribute("searchWord") Word searchWord, @PathVariable("id") long id, @ModelAttribute("review") @Valid Review review, BindingResult result, Principal principal, Model model) {
        User user = userService.findByEmail(principal.getName());
        if (result.hasErrors()) {
            model.addAttribute("book", bookService.getBookById(id));
            model.addAttribute("reviews", reviewService.getBookReviews(id));
            model.addAttribute("principal", principal);
            return "bookDetails";
        }
        review.setBook(bookService.getBookById(id));
        review.setUserNickname(user.getNickname());
        review.setUserPhoto(user.getPhoto());
        review.setUserId(user.getId());
        reviewService.saveReview(review);
        bookService.calculateRating(id);
        return "redirect:/bookDetails/" + id + "#allreviews";
    }

    /**
     * Handles the {@link RequestMethod#GET} used to get the view where a review can be edited.
     *
     * @param id    the identifier of the review that will be edited.
     * @param model {@link Model} used to add attributes that requires to be returned to the View.
     * @return the name of the view where reviews can be edited or deleted.
     */
    @GetMapping("/managePosts/{id}")
    public String managePosts(@PathVariable("id") long id, Model model) {
        Review review = reviewService.getReviewById(id);
        model.addAttribute(review);
        return "managePosts";
    }

    /**
     * Handles the {@link RequestMethod#POST} used to save an edited review.
     *
     * @param id        the identifier of the review that will be edited.
     * @param principal {@link Principal} object which stores the currently logged in user.
     * @param review    {@link Review} object that contains the new values for the review.
     * @return the name of the view where the edited review can be displayed.
     */
    @PostMapping("/managePosts/{id}")
    public String editPost(@PathVariable("id") long id, Principal principal, Review review) {
        String[] userRoles = userService.findByEmail(principal.getName()).getRoles().stream().map(Role::getName).toArray(String[]::new);
        long redirectId = reviewService.getReviewById(id).getBook().getId();
        review.setEdited(true);
        review.setEditedBy(userRoles[0].equals("ROLE_ADMIN") ? "Admin" : "Moderator");
        reviewService.updateReview(id, review);
        return "redirect:/bookDetails/" + redirectId;
    }

    /**
     * Handles the {@link RequestMethod#GET} used to delete an existing {@link Review} object.
     *
     * @param id the identifier of the review that will be deleted.
     * @return the name of the view where the review are displayed.
     */
    @GetMapping("/managePosts/delete/{id}")
    public String deletePost(@PathVariable("id") long id) {
        Book book = reviewService.getReviewById(id).getBook();
        reviewService.deleteReviewById(id);
        return "redirect:/bookDetails/" + book.getId();
    }
}
