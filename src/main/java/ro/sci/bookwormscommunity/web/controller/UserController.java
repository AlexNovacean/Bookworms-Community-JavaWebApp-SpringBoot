package ro.sci.bookwormscommunity.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/user/**")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public String userProfile(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("principal", principal);
        return "user";
    }

    @GetMapping("/user/{id}")
    public String seeUserProfile(@PathVariable("id") long id, Model model, Principal principal) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("principal", principal);
        return "user";
    }
}

