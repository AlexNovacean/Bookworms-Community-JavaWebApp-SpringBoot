package ro.sci.bookwormscommunity.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ro.sci.bookwormscommunity.mapper.BookMapper;
import ro.sci.bookwormscommunity.model.*;
import ro.sci.bookwormscommunity.service.BookService;
import ro.sci.bookwormscommunity.service.ReviewService;
import ro.sci.bookwormscommunity.service.UserService;
import ro.sci.bookwormscommunity.web.dto.BookDto;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class BookController {

    private Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @ModelAttribute("book")
    public BookDto book() {
        return new BookDto();
    }

    //list all books
    @GetMapping("/communityBooks")
    public String showBooks(@ModelAttribute("searchWord")Word searchWord, Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "communityBooks";
    }

    //show book by id
    @GetMapping("/bookDetails/{id}")
    public String bookDetailsForm(@ModelAttribute("searchWord")Word searchWord, @PathVariable("id") Long id, Model model, Principal principal) {
        Optional<Book> book = bookService.getBookById(id);
        List<Review> reviews = reviewService.getBookReviews(id);
        model.addAttribute("book", book.get());
        model.addAttribute("reviews",reviews);
        model.addAttribute("principal",principal);
        return "bookDetails";
    }

    //add a book
    @GetMapping("/addBook")
    public String showSaveBookForm(@ModelAttribute("searchWord")Word searchWord, Model model) {
        model.addAttribute("conditions", BookCondition.values());
        return "addBook";
    }

    @PostMapping("/addBook")
    public String addBook(@ModelAttribute("book") @Valid BookDto bookdto, BindingResult result, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        bookdto.setUser(user);
        if (result.hasErrors()) {
            return "addBook";
        }
        try {
            bookService.saveBook(bookdto);
        } catch (Exception e) {
            logger.error("Error when saving the book: ", e);
        }
        return "redirect:/addBook?success";
    }

    //delete a book
    @GetMapping("/deleteBook/{id}")
    public String deleteBookForm(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return "redirect:/communityBooks?deletedBook";
    }

    //update a book
    @GetMapping("/editBook/{id}")
    public String showUpdateForm(@ModelAttribute("searchWord")Word searchWord, @PathVariable("id") Long id, Model model) throws IOException {
        Optional<Book> book = bookService.getBookById(id);
        model.addAttribute(BookMapper.mapBookToBookDto(book.get()));
        model.addAttribute("conditions", BookCondition.values());
        return "updateBook";
    }

    @PostMapping("/updateBook/{id}")
    public String updateBook(@PathVariable("id") Long id, @Valid BookDto bookdto, BindingResult result, Principal principal) throws IOException {
        User user = userService.findByEmail(principal.getName());
        Book book = bookService.getBookById(id).get();
        bookdto.setUser(user);
        if (result.hasErrors()) {
            bookdto.setPhoto(new MockMultipartFile("bookPhoto.png",new ByteArrayInputStream(book.getImage())));
            bookdto.setCondition(book.getCondition());
            return "updateBook";
        }
        try {
            bookService.updateBook(id, bookdto);
        } catch (Exception e) {
            logger.error("Error when saving the book: ", e);
        }
        return "redirect:/bookDetails/" + id;
    }

    @ModelAttribute("review")
    public Review review(){ return new Review();}

    @PostMapping("/bookDetails/{id}")
    public String postReview(@PathVariable("id")long id, @ModelAttribute("review") @Valid Review review, BindingResult result, Principal principal, Model model){

        if(result.hasErrors()){
            model.addAttribute("book",bookService.getBookById(id).get());
            model.addAttribute("reviews",reviewService.getBookReviews(id));
            return "bookDetails";
        }

        review.setBook(bookService.getBookById(id).get());
        review.setUserNickname(userService.findByEmail(principal.getName()).getNickname());

        reviewService.saveReview(review);

        bookService.calculateRating(id);

        return "redirect:/bookDetails/" + id + "#allreviews";
    }

    @GetMapping("/managePosts/{id}")
    public String managePosts(@PathVariable("id")long id, Model model){
        Review review = reviewService.getReviewById(id);
        model.addAttribute(review);
        return "managePosts";
    }

    @PostMapping("/managePosts/{id}")
    public String editPost(@PathVariable("id")long id, Principal principal, Review review){

        String[] userRoles = userService.findByEmail(principal.getName()).getRoles().stream().map((role) -> role.getName()).toArray(String[]::new);
        long redirectId = reviewService.getReviewById(id).getBook().getId();
        review.setEdited(true);
        review.setEditedBy(userRoles[0].equals("ROLE_ADMIN")? "Admin" : "Moderator");

        reviewService.updateReview(id,review);

        return "redirect:/bookDetails/" + redirectId;
    }

    @GetMapping("/managePosts/delete/{id}")
    public String deletePost(@PathVariable("id")long id){
        Book book = reviewService.getReviewById(id).getBook();
        reviewService.deleteReviewById(id);
        return "redirect:/bookDetails/" + book.getId();
    }

    @GetMapping("/communityBooks/byRating")
    public String showBooksByRating(@ModelAttribute("searchWord")Word searchWord, Model model) {
        model.addAttribute("books", bookService.getAllBooksOrderedByRating());
        return "communityBooks";
    }

    @GetMapping("/communityBooks/byDate")
    public String showBooksByDate(@ModelAttribute("searchWord")Word searchWord, Model model) {
        model.addAttribute("books", bookService.getAllBooksOrderedByDate());
        return "communityBooks";
    }

    @GetMapping("/communityBooks/forRent")
    public String showBooksForRent(@ModelAttribute("searchWord")Word searchWord, Model model) {
        model.addAttribute("books", bookService.getAllBooksForRent());
        return "communityBooks";
    }

    @GetMapping("/communityBooks/forSale")
    public String showBooksForSale(@ModelAttribute("searchWord")Word searchWord, Model model) {
        model.addAttribute("books", bookService.getAllBooksForSale());
        return "communityBooks";
    }

    @GetMapping("/searchBooks")
    public String searchForBooks(@ModelAttribute("searchWord")Word searchWord, Model model){
        model.addAttribute("booksOfAuthors",bookService.searchForAuthors(searchWord.getSearchPattern()));
        model.addAttribute("booksWithName",bookService.searchForBookName(searchWord.getSearchPattern()));
        model.addAttribute("booksOfType", bookService.searchForBookType(searchWord.getSearchPattern()));
        return "searchResults";
    }
}
