package ro.sci.bookwormscommunity.web.controller;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.service.BanMailService;
import ro.sci.bookwormscommunity.service.BookService;
import ro.sci.bookwormscommunity.service.UserService;

import javax.servlet.http.HttpServletResponse;

/**
 * Controller that handles the mapping for the Administrator(ADMIN) actions.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 */
@Controller
public class AdminController {

    private Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BanMailService mailService;

    /**
     * Handles the {@link RequestMethod#GET} used to promote an user to moderator.
     *
     * @param id    identifier for the user that will be promoted.
     * @return the user.html view.
     */
    @GetMapping("/user/{id}/promote")
    public String promote(@PathVariable("id") long id) {
        userService.promoteUser(id);
        return "redirect:/user/" + id + "?promoted";
    }

    /**
     * Handles the {@link RequestMethod#GET} method used to disabled a specific user's account.
     *
     * @param id identifier for the user who's account will be disbled.
     * @return the name of the view where the banned's user information is displayed.
     */
    @GetMapping("/user/ban/{id}")
    public String banUser(@PathVariable("id") long id) {
        User user = userService.findById(id);

        userService.banUser(id);

        try {
            mailService.sendAccountDisabledMail(user);
        } catch (MailException e) {
            logger.error("Error Sending the Ban Mail: {e}", e);
        }

        return "redirect:/user/" + id + "?banned";
    }

    /**
     * Handles the {@link RequestMethod#GET} method used to download a list of all the existing users as a CSV file.
     *
     * @param response {@link HttpServletResponse} instance used to send the CSV file to the client
     */
    @GetMapping("/export-users")
    public void exportCSVUsers(HttpServletResponse response) {

        String fileName = "users.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + fileName + "\"");

        try {
            StatefulBeanToCsv<User> writer = new StatefulBeanToCsvBuilder<User>(response.getWriter())
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .withOrderedResults(false)
                    .build();

            writer.write(userService.getAllUsers());
        } catch (Exception e) {
            logger.error("An error occurred while creating the CSV file: ", e);
        }
    }

    /**
     * Handles the {@link RequestMethod#GET} method used to download a list of all the existing books as a CSV file.
     *
     * @param response {@link HttpServletResponse} instance used to send the CSV file to the client
     */
    @GetMapping("/export-books")
    public void exportCSVBooks(HttpServletResponse response) {

        String fileName = "books.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + fileName + "\"");
        try {
            StatefulBeanToCsv<Book> writer = new StatefulBeanToCsvBuilder<Book>(response.getWriter())
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .withOrderedResults(false)
                    .build();

            writer.write(bookService.getAllBooks());
        } catch (Exception e) {
            logger.error("An error occurred while creating the CSV file: ", e);
        }
    }
}
