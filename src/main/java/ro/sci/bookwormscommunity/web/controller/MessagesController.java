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
    public String seeSpecific(@PathVariable("id") long id, Model model, Principal principal) {
        User toUser = userService.findById(id);
        User fromUser = userService.findByEmail(principal.getName());
        Conversation conversation = conversationService.startConversation(toUser.getId(), fromUser.getId());
        List<Message> texts = messageService.getUserMessages(conversation.getId());
        model.addAttribute("texts", texts);
        model.addAttribute("conv", conversationService.findById(conversation.getId()));
        return "texts";
    }

    @GetMapping("/messages/conversation/{id}")
    public String openConversation(@PathVariable("id")long id,Model model){
        List<Message> texts = messageService.getUserMessages(id);
        model.addAttribute("texts", texts);
        model.addAttribute("conv", conversationService.findById(id));
        return "texts";
    }

    @PostMapping("/messages/{id}")
    public String sendMessage(@PathVariable("id") long id, Model model, @ModelAttribute("msg") MessageDto messageDto, Principal principal) {
        List<Message> texts = messageService.saveAndRetrieve(id, messageDto, principal);
        User user = userService.findByEmail(principal.getName());
        long forViewId;
        if (!texts.get(0).getToUser().getId().equals(user.getId())) {
            forViewId = texts.get(0).getToUser().getId();
        } else {
            forViewId = texts.get(0).getFromUser().getId();
        }
        model.addAttribute("texts", texts);
        return "redirect:/messages/" + forViewId;

    }

    @GetMapping("/messages/{id}/delete")
    public String deleteConversation(@PathVariable("id") long id) {
        conversationService.deleteById(id);
        return "redirect:/messages?deleted";
    }
}
