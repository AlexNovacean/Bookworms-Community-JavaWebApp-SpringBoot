package ro.sci.bookwormscommunity.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.sci.bookwormscommunity.model.Message;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.service.ConversationService;
import ro.sci.bookwormscommunity.service.MessageService;
import ro.sci.bookwormscommunity.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/messages/**")
public class MessagesController {

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String seeMessages(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("messages", conversationService.getUserConversations(user.getId()));
        model.addAttribute("user", user);
        return "messages";
    }

    @GetMapping("/messages/{id}")
    public String seeSpecific(@PathVariable("id")long id, Model model){
        List<Message> texts = messageService.getUserMessages(id);
        model.addAttribute("texts",texts);
        return "texts";
    }

}
