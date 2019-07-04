package ro.sci.bookwormscommunity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.sci.bookwormscommunity.model.Conversation;
import ro.sci.bookwormscommunity.model.Message;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.repositories.MessageRepository;
import ro.sci.bookwormscommunity.web.dto.MessageDto;

import java.util.List;

/**
 * Implementation for the {@link MessageService}.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @see MessageService
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    ConversationService conversationService;

    @Autowired
    private MessageRepository messageRepository;

    /**
     * Returns all the messages that have been sent within a specific conversation.
     *
     * @param conversationId the conversation identifier
     * @return a list of {@link Message} objects.
     */
    @Override
    public List<Message> getUserMessages(long conversationId) {
        return messageRepository.getUserMessages(conversationId);
    }

    /**
     * Save the new submitted message and retrieves all the messages including the new one.
     *
     * @param conversationId conversation identifier
     * @param messageDto     Data Transfer Object containing the message content.
     * @param fromUser       the user send the message.
     * @return a list of all the {@link Message} objects which correspond with provided conversation Id.
     */
    @Override
    public List<Message> saveAndRetrieve(long conversationId, MessageDto messageDto, User fromUser) {
        Conversation conversation = conversationService.findById(conversationId);

        User toUser;
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
