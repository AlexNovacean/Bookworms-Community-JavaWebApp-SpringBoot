package ro.sci.bookwormscommunity.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ro.sci.bookwormscommunity.mapper.UserMapper;
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.service.BanMailService;
import ro.sci.bookwormscommunity.service.BookService;
import ro.sci.bookwormscommunity.service.UserService;
import ro.sci.bookwormscommunity.web.dto.UserDto;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

/**
 * Controller that handles the mapping for the user profile related actions.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 */
@Controller
@RequestMapping("/user/**")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BanMailService mailService;

    /**
     * Initializes the user {@link ModelAttribute} with a new instance of the {@link UserDto}.
     *
     * @return {@link UserDto} instance.
     */
    @ModelAttribute("user")
    public UserDto userDto() {
        return new UserDto();
    }

    /**
     * Handles the {@link RequestMethod#GET} method used to send to the view all the user information(user's Last Name, First Name, etc, and added Books) for the current's user.
     *
     * @param model     {@link Model} used to add attributes that requires to be returned to the View.
     * @param principal {@link Principal} object which stores the currently logged in user.
     * @return the name of the view where the user information is displayed.
     */
    @GetMapping
    public String userProfile(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        List<Book> books = bookService.getUserBooks(user.getId());
        model.addAttribute("books", books);
        model.addAttribute("user", user);
        model.addAttribute("principal", principal);
        return "user";
    }

    /**
     * Handles the {@link RequestMethod#GET} method used to send to the view the information of a specific user.
     *
     * @param id        the identifier of the user who's profile will be seen
     * @param model     {@link Model} used to add attributes that requires to be returned to the View.
     * @param principal {@link Principal} object which stores the currently logged in user.
     * @return the name of the view where the user profile is displayed.
     */
    @GetMapping("/user/{id}")
    public String seeUserProfile(@PathVariable("id") long id, Model model, Principal principal) {
        User user = userService.findById(id);
        List<Book> books = bookService.getUserBooks(user.getId());
        model.addAttribute("books", books);
        model.addAttribute("user", user);
        model.addAttribute("principal", principal);
        return "user";
    }

    /**
     * Handles the {@link RequestMethod#GET} method used to map the view where the user information can be edited.
     *
     * @param model     {@link Model} used to add attributes that requires to be returned to the View.
     * @param principal {@link Principal} object which stores the currently logged in user.
     * @return the name of the view where the user information can be edited.
     */
    @GetMapping("/user/editProfile")
    public String showUpdateForm(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        try {
            model.addAttribute(UserMapper.mapUserToUserDto(user));
        } catch (IOException e) {
            logger.error("Error while retrieving the user's profile photo: ", e);
        }
        return "updateUser";
    }

    /**
     * Handles the {@link RequestMethod#POST} method used to retrieve and save the new user information.
     *
     * @param userDto   {@link UserDto} object containing the new values for the {@link User} object that will be updated.
     * @param result    {@link BindingResult} instance that stores the validation errors.
     * @param principal {@link Principal} object which stores the currently logged in user.
     * @param model     {@link Model} used to add attributes that requires to be returned to the View.
     * @return the name of the view where the user information can be edited in case of validation error, the name of the view where the user information is displayed otherwise.
     */
    @PostMapping("/user/updateProfile")
    public String updateUser(@Valid UserDto userDto, BindingResult result, Principal principal, Model model) {
        User user = userService.findByEmail(principal.getName());
        if (!principal.getName().equals(userDto.getEmail())) {
            User existing = userService.findByEmail(userDto.getEmail());
            if (existing != null) {
                result.rejectValue("email", null, "There is already an account registered with that email");
            }
        }
        if (result.hasErrors()) {
            try {
                userDto.setPhoto(new MockMultipartFile("userPhoto.png", new ByteArrayInputStream(user.getPhoto())));
            } catch (IOException e) {
                logger.error("Error while sending the user profile photo: ", e);
            }
            return "updateUser";
        }
        try {
            userService.updateUser(user.getId(), userDto);
        } catch (IOException e) {
            logger.error("Error while retrieving the user profile photo: ", e);
        }
        if (!principal.getName().equals(userDto.getEmail()) || !userDto.getPassword().isEmpty()) {
            return "redirect:/logout";
        }
        return "redirect:/user";
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
}

