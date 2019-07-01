package ro.sci.bookwormscommunity.web.controller;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ro.sci.bookwormscommunity.mapper.BookMapper;
import ro.sci.bookwormscommunity.mapper.UserMapper;
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.service.BookService;
import ro.sci.bookwormscommunity.service.UserService;

import javax.servlet.http.HttpServletResponse;

@Controller
public class AdminController {

    private Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @GetMapping("/user/{id}/promote")
    public String promote(@PathVariable("id") long id, Model model) {
        User user = userService.findById(id);
        userService.promoteUser(id);
        model.addAttribute("user", user);
        return "redirect:/user/" + id + "?promoted";
    }

    @GetMapping("/export-users")
    public void exportCSVUsers(HttpServletResponse response) throws Exception{

        String fileName = "users.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + fileName + "\"");

        StatefulBeanToCsv<User> writer = new StatefulBeanToCsvBuilder<User>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        writer.write(userService.getAllUsers());
    }

    @GetMapping("/export-books")
    public void exportCSVBooks(HttpServletResponse response) throws Exception{

        String fileName = "books.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + fileName + "\"");

        StatefulBeanToCsv<Book> writer = new StatefulBeanToCsvBuilder<Book>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        writer.write(bookService.getAllBooks());
    }
}
