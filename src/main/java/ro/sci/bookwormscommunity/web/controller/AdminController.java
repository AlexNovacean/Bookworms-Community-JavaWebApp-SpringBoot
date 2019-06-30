package ro.sci.bookwormscommunity.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.repositories.AdminRepository;
import ro.sci.bookwormscommunity.service.AdminService;
import ro.sci.bookwormscommunity.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    UserService userService;

    @ModelAttribute("user")
    public User user(){
        return new User();
    }

    @GetMapping("/home")
    public String adminHome() {
        return "adminhome";
    }

    @GetMapping("/create")
    public String moderator(){
        return "moderator";
    }


    @GetMapping("/all")
    public String listAllUsers(Model model){

       model.addAttribute("users", adminService.listAllUsers());
        return "adminhome";
    }

    @PostMapping(path = "/create")
    public String createModerator(@ModelAttribute("user") @Valid User user, BindingResult result, Principal principal){
        if (result.hasErrors()){
            return "adminhome";
        }
        User userr = userService.findByEmail(principal.getName());
        try{
            adminService.createModerator(userr);
        }catch (Exception e){
            System.out.println("something");
        }
        return "moderator";

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
