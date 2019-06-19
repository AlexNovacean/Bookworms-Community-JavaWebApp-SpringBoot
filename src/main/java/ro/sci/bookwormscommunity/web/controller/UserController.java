package ro.sci.bookwormscommunity.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ro.sci.bookwormscommunity.model.Message;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.service.MessageService;
import ro.sci.bookwormscommunity.service.UserService;
import ro.sci.bookwormscommunity.web.dto.MessageDto;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user/**")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @GetMapping
    public String userProfile(Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.findByEmail(email);
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/user/{id}")
    public String seeUserProfile(@PathVariable("id") long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "user";
    }

    @PostMapping("/user/send/{id}")
    public String sendMessage(@PathVariable("id")long id, @ModelAttribute("msg") MessageDto dto, Principal principal){
        User fromUser = userService.findByEmail(principal.getName());
        User toUser = userService.findById(id);
        Message message = new Message(dto.getContent(),toUser,fromUser);
        messageService.saveMessage(message);
        return "redirect:/user?sent";

    }

    @GetMapping("/user/messages")
    public String seeMessages(Model model, Principal principal){
        User user = userService.findByEmail(principal.getName());
        List<Message> messages = messageService.getUserMessages(user.getId());
        model.addAttribute("messages",messages);
        return "messages";
    }
}

