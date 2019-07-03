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

/**
 * Controller that handles the mapping for the application messaging system.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 */
@Controller
@RequestMapping("/messages/**")
public class MessagesController {

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    /**
     * Handles the {@link RequestMethod#GET} method used to send the list with all the conversation of the user.
     *
     * @param model     {@link Model} used to add attributes that requires to be returned to the View.
     * @param principal {@link Principal} object which stores the currently logged in user.
     * @return the name of the view where the conversations are displayed.
     */
    @GetMapping
    public String seeMessages(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("messages", conversationService.getUserConversations(user.getId()));
        model.addAttribute("user", user);
        return "messages";
    }

    /**
     * Handles the {@link RequestMethod#GET} method used to start a conversation and return the view with all the messages sent within an allready existing conversation between two users.
     *
     * @param id        the identifier of the user to which the message is sent.
     * @param bookName  the topic of the conversation, the name of the book for which the conversation is occurring
     * @param model     {@link Model} used to add attributes that requires to be returned to the View.
     * @param principal {@link Principal} object which stores the currently logged in user.
     * @return the name of the view where the messages are displayed.
     */
    @GetMapping("/messages/{id}")
    public String seeSpecific(@PathVariable("id") long id, @RequestParam(required = false) String bookName, Model model, Principal principal) {
        User toUser = userService.findById(id);
        User fromUser = userService.findByEmail(principal.getName());
        Conversation conversation = conversationService.startConversation(toUser.getId(), fromUser.getId(), bookName);
        List<Message> texts = messageService.getUserMessages(conversation.getId());
        model.addAttribute("texts", texts);
        model.addAttribute("conv", conversationService.findById(conversation.getId()));
        model.addAttribute("principal",principal);
        return "texts";
    }

    /**
     * Handles the {@link RequestMethod#GET} method used to return the view on which the messages of a conversation are displayed.
     *
     * @param id        the identifier of the conversation for which the messages are displayed.
     * @param model     {@link Model} used to add attributes that requires to be returned to the View.
     * @param principal {@link Principal} object which stores the currently logged in user.
     * @return the name of the view where the messages are displayed.
     */
    @GetMapping("/messages/conversation/{id}")
    public String openConversation(@PathVariable("id") long id, Model model, Principal principal) {
        List<Message> texts = messageService.getUserMessages(id);
        model.addAttribute("texts", texts);
        model.addAttribute("conv", conversationService.findById(id));
        model.addAttribute("principal", principal);
        return "texts";
    }

    /**
     * Handles the {@link RequestMethod#POST} method used to send a messages.
     *
     * @param id         the identifier of the conversation within the messages are sent.
     * @param model      {@link Model} used to add attributes that requires to be returned to the View.
     * @param messageDto {@link MessageDto} object that contains the content of the message that is being sent.
     * @param principal  {@link Principal} object which stores the currently logged in user.
     * @return the name of the view where the sent message is displayed.
     */
    @PostMapping("/messages/{id}")
    public String sendMessage(@PathVariable("id") long id, Model model, @ModelAttribute("msg") MessageDto messageDto, Principal principal) {
        List<Message> texts = messageService.saveAndRetrieve(id, messageDto, principal);
        Conversation conversation = conversationService.findById(id);
        User user = userService.findByEmail(principal.getName());
        long forViewId;
        if (!texts.get(0).getToUser().getId().equals(user.getId())) {
            forViewId = texts.get(0).getToUser().getId();
        } else {
            forViewId = texts.get(0).getFromUser().getId();
        }
        model.addAttribute("texts", texts);
        model.addAttribute("principal",principal);
        return "redirect:/messages/" + forViewId + "?bookName=" + conversation.getConversationTopic() + "#lastmsg";

    }

    /**
     * Handles the {@link RequestMethod#GET} method used delete an existing conversation.
     *
     * @param id identifier for the conversation that will be deleted.
     * @return the name of the view where the conversations are displayed.
     */
    @GetMapping("/messages/{id}/delete")
    public String deleteConversation(@PathVariable("id") long id) {
        conversationService.deleteById(id);
        return "redirect:/messages?deleted";
    }
}
