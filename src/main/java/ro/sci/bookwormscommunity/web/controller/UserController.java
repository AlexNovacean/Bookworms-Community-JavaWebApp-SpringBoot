package ro.sci.bookwormscommunity.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ro.sci.bookwormscommunity.model.Conversation;
import ro.sci.bookwormscommunity.model.Message;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.service.ConversationService;
import ro.sci.bookwormscommunity.service.MessageService;
import ro.sci.bookwormscommunity.service.UserService;
import ro.sci.bookwormscommunity.web.dto.MessageDto;

import java.security.Principal;

@Controller
@RequestMapping("/user/**")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ConversationService conversationService;

    @GetMapping
    public String userProfile(Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.findByEmail(email);
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

    @GetMapping("/user/msg/{id}")
    public String sendMsg(@PathVariable("id") long id, Model model) {
        model.addAttribute("toUser", userService.findById(id));
        return "mesaj";
    }


    @PostMapping("user/msg/{id}")
    public String saveMsg(@PathVariable("id") long id, Model model, Principal principal, @ModelAttribute("msg") MessageDto dto) {
        User toUser = userService.findById(id);
        User fromUser = userService.findByEmail(principal.getName());
        Conversation conversation = conversationService.startConversation(toUser.getId(), fromUser.getId());
        Message message = new Message(dto.getContent(), toUser, fromUser);
        message.setConversation(conversation);
        messageService.saveMessage(message);

        return "redirect:/user/" + id + "?trimis";

    }
}

