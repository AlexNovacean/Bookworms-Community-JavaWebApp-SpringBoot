package ro.sci.bookwormscommunity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.sci.bookwormscommunity.model.Conversation;
import ro.sci.bookwormscommunity.model.Message;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.repositories.MessageRepository;
import ro.sci.bookwormscommunity.web.dto.MessageDto;

import java.security.Principal;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    ConversationService conversationService;
    @Autowired
    UserService userService;
    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<Message> getUserMessages(long conversationId) {
        return messageRepository.getUserMessages(conversationId);
    }

    @Override
    public List<Message> saveAndRetrieve(long conversationId, MessageDto messageDto, Principal principal) {
        Conversation conversation = conversationService.findById(conversationId);

        User toUser;
        User fromUser = userService.findByEmail(principal.getName());
        if (conversation.getFromUser().getEmail().equals(fromUser.getEmail())) {
            toUser = conversation.getToUser();
        } else {
            toUser = conversation.getFromUser();
        }

        Message message = new Message(messageDto.getContent(), toUser, fromUser);
        message.setConversation(conversation);
        messageRepository.save(message);
        return messageRepository.getUserMessages(conversationId);
    }
}
