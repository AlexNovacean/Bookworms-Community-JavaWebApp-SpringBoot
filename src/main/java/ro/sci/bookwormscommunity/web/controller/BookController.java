package ro.sci.bookwormscommunity.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

/**
 * Controller that handles the mapping for the books related actions.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 */
@Controller
public class BookController {

    private Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    /**
     * Initializes the book {@link ModelAttribute} as a new {@link BookDto} object.
     *
     * @return {@link BookDto} object.
     */
    @ModelAttribute("book")
    public BookDto book() {
        return new BookDto();
    }

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
     * Handles the {@link RequestMethod#GET} method used to send the list with all existing books to the view.
     *
     * @param searchWord container for the user search input.
     * @param model      {@link Model} used to add attributes that requires to be returned to the View.
     * @return the name of the view.
     */
    @GetMapping("/communityBooks")
    public String showBooks(@ModelAttribute("searchWord") Word searchWord, Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "communityBooks";
    }

    /**
     * Handles the {@link RequestMethod#GET} method used to send a specific {@link Book} object to the view.
     *
     * @param searchWord container for the user search input.
     * @param id         identifier for the {@link Book} object that need to be sent.
     * @param model      {@link Model} used to add attributes that requires to be returned to the View.
     * @param principal  {@link Principal} object which stores the currently logged in user.
     * @return the name of the view.
     */
    @GetMapping("/bookDetails/{id}")
    public String bookDetailsForm(@ModelAttribute("searchWord") Word searchWord, @PathVariable("id") Long id, Model model, Principal principal) {
        Book book = bookService.getBookById(id);
        List<Review> reviews = reviewService.getBookReviews(id);
        model.addAttribute("book", book);
        model.addAttribute("reviews", reviews);
        model.addAttribute("principal", principal);
        return "bookDetails";
    }

    /**
     * Handles the {@link RequestMethod#GET} method used to map the view for adding a book.
     *
     * @param searchWord container for the user search input.
     * @param model      {@link Model} used to add attributes that requires to be returned to the View.
     * @return the name of the view.
     */
    @GetMapping("/addBook")
    public String showSaveBookForm(@ModelAttribute("searchWord") Word searchWord, Model model) {
        model.addAttribute("conditions", BookCondition.values());
        return "addBook";
    }

    /**
     * Handles the {@link RequestMethod#POST} method used to retrieve the {@link Book} object from the view and save it to the DB.
     *
     * @param bookDto    a {@link BookDto} object containing the information for the {@link Book} object that will be saved.
     * @param result     {@link BindingResult} instance that stores the validation errors.
     * @param principal  {@link Principal} object which stores the currently logged in user.
     * @param searchWord container for the user search input.
     * @param model      {@link Model} used to add attributes that requires to be returned to the View.
     * @return the name of the view for adding a book in case a validation error occurs, the name of the redirect view otherwise
     */
    @PostMapping("/addBook")
    public String addBook(@ModelAttribute("book") @Valid BookDto bookDto, BindingResult result, Principal principal, @ModelAttribute("searchWord") Word searchWord, Model model) {
        User user = userService.findByEmail(principal.getName());
        bookDto.setUser(user);
        if (result.hasErrors()) {
            model.addAttribute("conditions", BookCondition.values());
            return "addBook";
        }
        try {
            bookService.saveBook(bookDto);
        } catch (Exception e) {
            logger.error("Error when saving the book: ", e);
        }
        return "redirect:/addBook?success";
    }

    /**
     * Handles the {@link RequestMethod#GET} method used to delete a {@link Book} object from the DB.
     *
     * @param id identifier for the the book that will be deleted.
     * @return the name of the redirected view
     */
    @GetMapping("/deleteBook/{id}")
    public String deleteBookForm(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return "redirect:/communityBooks?deletedBook";
    }

    /**
     * Handles the {@link RequestMethod#GET} method used to return the view for editing an existing {@link Book}.
     *
     * @param searchWord container for the user search input.
     * @param id         identifier for the book that will be updated.
     * @param model      {@link Model} used to add attributes that requires to be returned to the View.
     * @return the name of the view used to update the book
     */
    @GetMapping("/editBook/{id}")
    public String showUpdateForm(@ModelAttribute("searchWord") Word searchWord, @PathVariable("id") Long id, Model model) {
        Book book = bookService.getBookById(id);
        try {
            model.addAttribute(BookMapper.mapBookToBookDto(book));
        } catch (IOException e) {
            logger.error("Error while retrieving the book photo: ", e);
        }
        model.addAttribute("conditions", BookCondition.values());
        return "updateBook";
    }

    /**
     * Handles the {@link RequestMethod#POST} method used to retrieve the {@link BookDto} object from the view.
     * The bookDto object's information will be used to update an existing {@link Book} object.
     *
     * @param id         identifier for the book that will be updated.
     * @param bookDto    {@link BookDto} object that contains the new values for the the {@link Book} object's fields
     * @param result     {@link BindingResult} instance that stores the validation errors of the {@link BookDto} object's field.
     * @param principal  {@link Principal} object which stores the currently logged in user.
     * @param searchWord container for the user search input.
     * @param model      {@link Model} used to add attributes that requires to be returned to the View.
     * @return the name of the view used to update a book in case of validation errors, the name of the redirected view otherwise.
     */
    @PostMapping("/updateBook/{id}")
    public String updateBook(@PathVariable("id") Long id, @Valid BookDto bookDto, BindingResult result, Principal principal, @ModelAttribute("searchWord") Word searchWord, Model model) {
        User user = userService.findByEmail(principal.getName());
        Book book = bookService.getBookById(id);
        bookDto.setUser(user);
        if (result.hasErrors()) {
            try {
                bookDto.setPhoto(new MockMultipartFile("bookPhoto.png", new ByteArrayInputStream(book.getImage())));
            } catch (IOException e) {
                logger.error("Error while retrieving the book photo: ", e);
            }
            bookDto.setCondition(book.getCondition());
            model.addAttribute("conditions", BookCondition.values());
            return "updateBook";
        }
        try {
            bookService.updateBook(id, bookDto);
        } catch (Exception e) {
            logger.error("Error when saving the book: ", e);
        }
        return "redirect:/bookDetails/" + id;
    }

    /**
     * Handles the {@link RequestMethod#GET} used to send a list with all the existing {@link Book} objects ordered by their rating field.
     *
     * @param searchWord container for the user search input.
     * @param model      {@link Model} used to add attributes that requires to be returned to the View.
     * @return the name of the view where the books will be displayed.
     */
    @GetMapping("/communityBooks/byRating")
    public String showBooksByRating(@ModelAttribute("searchWord") Word searchWord, Model model) {
        model.addAttribute("books", bookService.getAllBooksOrderedByRating());
        return "communityBooks";
    }

    /**
     * Handles the {@link RequestMethod#GET} used to send a list with all the existing {@link Book} objects ordered by their addDate field.
     *
     * @param searchWord container for the user search input.
     * @param model      {@link Model} used to add attributes that requires to be returned to the View.
     * @return the name of the view where the books will be displayed.
     */
    @GetMapping("/communityBooks/byDate")
    public String showBooksByDate(@ModelAttribute("searchWord") Word searchWord, Model model) {
        model.addAttribute("books", bookService.getAllBooksOrderedByDate());
        return "communityBooks";
    }

    /**
     * Handles the {@link RequestMethod#GET} used to send a list with all existing {@link Book} object who's bookRent field is true.
     *
     * @param searchWord container for the user search input.
     * @param model      {@link Model} used to add attributes that requires to be returned to the View.
     * @return the name of the view where the books will be displayed.
     */
    @GetMapping("/communityBooks/forRent")
    public String showBooksForRent(@ModelAttribute("searchWord") Word searchWord, Model model) {
        model.addAttribute("books", bookService.getAllBooksForRent());
        return "communityBooks";
    }

    /**
     * Handles the {@link RequestMethod#GET} used to send a list with all existing {@link Book} object who's bookSale field is true.
     *
     * @param searchWord container for the user search input.
     * @param model      {@link Model} used to add attributes that requires to be returned to the View.
     * @return the name of the view where the books will be displayed.
     */
    @GetMapping("/communityBooks/forSale")
    public String showBooksForSale(@ModelAttribute("searchWord") Word searchWord, Model model) {
        model.addAttribute("books", bookService.getAllBooksForSale());
        return "communityBooks";
    }

    /**
     * Handles the {@link RequestMethod#GET} used to send the list with all the {@link Book} objects that who's name, author or type matches the searchWord.
     *
     * @param searchWord container for the user search input.
     * @param model      {@link Model} used to add attributes that requires to be returned to the View.
     * @return the name of the view where the books will be displayed.
     */
    @GetMapping("/searchBooks")
    public String searchForBooks(@ModelAttribute("searchWord") Word searchWord, Model model) {
        model.addAttribute("booksOfAuthors", bookService.searchForAuthors(searchWord.getSearchPattern()));
        model.addAttribute("booksWithName", bookService.searchForBookName(searchWord.getSearchPattern()));
        model.addAttribute("booksOfType", bookService.searchForBookType(searchWord.getSearchPattern()));
        return "searchResults";
    }
}
