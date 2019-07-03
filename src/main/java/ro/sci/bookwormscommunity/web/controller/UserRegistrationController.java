package ro.sci.bookwormscommunity.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.service.UserService;
import ro.sci.bookwormscommunity.web.dto.UserRegistrationDto;

import javax.validation.Valid;

/**
 * Controller that handles the mapping for the user registration process.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 */
@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    private Logger logger = LoggerFactory.getLogger(UserRegistrationController.class);

    @Autowired
    private UserService userService;

    /**
     * Initializes the user {@link ModelAttribute} with a new instance of {@link UserRegistrationDto}.
     *
     * @return {@link UserRegistrationDto} instance.
     */
    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    /**
     * Handles the {@link RequestMethod#GET} method used to map retrieve the view for the user registration process.
     *
     * @param model {@link Model} used to add attributes that requires to be returned to the View.
     * @return the name of the view where a user can register.
     */
    @GetMapping
    public String showRegistrationForm(Model model) {
        return "registration";
    }

    /**
     * Handles the {@link RequestMethod#POST} method used to retrieve the registration information an save a new user to DB.
     *
     * @param userDto {@link UserRegistrationDto} object that contains the registration infromtion.
     * @param result  {@link BindingResult} instance that stores the validation errors.
     * @return the name of the view where the user can register in case of error, the name of the log in view otherwise.
     */
    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
                                      BindingResult result) {

        User existing = userService.findByEmail(userDto.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }

        if (result.hasErrors()) {
            return "registration";
        }
        try {
            userService.save(userDto);
        } catch (Exception e) {
            logger.warn("An error occurred while saving the profile picture: ", e);
        }
        return "redirect:/login?success";
    }
}
