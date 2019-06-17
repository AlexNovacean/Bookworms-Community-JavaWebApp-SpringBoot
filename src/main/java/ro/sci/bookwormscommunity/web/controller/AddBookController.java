package ro.sci.bookwormscommunity.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ro.sci.bookwormscommunity.web.model.Book;
import ro.sci.bookwormscommunity.web.model.BookCondition;
import ro.sci.bookwormscommunity.web.repository.BookRepository;
import ro.sci.bookwormscommunity.web.service.BookService;

import javax.validation.Valid;

@Controller
@RequestMapping("/addBook")
public class AddBookController {

    @Autowired
    BookService bookService;

    @Autowired
    BookRepository bookRepository;

    @ModelAttribute("book")
    public Book book(){return new Book();}


    @GetMapping
    public String showSaveBookForm(Model model){

       model.addAttribute("conditions", BookCondition.values());
        return "addBook";}


    @PostMapping
    public String addBook (@ModelAttribute("book") @Valid Book book, BindingResult result){


        if(result.hasErrors()){
            return "addBook";
        }
        bookService.save(book);

        return "redirect:/addBook?success";

    }

    @GetMapping("/updateBook/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Book book = bookService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));

        model.addAttribute("book", book);
        return "updateBook";
    }
    @PostMapping("/updateBook/{id}")
    public String updateBook(@PathVariable("id") Long id, @Valid Book book,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            book.setId(id);
            return "updateBook";
        }

        bookService.save(book);
        model.addAttribute("books", bookService.findAll());
        return "addBook";
    }

    @GetMapping("/deleteBook/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        Book book = bookService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
        bookRepository.delete(book);
        model.addAttribute("books", bookRepository.findAll());
        return "/index";
    }
}
