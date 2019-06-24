package ro.sci.bookwormscommunity.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ro.sci.bookwormscommunity.mapper.BookMapper;
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.model.BookCondition;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.service.BookServiceImpl;
import ro.sci.bookwormscommunity.service.UserService;
import ro.sci.bookwormscommunity.web.dto.BookDto;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
public class BookController {
    private Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    BookServiceImpl bookServiceImpl;

    @Autowired
    UserService userService;

    @ModelAttribute("book")
    public BookDto book(){return new BookDto();}

    //list all books
    @GetMapping("/communityBooks")
    public String showBooks(Model model) {
        model.addAttribute("books",bookServiceImpl.getAllBooks());
        return "communityBooks";
    }

    //show book by id
    @GetMapping("/bookDetails/{id}")
    public String bookDetailsForm(@PathVariable("id") Long id, Model model) {
        Optional<Book> book = bookServiceImpl.getBookById(id);
        model.addAttribute("book",  book.get());
        return "bookDetails";
    }

    //add a book
    @GetMapping("/addBook")
    public String showSaveBookForm(Model model){
       model.addAttribute("conditions", BookCondition.values());
        return "addBook";}

    @PostMapping("/addBook")
    public String addBook (@ModelAttribute("book") @Valid BookDto bookdto, BindingResult result, Principal principal){
        User user = userService.findByEmail(principal.getName());
        bookdto.setUser(user);
        if(result.hasErrors()){
            return "addBook";
        }
        try {
            bookServiceImpl.saveBook(bookdto);
        } catch (Exception e) {
            logger.error("Error when saving the book: ",e);
        }
        return "redirect:/addBook?success";
    }

    //delete a book
    @GetMapping("/deleteBook/{id}")
    public String deleteBookForm(@PathVariable("id") Long id, Model model) {
        Optional<Book> book = bookServiceImpl.getBookById(id);
        model.addAttribute("book", book.get());
        return "deleteBook";
    }
    @PostMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable("id") Long id, Model model) {
        Book book = bookServiceImpl.getBookById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
        bookServiceImpl.deleteBook(book);
        model.addAttribute("books", bookServiceImpl.getAllBooks());
        return "redirect:/communityBooks";
    }

    //update a book
    @GetMapping("/updateBook/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Optional<Book> book = bookServiceImpl.getBookById(id);
        BookDto bookDto = BookMapper.mapBookToBookDto(book.get());
        model.addAttribute("book", bookDto);
        model.addAttribute("conditions", BookCondition.values());
        return "updateBook";
    }

    @PostMapping("/updateBook/{id}")
    public String updateBook(@PathVariable("id") Long id, @Valid BookDto bookdto, BindingResult result, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        bookdto.setUser(user);
        if(result.hasErrors()){
            return "updateBook";
        }
        try {
            bookServiceImpl.saveBook(bookdto);
        } catch (Exception e) {
            logger.error("Error when saving the book: ",e);
        }
        return "redirect:/communityBooks";

    }

}
