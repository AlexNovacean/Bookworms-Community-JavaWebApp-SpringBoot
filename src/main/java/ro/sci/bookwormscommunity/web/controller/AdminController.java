package ro.sci.bookwormscommunity.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.repositories.AdminRepository;
import ro.sci.bookwormscommunity.service.AdminService;
import ro.sci.bookwormscommunity.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin/**")
public class AdminController {


    @Autowired
    AdminService adminService;

    @Autowired
    UserService userService;

    @GetMapping("/home")
    public String adminHome(Model model){

        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "adminhome";
    }

    @GetMapping("/home/user/{id}")
    public String seeUsers(@PathVariable("id") long id, Model model, Principal principal){
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("principal", principal);
        return "user";
    }

    @GetMapping("/home/user/{id}/promote")
    public String promote(@PathVariable("id") long id, Model model) {
        User user = userService.findById(id);
            adminService.createModerator(user);
        model.addAttribute("user", user);
        return "redirect:/user/" + id + "?promoted";
        }




//    @PostMapping
//    public String addBook (@ModelAttribute("book") @Valid BookDto bookdto, BindingResult result, Principal principal){
//
//        User user = userService.findByEmail(principal.getName());
//
//        bookdto.setUser(user);
//        if(result.hasErrors()){
//            return "addBook";
//        }
//        try {
//            bookService.save(bookdto);
//        } catch (Exception e) {
//            logger.error("Error when saving the book: ",e);
//        }
//
//        return "redirect:/addBook?success";
//
//    }
}
