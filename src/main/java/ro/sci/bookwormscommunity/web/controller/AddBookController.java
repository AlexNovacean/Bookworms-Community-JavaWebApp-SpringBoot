package ro.sci.bookwormscommunity.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.model.BookCondition;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.repositories.BookRepository;
import ro.sci.bookwormscommunity.service.BookService;
import ro.sci.bookwormscommunity.service.UserService;
import ro.sci.bookwormscommunity.web.dto.BookDto;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/addBook")
public class AddBookController {
    private Logger logger = LoggerFactory.getLogger(AddBookController.class);

    @Autowired
    BookService bookService;

    @Autowired
    UserService userService;

    @Autowired
    BookRepository bookRepository;

    @ModelAttribute("book")
    public BookDto book(){return new BookDto();}


    @GetMapping
    public String showSaveBookForm(Model model){

       model.addAttribute("conditions", BookCondition.values());
        return "addBook";}


    @PostMapping
    public String addBook (@ModelAttribute("book") @Valid BookDto bookdto, BindingResult result, Principal principal){

        User user = userService.findByEmail(principal.getName());

        bookdto.setUser(user);
        if(result.hasErrors()){
            return "addBook";
        }
        try {
            bookService.save(bookdto);
        } catch (Exception e) {
            logger.error("Error when saving the book: ",e);
        }

        return "redirect:/addBook?success";

    }
}
