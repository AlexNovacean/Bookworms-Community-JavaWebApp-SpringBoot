package ro.sci.bookwormscommunity.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.repositories.BookRepository;
import ro.sci.bookwormscommunity.repositories.UserRepository;
import ro.sci.bookwormscommunity.service.WriteDataToCSV;
import ro.sci.bookwormscommunity.service.WriteDataToCsvBooks;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CsvDownloadController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BookRepository bookRepository;

    @GetMapping("/download/users.csv")
    public void downloadCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=users.csv");

        List<User> users = (List<User>) userRepository.findAll();

        // Using method 1 ->
        // WriteDataToCSV.writeDataToCsvUsers(response.getWriter(), users);

        // Using method 2 ->
        WriteDataToCSV.writeDataToCsvWithListObjects(response.getWriter(), users);
    }
    @GetMapping("/download/books.csv")
    public void downloadCsvBooks(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=books.csv");

        List<Book> books= (List<Book>) bookRepository.findAll();

        // Using method 1 ->
        // WriteDataToCSV.writeDataToCsvUsers(response.getWriter(), books);

        // Using method 2 ->
        WriteDataToCsvBooks.writeDataToCsvWithListObjects(response.getWriter(), books);
    }

}