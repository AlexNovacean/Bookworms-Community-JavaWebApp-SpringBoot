package ro.sci.bookwormscommunity.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.model.Word;
import ro.sci.bookwormscommunity.service.BookService;
import ro.sci.bookwormscommunity.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @GetMapping("")
    public String root(@ModelAttribute("searchWord") Word searchWord, Model model, Principal principal) {
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            model.addAttribute("user", user);
        }

        List<Book> topBooks = bookService.getTop10RatedBooks();
        model.addAttribute("topBooks",topBooks);

        List<Book> latestBooks = bookService.getLatest10AddedBooks();
        model.addAttribute("latestBooks",latestBooks);

        return "Home";
    }

    @GetMapping("/login/**")
    public String login() {
        return "login";
    }

}
