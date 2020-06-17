package ro.sci.bookwormscommunity.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.model.Word;
import ro.sci.bookwormscommunity.service.BookService;
import ro.sci.bookwormscommunity.service.UserService;

import java.security.Principal;
import java.util.List;

/**
 * Controller that handles the mapping for the index and login views.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @author Raul
 */
@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    /**
     * Handles the {@link RequestMethod#GET} method used to map the index, and send the lists with the top 10 rated and latest 10 added {@link Book} object to the view.
     *
     * @param searchWord container for the user search input.
     * @param model      {@link Model} used to add attributes that requires to be returned to the View.
     * @param principal  {@link Principal} object which stores the currently logged in user.
     * @return the name of the view for the index.
     */
    @GetMapping("")
    public String root(@ModelAttribute("searchWord") Word searchWord, Model model, Principal principal) {
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            model.addAttribute("user", user);
        }

        List<Book> topBooks = bookService.getTop10RatedBooks();
        model.addAttribute("topBooks", topBooks);

        List<Book> latestBooks = bookService.getLatest10AddedBooks();
        model.addAttribute("latestBooks", latestBooks);

        return "Home";
    }

    /**
     * Handles the {@link RequestMethod#GET} method used to map the login view.
     *
     * @return the name of the view used for log in.
     */
    @GetMapping("/login/**")
    public String login() {
        return "login";
    }

}
