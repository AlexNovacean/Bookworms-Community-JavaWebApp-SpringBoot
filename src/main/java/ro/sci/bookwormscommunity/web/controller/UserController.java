package ro.sci.bookwormscommunity.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.service.BookService;
import ro.sci.bookwormscommunity.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user/**")
public class UserController {

    private UserService userService;
    private BookService bookService;

    @Autowired
    public UserController(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }

    @GetMapping
    public String userProfile(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        List<Book> books = bookService.getUserBooks(user.getId());
        model.addAttribute("books", books);
        model.addAttribute("user", user);
        model.addAttribute("principal", principal);
        return "user";
    }

    @GetMapping("/user/{id}")
    public String seeUserProfile(@PathVariable("id") long id, Model model, Principal principal) {
        User user = userService.findById(id);
        List<Book> books = bookService.getUserBooks(user.getId());
        model.addAttribute("books",books);
        model.addAttribute("user", user);
        model.addAttribute("principal", principal);
        return "user";
    }
}

